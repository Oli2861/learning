package com.oli.persistence

import com.oli.order.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class OrderDAOImpl : OrderDAO {

    override suspend fun createOrder(order: Order): Order = Order(createOrderReturnEntity(order))

    override suspend fun createOrderReturnEntity(order: Order): Pair<OrderEntity, List<OrderItemEntity>> {
        val orderEntity = DatabaseFactory.dbQuery {
            OrderEntity.new {
                userId = order.userId
                date = order.timestamp.toInstant()
                state = order.orderState
            }
        }
        val orderItemEntities = DatabaseFactory.dbQuery {
            order.items.map { orderItem ->
                OrderItemEntity.new {
                    orderId = orderEntity.id
                    articleNumber = orderItem.articleNumber
                    amount = orderItem.amount
                }
            }
        }
        return Pair(orderEntity, orderItemEntities)
    }


    override suspend fun readOrder(id: Int) = DatabaseFactory.dbQuery {
        val orderEntity = OrderEntity.find { Orders.id eq id }.firstOrNull() ?: return@dbQuery null
        val items = OrderItemEntity.find(OrderItems.orderId eq orderEntity.id).toList()
        return@dbQuery Order(orderEntity, items)
    }

    override suspend fun deleteOrder(id: Int): Int = DatabaseFactory.dbQuery {
        OrderItems.deleteWhere { this.orderId eq id }
        Orders.deleteWhere { this.id eq id }
    }

    override suspend fun updateOrderState(id: Int, newState: Int): Int = DatabaseFactory.dbQuery {
        Orders.update({ Orders.id eq id }) {
            it[state] = newState
        }
    }

}