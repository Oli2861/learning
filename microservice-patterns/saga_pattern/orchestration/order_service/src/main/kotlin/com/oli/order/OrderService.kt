package com.oli.order

import com.oli.utility.stringIdToInt
import org.slf4j.Logger

class OrderService(
    private val orderRepository: OrderRepository,
    private val logger: Logger
) {

    suspend fun createOrder(sagaId: Int, order: Order): Order? {
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

}