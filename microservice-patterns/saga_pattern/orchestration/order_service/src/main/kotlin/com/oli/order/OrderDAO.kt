package com.oli.order

import java.sql.Timestamp

interface OrderDAO {
    suspend fun createOrder(userId: Int, timestamp: Timestamp): Order?
    suspend fun readOrder(id: Int): Order?
    suspend fun deleteOrder(id: Int): Boolean
}