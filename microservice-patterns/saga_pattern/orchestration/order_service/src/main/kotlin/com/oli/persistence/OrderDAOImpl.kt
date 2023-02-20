package com.oli.persistence

import com.oli.order.Order
import com.oli.order.OrderDAO
import com.oli.order.Orders
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import java.sql.Timestamp

class OrderDAOImpl : OrderDAO {
    private fun resultRowToOrder(row: ResultRow): Order = Order(
        id = row[Orders.id],
        userId = row[Orders.userID],
        timestamp = Timestamp.from(row[Orders.date])
    )

    override suspend fun addOrder(userId: Int, timestamp: Timestamp): Order? = DatabaseFactory.dbQuery {
        val insertStatement = Orders.insert {
            it[userID] = userId
            it[date] = timestamp.toInstant()
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToOrder)
    }

    override suspend fun deleteOrder(id: Int): Boolean = DatabaseFactory.dbQuery {
        Orders.deleteWhere { this.id eq id } > 0
    }

}