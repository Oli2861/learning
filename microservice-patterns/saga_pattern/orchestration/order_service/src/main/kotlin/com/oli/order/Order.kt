package com.oli.order

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

data class Order(
    val id: Int,
    val userId: Int,
    val timestamp: Timestamp
)

object Orders : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("userId", 64)
    val date = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}
