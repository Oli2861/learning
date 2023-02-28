package com.oli.persistence

import com.oli.order.*
import com.oli.saga.CreateOrderSagaStateEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class OrderSagaAssociationDAOImpl : OrderSagaAssociationDAO {

    override suspend fun create(
        order: OrderEntity,
        createOrderSagaState: CreateOrderSagaStateEntity
    ): OrderSagaAssociationEntity =
        DatabaseFactory.dbQuery {
            OrderSagaAssociationEntity.new {
                orderId = order.id
                sagaId = createOrderSagaState.id
            }
        }

    suspend fun read(id: Int): OrderSagaAssociationEntity? = DatabaseFactory.dbQuery {
        OrderSagaAssociationEntity.findById(id)
    }

    override suspend fun readUsingSagaId(sagaId: Int): OrderSagaAssociationEntity? = DatabaseFactory.dbQuery {
        OrderSagaAssociationEntity.find(OrderSagaAssociations.sagaId eq sagaId).firstOrNull()
    }

    override suspend fun deleteAllForSaga(sagaId: Int): Int = DatabaseFactory.dbQuery {
        OrderSagaAssociations.deleteWhere { this.sagaId eq sagaId }
    }

    override suspend fun deleteAllForOrder(orderId: Int): Int = DatabaseFactory.dbQuery {
        OrderSagaAssociations.deleteWhere{this.orderId eq orderId}
    }

}