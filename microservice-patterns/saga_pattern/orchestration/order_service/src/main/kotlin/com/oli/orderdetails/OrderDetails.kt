package com.oli.orderdetails

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
    val orderDetailsItems: List<OrderDetailsItem>,
    val orderingDate: Timestamp
)

/**
 * Exposed table definition
 */
object OrderDetailsTable : IntIdTable() {
    val userId = integer("userId")
    val paymentInfo = varchar("paymentInfo", 128)
    val orderingDate = timestamp("orderingDate")
}
