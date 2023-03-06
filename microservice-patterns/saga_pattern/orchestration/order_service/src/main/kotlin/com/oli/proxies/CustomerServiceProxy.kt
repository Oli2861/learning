package com.oli.proxies

import com.oli.event.MessageBroker
import com.oli.event.VerifyCustomerCommandEvent

interface CustomerServiceProxy {
    suspend fun sendVerifyCustomerDetailsCommand(correlationId: Int, sagaId: Int, customer: Customer): Boolean
}

class CustomerServiceProxyImpl(
    private val messageBroker: MessageBroker
) : CustomerServiceProxy {
    private val createOrderSagaCommandChannel = System.getenv("CREATE_ORDER_SAGA_COMMAND_CHANNEL") ?: "create_order_saga_command_channel"
    private val creatOrderSagaReplyChannel = System.getenv("CREATE_ORDER_SAGA_REPLY_CHANNEL") ?: "create_order_saga_reply_channel"

    override suspend fun sendVerifyCustomerDetailsCommand(correlationId: Int, sagaId: Int, customer: Customer): Boolean {
        val command = VerifyCustomerCommandEvent(correlationId, sagaId, customer)
        messageBroker.remoteProcedureCall(createOrderSagaCommandChannel, creatOrderSagaReplyChannel, command)
        return true
    }
}
