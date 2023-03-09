package com.oli.event

import com.oli.proxies.Address
import com.oli.proxies.Customer
import com.oli.proxies.CustomerServiceProxyImpl
import com.rabbitmq.client.*
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CompletableFuture

interface MessageBroker {
    fun listenForRPC(queueName: String, onReceive: (String?, String) -> Unit, onCancel: () -> Unit)
    suspend fun remoteProcedureCall(queueName: String, replyQueueName: String, event: Event): String?
}

object RabbitMQBroker : MessageBroker {
    private val logger by inject<Logger>(Logger::class.java)
    private val CONNECTION_INSTANCE: Connection
        get() = establishConnection(System.getenv("RABBITMQ_HOST") ?: "localhost")

    private val openChannels: MutableMap<Int, Channel> = mutableMapOf()
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
        if (openChannels[channelId] == null) {
            val channel = CONNECTION_INSTANCE.createChannel(channelId)
            openChannels[channelId] = channel
        }
        return openChannels[channelId]!!
    }

    private fun createQueueIfDoesNotExist(queueName: String, channel: Channel) {
        if (!declaredQueues.contains(queueName)) {
            channel.queueDeclare(queueName, false, false, false, null)
            declaredQueues.add(queueName)
        }
    }

    override suspend fun remoteProcedureCall(queueName: String, replyQueueName: String, event: Event): String? {
        val message = EventSerializer.serialize(event)
        val channel = getChannel(PUBLISH_CHANNEL_ID)
        createQueueIfDoesNotExist(queueName, channel)
        val replyChannel = getChannel(RECEIVE_CHANNEL_ID)
        createQueueIfDoesNotExist(replyQueueName, replyChannel)

        val correlationId = UUID.randomUUID().toString()
        val properties = AMQP.BasicProperties.Builder().correlationId(correlationId).replyTo(replyQueueName).build()
        channel.basicPublish(defaultExchange, queueName, properties, message.toByteArray())
        println("Send message $message")

        return waitForResponse(replyChannel, replyQueueName, correlationId)
    }

    // TODO: This is blocking!
    private fun waitForResponse(replyChannel: Channel, replyQueueName: String, correlationId: String): String? {
        val response = CompletableFuture<String>();
        replyChannel.basicConsume(replyQueueName, true,
            { consumerTag: String?, delivery: Delivery ->
                if (delivery.properties.correlationId.equals(correlationId)) {
                    response.complete(String(delivery.body, Charsets.UTF_8))
                }
            }
        ) { consumerTag: String? ->
        }
        return response.get()
    }

    override fun listenForRPC(
        queueName: String,
        onReceive: (String?, String) -> Unit,
        onCancel: () -> Unit
    ) {
        val channel = getChannel(RECEIVE_CHANNEL_ID)
        channel.exchangeDeclare(defaultExchange, BuiltinExchangeType.DIRECT)
        createQueueIfDoesNotExist(queueName, channel)

        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            val message = String(delivery.body, StandardCharsets.UTF_8)
            val correlationId: String? = delivery.properties.correlationId
            logger.debug("Received message with correlation id $correlationId on channel $consumerTag: $message")
            onReceive.invoke(correlationId, message)
        }

        val cancelCallback = CancelCallback { consumerTag ->
            logger.debug("Channel $consumerTag closed.")
            onCancel.invoke()
        }

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback)
    }

}

fun main() = runBlocking {
    val customer = Customer(1, 28, "Max", "Mustermann", listOf(Address(0, 9123, "Mustertown", "2c")))
    //println(CustomerServiceProxyImpl(RabbitMQBroker).sendVerifyCustomerDetailsCommand(1, customer))
}