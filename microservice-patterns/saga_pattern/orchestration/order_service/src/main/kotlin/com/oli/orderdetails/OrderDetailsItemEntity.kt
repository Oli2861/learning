package com.oli.orderdetails

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class OrderDetailsItemEntity(id: EntityID<Int>): IntEntity(id){
    companion object: IntEntityClass<OrderDetailsItemEntity>(OrderDetailsItemEntities)

    var orderDetailsId by OrderDetailsItemEntities.orderDetailsId
    var articleNumber by OrderDetailsItemEntities.articleNumber
    var amount by OrderDetailsItemEntities.amount
}

object OrderDetailsItemEntities : IntIdTable() {
    val orderDetailsId = reference("orderDetailsId", OrderDetailsEntities)
    val articleNumber = integer("articleNumber")
    val amount = integer("amount")
}
