package com.oli.order

import org.apache.commons.text.StringEscapeUtils
import org.slf4j.Logger
import java.lang.NumberFormatException

class OrderService(
    private val orderDAO: OrderDAO,
    private val logger: Logger
) {

    suspend fun createOrder(order: OrderNoId): Order? {
        return orderDAO.addOrder(order.userId, order.timestamp)
    }

    suspend fun deleteOrder(id: String): Boolean {
        return try {
            val orderId = id.toInt()
            orderDAO.deleteOrder(orderId)
        } catch (e: NumberFormatException) {
           val sanitizedId = StringEscapeUtils.escapeJava(id)
            logger.error("Failed to parse id to number: $sanitizedId")
            false
        }
    }
}