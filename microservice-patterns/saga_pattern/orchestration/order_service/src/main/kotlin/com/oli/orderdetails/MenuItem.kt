package com.oli.orderdetails

import com.oli.order.OrderItem
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class MenuItem(val ticketId: Int = 0, val articleNumber: Int, val amount: Int) {
    fun equalsIgnoreOrderDetailsId(other: Any?): Boolean {
        if (other !is MenuItem) return false
        if (other.articleNumber != articleNumber) return false
        if (other.amount != amount) return false
        return true
    }

    fun toOrderItem(): OrderItem {
        return OrderItem(articleNumber, amount)
    }
}

fun List<MenuItem>.toOrderItems() = map { it.toOrderItem() }


object MenuItems : IntIdTable() {
    val orderDetailsId = reference("orderDetailsId", OrderDetailsTable)
    val articleNumber = integer("articleNumber")
    val amount = integer("amount")
}
