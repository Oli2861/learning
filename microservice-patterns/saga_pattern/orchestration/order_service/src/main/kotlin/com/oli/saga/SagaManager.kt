package com.oli.saga

import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.OrderDetailsDAO


class CreateOrderSagaManager(
    private val sagaId: Int,
    private val orderSagaStateDAO: CreateOrderSagaStateDAO,
    private val orderDetailsDAO: OrderDetailsDAO,
    private val sagaDefinition: CreateOrderSagaDefinition
) {
    private lateinit var sagaState: SagaState
    private lateinit var orderDetails: OrderDetails

    /**
     * Load saga data from the database.
     */
    private suspend fun setup(): Boolean {
        sagaState = orderSagaStateDAO.read(sagaId) ?: return false
        orderDetails = orderDetailsDAO.read(sagaId) ?: return false
        return true
    }

    /**
     * Execute the next step of the saga.
     */
    suspend fun executeNextStep() {
        val result = sagaDefinition.step()
        when (result) {
            SagaStepResult.UNFINISHED -> {

            }

            SagaStepResult.ROLLED_BACK -> {

            }

            SagaStepResult.FINISHED -> {

            }
        }

    }

    /**
     * Persist the state of the saga until execution can continue.
     * @return whether the execution of the operations lead to the expected result.
     */
    private suspend fun persist(): Boolean {
        val affectedRows = orderSagaStateDAO.update(sagaState)
        return affectedRows == 1
    }

}