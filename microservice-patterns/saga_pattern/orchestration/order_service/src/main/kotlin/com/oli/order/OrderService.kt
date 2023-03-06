package com.oli.order

import com.oli.event.CreateTicketReplyEvent
import com.oli.event.Event
import com.oli.event.VerifyCustomerReplyEvent
import com.oli.utility.stringIdToInt
import org.slf4j.Logger

class OrderService(
    private val orderRepository: OrderRepository,
    private val logger: Logger
) {

    suspend fun createOrder(sagaId: Int, order: Order): Pair<Order?, OrderSagaAssociation?> {
        return orderRepository.createOrder(sagaId, order)
    }

    suspend fun readOrder(id: String): Order? {
        val orderId = stringIdToInt(id, logger) ?: return null
        return orderRepository.readOrder(orderId)
    }

    suspend fun deleteOrder(id: String): Int {
        val orderId = stringIdToInt(id, logger) ?: return -1
        return orderRepository.deleteOrder(orderId)
    }

    suspend fun updateOrderState(sagaId: Int, orderState: Int): Int {
        return orderRepository.updateOrderState(sagaId, orderState)
    }

    fun handleEvent(event: Event) {
        println("KLASHDALSKDHLASKDJHASLDHASODLIKASHDOIASDH")
        println(logger.isDebugEnabled)
        logger.debug("Received event $event")
        when(event){
            is VerifyCustomerReplyEvent -> handleVerifyCustomerReplyEvent(event)
            is CreateTicketReplyEvent -> {}
            else -> logger.debug("Received unhandled event $event")
        }
    }

    private fun handleVerifyCustomerReplyEvent(event: VerifyCustomerReplyEvent) {
        logger.debug("Received $event")
    }

}