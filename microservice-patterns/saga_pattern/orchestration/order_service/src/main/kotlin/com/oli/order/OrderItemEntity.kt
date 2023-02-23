package com.oli.order

import com.oli.order.OrderItems.references
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class OrderItemEntity(id: EntityID<Int>): IntEntity(id){
    companion object: IntEntityClass<OrderItemEntity>(OrderItems)

    var orderId by OrderItems.orderId references Orders.id
    var articleNumber by OrderItems.articleNumber
}

object OrderItems : IntIdTable() {
    val orderId = reference("orderId", Orders)
    val articleNumber = integer("articleNumber")
}