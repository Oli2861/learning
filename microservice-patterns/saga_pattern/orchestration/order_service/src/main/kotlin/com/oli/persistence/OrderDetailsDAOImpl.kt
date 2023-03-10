package com.oli.persistence

import com.oli.orderdetails.*
import com.oli.proxies.Address
import com.oli.proxies.Customer
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.sql.Timestamp

class OrderDetailsDAOImpl : OrderDetailsDAO {

    private fun resultRowToOrderDetails(resultRow: ResultRow, menuItems: List<MenuItem>) = OrderDetails(
        id = resultRow[OrderDetailsTable.id].value,
        paymentInfo = resultRow[OrderDetailsTable.paymentInfo],
        orderingDate = Timestamp.from(resultRow[OrderDetailsTable.orderingDate]),
        customer = Customer(
            id = resultRow[OrderDetailsTable.customerId],
            age = resultRow[OrderDetailsTable.age],
            firstName = resultRow[OrderDetailsTable.firstName],
            lastName = resultRow[OrderDetailsTable.lastName],
            addresses = listOf(
                Address(
                    postCode = resultRow[OrderDetailsTable.postCode],
                    city = resultRow[OrderDetailsTable.city],
                    houseNumber = resultRow[OrderDetailsTable.houseNumber]
                )
            )
        ),
        menuItems = menuItems
    )

    private fun resultRowToOrderDetailsItem(resultRow: ResultRow) = MenuItem(
        articleNumber = resultRow[MenuItems.articleNumber],
        amount = resultRow[MenuItems.amount]
    )

    override suspend fun create(orderDetails: OrderDetails): OrderDetails? = DatabaseFactory.dbQuery {
        val createdOrderDetailsId = OrderDetailsTable.insertAndGetId {
            it[paymentInfo] = orderDetails.paymentInfo
            it[orderingDate] = orderDetails.orderingDate.toInstant()

            it[customerId] = orderDetails.customer.id
            it[age] = orderDetails.customer.age
            it[firstName] = orderDetails.customer.firstName
            it[lastName] = orderDetails.customer.lastName

            it[postCode] = orderDetails.customer.addresses.first().postCode
            it[city] = orderDetails.customer.addresses.first().city
            it[houseNumber] = orderDetails.customer.addresses.first().houseNumber

        }.value
        orderDetails.menuItems.forEach { orderItem ->
            MenuItems.insert {
                it[orderDetailsId] = createdOrderDetailsId
                it[articleNumber] = orderItem.articleNumber
                it[amount] = orderItem.amount
            }

        }
        return@dbQuery readQuery(createdOrderDetailsId)
    }

    private fun readQuery(id: Int): OrderDetails? {
        val items =
            MenuItems.select { MenuItems.orderDetailsId eq id }.map(::resultRowToOrderDetailsItem)
        return OrderDetailsTable.select { OrderDetailsTable.id eq id }.map { resultRowToOrderDetails(it, items) }
            .firstOrNull()
    }

    override suspend fun read(id: Int): OrderDetails? = DatabaseFactory.dbQuery { readQuery(id) }


    override suspend fun delete(id: Int): Boolean = DatabaseFactory.dbQuery {
        MenuItems.deleteWhere { this.orderDetailsId eq id }
        OrderDetailsTable.deleteWhere { this.id eq id } > 0
    }
}