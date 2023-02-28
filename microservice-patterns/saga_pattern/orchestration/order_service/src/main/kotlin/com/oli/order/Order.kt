package com.oli.order

import com.oli.plugins.TimestampAsLongSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

@Serializable
data class Order(
    val id: Int,
    val userId: Int,
    @Serializable(with = TimestampAsLongSerializer::class)
    val timestamp: Timestamp,
    val orderState: Int,
    val items: List<OrderItem>
) {
    constructor(
        orderEntity: OrderEntity,
        orderItemEntities: List<OrderItemEntity>
    ) : this(
        orderEntity.id.value,
        orderEntity.userId,
        Timestamp.from(orderEntity.date),
        orderEntity.state,
        orderItemEntities.map { OrderItem(it) }
    )

    constructor(pair: Pair<OrderEntity, List<OrderItemEntity>>) : this(pair.first, pair.second)

    fun equalsIgnoreId(other: Any?): Boolean {
        if(other !is Order) return false
        if(other.userId != userId) return false
        if (other.timestamp != timestamp) return false
        if(other.orderState != orderState) return false
        if (other.items != items) return false
        return true
    }
}

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(Orders)

    var userId by Orders.userId
    var date by Orders.date
    var state by Orders.state
}

object Orders : IntIdTable() {
    val userId = integer("userId")
    val date = timestamp("timestamp")
    val state = integer("orderState")
}
