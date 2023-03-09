package com.oli.orderdetails

import com.oli.proxies.Customer
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.sql.Timestamp

/**
 * Business object
 */
data class OrderDetails(
    val id: Int,
    val paymentInfo: String,
    val orderingDate: Timestamp,
    val customer: Customer,
    val orderDetailsItems: List<OrderDetailsItem>
)

/**
 * Exposed table definition. Not necessarily a great idea to store the customer as part of the order details in this service,
 * but it will do the trick for this learning project.
 * Does support only one address per order.
 */
object OrderDetailsTable : IntIdTable() {
    val paymentInfo = varchar("paymentInfo", 128)
    val orderingDate = timestamp("orderingDate")

    val customerId = integer("customerId")
    val age = integer("age")
    val firstName = varchar("firstName", 128)
    val lastName = varchar("lastName", 128)

    val postCode = integer("postCode")
    val city = varchar("city", 64)
    val houseNumber = varchar("houseNumber", 16)
}
