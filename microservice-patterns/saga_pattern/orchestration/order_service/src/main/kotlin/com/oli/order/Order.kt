package com.oli.order

import com.oli.plugins.TimestampAsLongSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

@Serializable
data class Order(
    val id: Int,
    val userId: Int,
    @Serializable(with = TimestampAsLongSerializer::class)
    val timestamp: Timestamp
)

@Serializable
data class OrderNoId(
    val userId: Int,
    @Serializable(with = TimestampAsLongSerializer::class)
    val timestamp: Timestamp
)

object Orders : Table() {
    val id = integer("id").autoIncrement()
    val userID = integer("userId")
    val date = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}
