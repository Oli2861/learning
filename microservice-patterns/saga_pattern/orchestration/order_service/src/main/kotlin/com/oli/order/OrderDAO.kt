package com.oli.order

import java.sql.Timestamp

interface OrderDAO {
    suspend fun createOrder(order: Order): Order?
    suspend fun readOrder(id: Int): Order?
    suspend fun deleteOrder(id: Int): Boolean
    suspend fun updateOrderState(id: Int, newState: Int): Int
}