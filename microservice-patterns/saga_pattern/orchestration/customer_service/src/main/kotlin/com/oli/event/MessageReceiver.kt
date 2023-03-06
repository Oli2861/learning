package com.oli.event

import com.oli.customer.CustomerService
import kotlinx.coroutines.*
import org.apache.commons.text.StringEscapeUtils
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import java.lang.IllegalArgumentException

object MessageReceiver {
    private val logger by inject<Logger>(Logger::class.java)
    private val customerService by inject<CustomerService>(CustomerService::class.java)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val createOrderSagaCommandChannel = System.getenv("CREATE_ORDER_SAGA_COMMAND_CHANNEL") ?: "create_order_saga_command_channel"

    fun init() {
        RabbitMQBroker.listenForRPC(
            queueName = createOrderSagaCommandChannel,
            onReceive = { correlationId: String?, message ->
                val result =  try {
                    val event = EventSerializer.deserialize(message)
                    // TODO: Remove run blocking
                    runBlocking {
                        customerService.handleEvent(event)
                    }
                }catch (e: IllegalArgumentException){
                    logger.debug("Could not decode the received event. Received message: ${StringEscapeUtils.escapeJava(message)}")
                    ""
                }
                result
            },
            onCancel = {
                // TODO
            }
        )
    }

}