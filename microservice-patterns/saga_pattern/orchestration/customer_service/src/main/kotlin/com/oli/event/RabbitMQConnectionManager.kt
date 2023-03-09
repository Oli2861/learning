package com.oli.event

import com.rabbitmq.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException
import java.nio.charset.StandardCharsets


interface MessageBroker {
    fun publishToQueue(queueName: String, event: Event)
    fun listenForRPC(scope: CoroutineScope, queueName: String, onReceive: suspend (String?, String) -> String, onCancel: () -> Unit)
}

object RabbitMQBroker : MessageBroker {
    private val CONNECTION_INSTANCE: Connection
        get() = establishConnection(System.getenv("RABBITMQ_HOST") ?: "localhost")

    private var OPEN_CHANNELS: MutableMap<Int, Channel> = mutableMapOf()
    private val declaredQueues: MutableSet<String> = mutableSetOf()

    private val defaultExchange: String = ""
    private const val PUBLISH_CHANNEL_ID: Int = 1
    private const val RECEIVE_CHANNEL_ID: Int = 2

    private fun establishConnection(host: String): Connection {
        val factory = ConnectionFactory()
        factory.host = host
        return factory.newConnection()
    }

    private fun getChannel(channelId: Int): Channel {
        if (OPEN_CHANNELS[channelId] == null) {
            val channel = CONNECTION_INSTANCE.createChannel(channelId)
            OPEN_CHANNELS[channelId] = channel
        }
        return OPEN_CHANNELS[channelId]!!
    }

    private fun createQueueIfDoesNotExist(queueName: String, channel: Channel) {
        if (!declaredQueues.contains(queueName)) {
            channel.queueDeclare(queueName, false, false, false, null)
            declaredQueues.add(queueName)
        }
    }

    override fun publishToQueue(queueName: String, event: Event) {
        // TODO
    }

    override fun listenForRPC(
        scope: CoroutineScope,
        queueName: String,
        onReceive: suspend (String?, String) -> String,
        onCancel: () -> Unit
    ) {
        val channel = getChannel(RECEIVE_CHANNEL_ID)
        createQueueIfDoesNotExist(queueName, channel)

        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            scope.launch {
                val message = String(delivery.body, StandardCharsets.UTF_8)
                val correlationId: String? = delivery.properties.correlationId
                val reply = try { onReceive(correlationId, message)
                } catch (_: RuntimeException) { "500" }
                val replyProps = AMQP.BasicProperties.Builder().correlationId(correlationId).build()
                channel.basicPublish(defaultExchange, delivery.properties.replyTo, replyProps, reply.toByteArray(Charsets.UTF_8))
                channel.basicAck(delivery.envelope.deliveryTag, false)
            }
        }

        val cancelCallback = CancelCallback { consumerTag -> onCancel.invoke() }

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback)
    }
}