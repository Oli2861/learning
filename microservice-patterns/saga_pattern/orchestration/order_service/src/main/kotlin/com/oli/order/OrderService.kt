package com.oli.order

import org.apache.commons.text.StringEscapeUtils
import org.slf4j.Logger
import java.sql.Timestamp
import kotlin.NumberFormatException

class OrderService(
    private val orderDAO: OrderDAO,
    private val logger: Logger
) {

    suspend fun createOrder(order: Order): Order? {
        return orderDAO.createOrder(order)
    }

    suspend fun readOrder(id: String): Order? {
        val orderId = stringIdToInt(id) ?: return null
        return orderDAO.readOrder(orderId)
    }

    suspend fun deleteOrder(id: String): Boolean {
        val orderId = stringIdToInt(id) ?: return false
        return orderDAO.deleteOrder(orderId)
    }

    private fun stringIdToInt(id: String): Int? = try {
        id.toInt()
    } catch (e: NumberFormatException) {
        val sanitizedId = StringEscapeUtils.escapeJava(id)
        logger.error("Failed to parse id to number: $sanitizedId")
        null
    }

    suspend fun cancelOrder(id: Int): Int {
        return orderDAO.updateOrderState(id, OrderStates.CANCELED)
    }
}