package com.oli.persistence

import com.oli.order.Order
import com.oli.order.OrderDAO
import com.oli.order.OrderEntity
import com.oli.order.Orders
import com.oli.order.OrderItemEntity
import com.oli.order.OrderItems
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.sql.Timestamp

class OrderDAOImpl : OrderDAO {
    private fun dtoToOrder(orderEntity: OrderEntity, items: List<OrderItemEntity>) = Order(
        id = orderEntity.id.value,
        userId = orderEntity.userId,
        timestamp = Timestamp.from(orderEntity.date),
        orderState = orderEntity.state,
        items = items.map { it.articleNumber }
    )

    override suspend fun createOrder(order: Order): Order? {
        val orderEntity = DatabaseFactory.dbQuery {
            OrderEntity.new {
                userId = order.userId
                date = order.timestamp.toInstant()
                state = order.orderState
            }
        }
        val orderItems = DatabaseFactory.dbQuery {
            order.items.map { articleNum ->
                OrderItemEntity.new {
                    orderId = orderEntity.id
                    articleNumber = articleNum
                }
            }
        }
        return dtoToOrder(orderEntity, orderItems)
    }


    override suspend fun readOrder(id: Int): Order? = DatabaseFactory.dbQuery {
        val orderEntity = OrderEntity.find{ Orders.id eq id}.firstOrNull() ?: return@dbQuery null
        val items = OrderItemEntity.find( OrderItems.orderId eq orderEntity.id)
        dtoToOrder(orderEntity, items.toList())
    }

    override suspend fun deleteOrder(id: Int): Boolean = DatabaseFactory.dbQuery {
        OrderItems.deleteWhere { this.orderId eq id}
        Orders.deleteWhere { this.id eq id } > 0
    }

    override suspend fun updateOrderState(id: Int, newState: Int): Int = DatabaseFactory.dbQuery {
        Orders.update({ Orders.id eq id }) {
            it[state] = newState
        }
    }

}