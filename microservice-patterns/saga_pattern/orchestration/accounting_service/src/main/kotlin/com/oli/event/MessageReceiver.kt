package com.oli.event

import com.oli.payment.PaymentService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.commons.text.StringEscapeUtils
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger

object MessageReceiver {
    private val logger by inject<Logger>(Logger::class.java)
    private val paymentService by inject<PaymentService>(PaymentService::class.java)

    fun init() {
        RabbitMQBroker.listenToQueue(
            queueName = "accounting_service_request_channel",
            onReceive = { message ->
                try {
                    val commandEvent = Json.decodeFromString<AuthorizationCommandEvent>(message)
                    paymentService.authorizeCreditCard(commandEvent)
                } catch (e: IllegalArgumentException) {
                    logger.debug("Could not decode the received event to a instance of CreditCardInfo. Received message: ${StringEscapeUtils.escapeJava(message)}")
                }
            },
            onCancel = {
                // TODO
            }
        )
    }

}