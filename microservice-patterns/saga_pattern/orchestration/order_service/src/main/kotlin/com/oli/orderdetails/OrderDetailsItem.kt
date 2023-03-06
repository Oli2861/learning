package com.oli.orderdetails

import com.oli.order.OrderItem
import org.jetbrains.exposed.dao.id.IntIdTable

data class OrderDetailsItem(
    val orderDetailsId: Int,
    val articleNumber: Int,
    val amount: Int
) {
    fun equalsIgnoreOrderDetailsId(other: Any?): Boolean {
        if (other !is OrderDetailsItem) return false
        if (other.articleNumber != articleNumber) return false
        if (other.amount != amount) return false
        return true
    }

    fun toOrderItem(): OrderItem {
        return OrderItem(articleNumber, amount)
    }
}

fun List<OrderDetailsItem>.toOrderItems() = map { it.toOrderItem() }


object OrderDetailsItems : IntIdTable() {
    val orderDetailsId = reference("orderDetailsId", OrderDetailsTable)
    val articleNumber = integer("articleNumber")
    val amount = integer("amount")
}
