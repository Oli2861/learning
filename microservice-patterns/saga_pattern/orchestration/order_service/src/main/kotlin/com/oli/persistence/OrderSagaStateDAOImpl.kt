package com.oli.persistence

import com.oli.saga.CreateOrderSagaStateEntity
import com.oli.saga.CreateOrderSagaStateDAO
import com.oli.saga.CreateOrderSagaStates
import com.oli.saga.SagaState
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class CreateOrderSagaStateDAOImpl : CreateOrderSagaStateDAO {

    override suspend fun create(
        currentStep: Int,
        rollingBack: Boolean,
        orderDetailsId: Int
    ): CreateOrderSagaStateEntity =
        DatabaseFactory.dbQuery {
            CreateOrderSagaStateEntity.new {
                this.currentStep = currentStep
                this.rollingBack = rollingBack
                this.orderDetailsId = orderDetailsId
            }
        }

    override suspend fun read(id: Int): CreateOrderSagaStateEntity? = DatabaseFactory.dbQuery {
        return@dbQuery CreateOrderSagaStateEntity.find(CreateOrderSagaStates.id eq id).firstOrNull()
    }

    override suspend fun delete(id: Int): Int = DatabaseFactory.dbQuery {
        CreateOrderSagaStates.deleteWhere { CreateOrderSagaStates.id eq id }
    }

    override suspend fun update(sagaState: SagaState): Int = DatabaseFactory.dbQuery {
        CreateOrderSagaStates.update({ CreateOrderSagaStates.id eq sagaState.sagaId }) {
            it[currentStep] = sagaState.currentStep
            it[rollingBack] = sagaState.rollingBack
        }
    }


}