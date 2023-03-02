package com.oli.event

import com.rabbitmq.client.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets


interface MessageBroker {
    fun publishToQueue(queueName: String, event: VerifyCustomerEvent)
    fun listenToQueue(queueName: String, onReceive: (String) -> Unit, onCancel: (String?) -> Unit)
}

object RabbitMQBroker : MessageBroker {
    private val CONNECTION_INSTANCE: Connection
        get() = establishConnection(System.getenv("RABBITMQ_HOST") ?: "localhost")

    private var OPEN_CHANNELS: MutableMap<Int, Channel> = mutableMapOf()

    private val EXCHANGE: String = System.getenv("RABBITMQ_EXCHANGE") ?: ""
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

    private fun publishMessage(queueName: String, message: String) {
        val channel = getChannel(PUBLISH_CHANNEL_ID)
        channel.queueDeclare(queueName, false, false, false, null)
        channel.basicPublish(EXCHANGE, queueName, null, message.toByteArray())
    }

    override fun publishToQueue(queueName: String, event: VerifyCustomerEvent) {
        val serializedEvent = Json.encodeToString(event)
        publishMessage(queueName, serializedEvent)
    }

    override fun listenToQueue(
        queueName: String,
        onReceive: (String) -> Unit,
        onCancel: (String?) -> Unit
    ) {
        val channel = getChannel(RECEIVE_CHANNEL_ID)
        channel.queueDeclare(queueName, false, false, false, null)

        val deliverCallback = DeliverCallback { _: String?, delivery: Delivery ->
            val message = String(delivery.body, StandardCharsets.UTF_8)
            onReceive.invoke(message)
        }

        channel.basicConsume(queueName, true, deliverCallback, onCancel)
    }
}