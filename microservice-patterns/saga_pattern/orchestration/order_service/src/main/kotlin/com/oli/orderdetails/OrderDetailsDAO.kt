package com.oli.orderdetails

interface OrderDetailsDAO {
    suspend fun create(orderDetails: OrderDetails): OrderDetails?
    suspend fun read(id: Int): OrderDetails?
    suspend fun delete(id: Int): Boolean
}