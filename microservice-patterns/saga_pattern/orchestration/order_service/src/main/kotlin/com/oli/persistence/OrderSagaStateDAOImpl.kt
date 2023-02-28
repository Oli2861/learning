package com.oli.persistence

import com.oli.saga.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class CreateOrderSagaStateDAOImpl : CreateOrderSagaStateDAO {

    override suspend fun create(
        currentStep: Int,
        rollingBack: Boolean,
        orderDetailsId: Int
    ): CreateOrderSagaState =
        DatabaseFactory.dbQuery {
            val entity = createAndReturnEntity(currentStep, rollingBack, orderDetailsId)
            CreateOrderSagaState(entity)
        }

    override suspend fun createAndReturnEntity(
        currentStep: Int,
        rollingBack: Boolean,
        orderDetailsId: Int
    ): CreateOrderSagaStateEntity = DatabaseFactory.dbQuery {
        CreateOrderSagaStateEntity.new {
            this.currentStep = currentStep
            this.rollingBack = rollingBack
            this.orderDetailsId = orderDetailsId
        }
    }

    override suspend fun read(id: Int): CreateOrderSagaState? {
        val entity = readEntity(id) ?: return null
        return CreateOrderSagaState(entity)
    }

    override suspend fun readEntity(id: Int): CreateOrderSagaStateEntity? = DatabaseFactory.dbQuery {
        CreateOrderSagaStateEntity.find(CreateOrderSagaStates.id eq id).firstOrNull()
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