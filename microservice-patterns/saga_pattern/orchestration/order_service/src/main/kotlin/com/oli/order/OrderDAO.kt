package com.oli.order

import com.oli.order.Order
import java.sql.Timestamp

interface OrderDAO {
    suspend fun addOrder(userId: Int, timestamp: Timestamp): Order?
    suspend fun deleteOrder(id: Int): Boolean
}