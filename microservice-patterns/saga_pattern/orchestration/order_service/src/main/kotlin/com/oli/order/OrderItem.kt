package com.oli.order

import com.oli.order.OrderItems.references
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class OrderItem(val articleNumber: Int, val amount: Int){
    constructor(orderItemEntity: OrderItemEntity): this(orderItemEntity.articleNumber, orderItemEntity.amount)
}

class OrderItemEntity(id: EntityID<Int>): IntEntity(id){
    companion object: IntEntityClass<OrderItemEntity>(OrderItems)

    var orderId by OrderItems.orderId references Orders.id
    var articleNumber by OrderItems.articleNumber
    var amount by OrderItems.amount
}

object OrderItems : IntIdTable() {
    val orderId = reference("orderId", Orders)
    val articleNumber = integer("articleNumber")
    val amount = integer("amount")
}
