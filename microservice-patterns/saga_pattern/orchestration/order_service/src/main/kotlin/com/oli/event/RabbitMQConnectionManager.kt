package com.oli.event

import com.oli.order.Order
import com.rabbitmq.client.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.sql.Timestamp


@Serializable
data class TestEvent(val id: Int, val message: String, val order: Order)

interface MessageBroker {
    fun publishToQueue(queueName: String, event: TestEvent)
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

    override fun publishToQueue(queueName: String, event: TestEvent) {
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

fun main() {
    RabbitMQBroker.listenToQueue("test", onReceive = { println("Received 1 $it") }) {}
    RabbitMQBroker.listenToQueue("test", onReceive = { println("Received 2 $it") }) {}
    RabbitMQBroker.publishToQueue("test", TestEvent(0, "first msg", Order(1, 1, Timestamp(System.currentTimeMillis()), 1, listOf())))
    RabbitMQBroker.publishToQueue("test", TestEvent(0, "second msg", Order(2, 2, Timestamp(System.currentTimeMillis()), 1, listOf())))
    RabbitMQBroker.publishToQueue("test", TestEvent(0, "third msg", Order(3, 1, Timestamp(System.currentTimeMillis()), 1, listOf())))
    RabbitMQBroker.publishToQueue("test", TestEvent(0, "fourth msg", Order(4, 2, Timestamp(System.currentTimeMillis()),1, listOf())))

}
