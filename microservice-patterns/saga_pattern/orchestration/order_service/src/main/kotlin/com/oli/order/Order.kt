package com.oli.order

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

data class OrderNoId(val userId: Int, val timestamp: Timestamp)
data class Order(
    val id: Int,
    val userId: Int,
    val timestamp: Timestamp
)

object Orders : Table() {
    val id = integer("id").autoIncrement()
    val userID = integer("userId")
    val date = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}
