package com.oli.persistence

import com.oli.orderdetails.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.sql.Timestamp

class OrderDetailsDAOImpl : OrderDetailsDAO {

    private fun resultRowToOrderDetails(resultRow: ResultRow, orderDetailsItems: List<OrderDetailsItem>) = OrderDetails(
        id = resultRow[OrderDetailsTable.id].value,
        paymentInfo = resultRow[OrderDetailsTable.paymentInfo],
        userId = resultRow[OrderDetailsTable.userId],
        orderDetailsItems = orderDetailsItems,
        orderingDate = Timestamp.from(resultRow[OrderDetailsTable.orderingDate])
    )

    private fun resultRowToOrderDetailsItem(resultRow: ResultRow) = OrderDetailsItem(
        orderDetailsId = resultRow[OrderDetailsItems.orderDetailsId].value,
        articleNumber = resultRow[OrderDetailsItems.articleNumber],
        amount = resultRow[OrderDetailsItems.amount]
    )

    override suspend fun create(orderDetails: OrderDetails): OrderDetails? = DatabaseFactory.dbQuery {
        val createdOrderDetailsId = OrderDetailsTable.insertAndGetId {
            it[userId] = orderDetails.userId
            it[paymentInfo] = orderDetails.paymentInfo
            it[orderingDate] = orderDetails.orderingDate.toInstant()

        }.value
        //TODO: NOT ORDER ITEMS
        orderDetails.orderDetailsItems.forEach { orderItem ->
            OrderDetailsItems.insert {
                it[orderDetailsId] = createdOrderDetailsId
                it[articleNumber] = orderItem.articleNumber
                it[amount] = orderItem.amount
            }

        }
        return@dbQuery readQuery(createdOrderDetailsId)
    }

    private fun readQuery(id: Int): OrderDetails? {
        val items = OrderDetailsItems.select { OrderDetailsItems.orderDetailsId eq id }.map(::resultRowToOrderDetailsItem)
        return OrderDetailsTable.select { OrderDetailsTable.id eq id }.map { resultRowToOrderDetails(it, items) }
            .firstOrNull()
    }

    override suspend fun read(id: Int): OrderDetails? = DatabaseFactory.dbQuery { readQuery(id) }


    override suspend fun delete(id: Int): Boolean = DatabaseFactory.dbQuery {
        OrderDetailsItems.deleteWhere { this.orderDetailsId eq id }
        OrderDetailsTable.deleteWhere { this.id eq id } > 0
    }
}