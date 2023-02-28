package com.oli.orderdetails

import com.oli.order.OrderItem
import java.sql.Timestamp

interface OrderDetailsDAO {
    suspend fun create(
        userId: Int,
        paymentInfo: String,
        articleNumbers: List<OrderItem>,
        orderingDate: Timestamp
    ): OrderDetails
    suspend fun read(id: Int): OrderDetails?
    suspend fun delete(id: Int): Boolean
}