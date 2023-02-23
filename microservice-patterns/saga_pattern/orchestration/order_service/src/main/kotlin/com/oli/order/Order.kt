package com.oli.order

import com.oli.plugins.TimestampAsLongSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

object OrderStates{
    const val PENDING = 0
    const val APPROVED = 1
    const val CANCELED = 2
}

@Serializable
data class Order(
    val id: Int,
    val userId: Int,
    @Serializable(with = TimestampAsLongSerializer::class)
    val timestamp: Timestamp,
    val orderState: Int,
    val items: List<Int>
)

class OrderEntity(id: EntityID<Int>): IntEntity(id){
    companion object: IntEntityClass<OrderEntity>(Orders)

    var userId by Orders.userId
    var date by Orders.date
    var state by Orders.state
}

object Orders : IntIdTable() {
    val userId = integer("userId")
    val date = timestamp("timestamp")
    val state = integer("orderState")
}
