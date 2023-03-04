package com.oli.payment

import com.oli.event.AuthorizationCommandEvent
import com.oli.event.AuthorizationReplyEvent
import com.oli.event.MessageBroker

class PaymentService(private val messageBroker: MessageBroker) {

    fun authorizeCreditCard(commandEvent: AuthorizationCommandEvent) {

        val result = true
        // Reply
        val reply = AuthorizationReplyEvent(commandEvent.correlationId, commandEvent.sagaId, result)
        messageBroker.publishToQueue("create_order_saga_reply_channel", reply)
    }
}