package com.oli.saga

interface CreateOrderSagaStateDAO {
    suspend fun create(currentStep: Int, rollingBack: Boolean, orderDetailsId: Int): CreateOrderSagaState
    suspend fun createAndReturnEntity(currentStep: Int, rollingBack: Boolean, orderDetailsId: Int): CreateOrderSagaStateEntity

    suspend fun read(id: Int): CreateOrderSagaState?

    suspend fun readEntity(id: Int): CreateOrderSagaStateEntity?

    suspend fun delete(id: Int): Int
    suspend fun update(sagaState: SagaState): Int
}