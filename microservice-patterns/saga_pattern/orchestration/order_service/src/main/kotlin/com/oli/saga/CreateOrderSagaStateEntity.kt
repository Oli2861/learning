package com.oli.saga

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * State of an order saga.
 * @param id id of the order saga state.
 * @param currentStep identifies the current step in the CreateOrderSaga saga.
 * @param rollingBack whether the saga is currently rolled back.
 * @param orderDetailsId identifies the associated order details.
 * @param orderId identifies created order. If -1, then the order has not been created yet.
 * @param ticketId identifies created ticket. If -1, then the ticket has not been created yet.
 */
class CreateOrderSagaStateEntity(id: EntityID<Int>): IntEntity(id), SagaState {
    companion object: IntEntityClass<CreateOrderSagaStateEntity>(CreateOrderSagaStates)

    override val sagaId: Int
        get() = super.id.value

    override var currentStep by CreateOrderSagaStates.currentStep
    override var rollingBack by CreateOrderSagaStates.rollingBack
    var orderDetailsId by CreateOrderSagaStates.orderDetailsId
}

object CreateOrderSagaStates : IntIdTable() {
    var currentStep = integer("currentStep")
    val rollingBack = bool("rollingBack")
    // TODO: Reference!
    val orderDetailsId = integer("orderDetailsId")
}