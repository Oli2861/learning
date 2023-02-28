package com.oli.orderdetails

import com.oli.order.OrderItem
import com.oli.order.Orders
import com.oli.saga.CreateOrderSagaStates.entityId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

/**
 * Business object
 */
data class OrderDetails(
    val id: Int,
    val paymentInfo: String,
    val userId: Int,
    val articleNumbers: List<OrderItem>,
    val orderingDate: Timestamp
) {
    constructor(
        orderDetailsEntity: OrderDetailsEntity,
        orderDetailsItemEntities: List<OrderDetailsItemEntity>
    ) : this(
        orderDetailsEntity.id.value,
        orderDetailsEntity.paymentInfo,
        orderDetailsEntity.userId,
        orderDetailsItemEntities.map { OrderItem(it.articleNumber, it.amount) },
        Timestamp.from(orderDetailsEntity.orderingDate)
    )

    constructor(
        entities: Pair<OrderDetailsEntity,
                List<OrderDetailsItemEntity>>
    ) : this(
        entities.first,
        entities.second
    )
}

/**
 * Exposed entity
 */
class OrderDetailsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderDetailsEntity>(OrderDetailsEntities)

    var userId by OrderDetailsEntities.userId
    var paymentInfo by OrderDetailsEntities.paymentInfo
    var orderingDate by OrderDetailsEntities.orderingDate
}

/**
 * Exposed table definition
 */
object OrderDetailsEntities : IntIdTable() {
    val userId = integer("userId")
    val paymentInfo = varchar("paymentInfo", 128)
    val orderingDate = timestamp("orderingDate")
}
