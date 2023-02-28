package com.oli.persistence

import com.oli.order.OrderItem
import com.oli.orderdetails.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.sql.Timestamp

class OrderDetailsDAOImpl : OrderDetailsDAO{
    override suspend fun create(userId: Int, paymentInfo: String, articleNumbers: List<OrderItem>, orderingDate: Timestamp) = DatabaseFactory.dbQuery {
        val orderDetails = OrderDetailsEntity.new {
            this.userId = userId
            this.paymentInfo = paymentInfo
            this.orderingDate = orderingDate.toInstant()
        }
        val orderDetailsItems = articleNumbers.map {
            OrderDetailsItemEntity.new {
                this.orderDetailsId = orderDetails.id
                this.articleNumber = it.articleNumber
                this.amount = it.amount
            }
        }
        return@dbQuery OrderDetails(orderDetails, orderDetailsItems)
    }

    override suspend fun read(id: Int): OrderDetails? = DatabaseFactory.dbQuery {
        val orderDetails = OrderDetailsEntity.find(OrderDetailsEntities.id eq id).firstOrNull() ?: return@dbQuery null
        val items = OrderDetailsItemEntity.find(OrderDetailsItemEntities.orderDetailsId eq orderDetails.id.value).toList()
        return@dbQuery OrderDetails(orderDetails, items)
    }

    override suspend fun delete(id: Int): Boolean = DatabaseFactory.dbQuery {
        OrderDetailsItemEntities.deleteWhere { this.orderDetailsId eq id }
        OrderDetailsEntities.deleteWhere { this.id eq id } > 0
    }
}