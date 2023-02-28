package com.oli.saga

interface CreateOrderSagaStateDAO {
    suspend fun create(currentStep: Int, rollingBack: Boolean, orderDetailsId: Int): CreateOrderSagaStateEntity
    suspend fun read(id: Int): CreateOrderSagaStateEntity?
    suspend fun delete(id: Int): Int
    suspend fun update(sagaState: SagaState): Int
}