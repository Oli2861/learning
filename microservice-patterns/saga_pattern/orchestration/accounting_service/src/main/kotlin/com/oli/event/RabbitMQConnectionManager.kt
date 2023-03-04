package com.oli.event

import com.rabbitmq.client.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import java.nio.charset.StandardCharsets



interface MessageBroker {
    fun publishToQueue(queueName: String, event: Event)
    fun listenToQueue(queueName: String, onReceive: (String) -> Unit, onCancel: () -> Unit)
}

object RabbitMQBroker : MessageBroker {
    private val logger by inject<Logger>(Logger::class.java)
    private val CONNECTION_INSTANCE: Connection
        get() = establishConnection(System.getenv("RABBITMQ_HOST") ?: "localhost")

    private var openChannels: MutableMap<Int, Channel> = mutableMapOf()
    private var declaredQueues: MutableSet<String> = mutableSetOf()

    private val EXCHANGE: String = System.getenv("RABBITMQ_EXCHANGE") ?: "exc"
    private const val PUBLISH_CHANNEL_ID: Int = 1
    private const val RECEIVE_CHANNEL_ID: Int = 2

    private fun establishConnection(host: String): Connection {
        val factory = ConnectionFactory()
        factory.host = host
        return factory.newConnection()
    }

    private fun getChannel(channelId: Int): Channel {
        if (openChannels[channelId] == null) {
            val channel = CONNECTION_INSTANCE.createChannel(channelId)
            openChannels[channelId] = channel
        }
        return openChannels[channelId]!!
    }

    private fun createQueueIfDoesNotExist(queueName: String, channel: Channel){
        if(!declaredQueues.contains(queueName)){
            channel.queueDeclare(queueName, false, false, false, null)
            declaredQueues.add(queueName)
        }
    }

    private fun publishMessage(queueName: String, message: String) {
        val channel = getChannel(PUBLISH_CHANNEL_ID)
        createQueueIfDoesNotExist(queueName, channel)
        channel.basicPublish(EXCHANGE, queueName, null, message.toByteArray())
    }

    override fun publishToQueue(queueName: String, event: Event) {
        val serializedEvent = Json.encodeToString(event)
        publishMessage(queueName, serializedEvent)
    }

    override fun listenToQueue(
        queueName: String,
        onReceive: (String) -> Unit,
        onCancel: () -> Unit
    ) {
        val channel = getChannel(RECEIVE_CHANNEL_ID)
        createQueueIfDoesNotExist(queueName, channel)

        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            val message = String(delivery.body, StandardCharsets.UTF_8)
            logger.debug("Received message on channel $consumerTag: $message")
            onReceive.invoke(message)
        }

        val cancelCallback = CancelCallback { consumerTag ->
            logger.debug("Channel $consumerTag closed.")
            onCancel.invoke()
        }

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback)
    }

}
