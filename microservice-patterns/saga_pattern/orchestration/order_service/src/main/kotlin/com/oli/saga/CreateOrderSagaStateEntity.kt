package com.oli.saga

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


data class CreateOrderSagaState(
    override val sagaId: Int,
    override var currentStep: Int,
    override var rollingBack: Boolean,
    val orderDetailsId: Int
) : SagaState {
    constructor(entity: CreateOrderSagaStateEntity) : this(
        entity.id.value,
        entity.currentStep,
        entity.rollingBack,
        entity.orderDetailsId
    )

    fun equalsIgnoreId(other: Any?): Boolean {
        if (other !is CreateOrderSagaState) return false
        if(other.currentStep != currentStep) return false
        if(other.rollingBack != rollingBack) return false
        if(other.orderDetailsId != orderDetailsId) return false
        return true
    }
}

/**
 * State of an order saga.
 * @param id id of the order saga state.
 * @param currentStep identifies the current step in the CreateOrderSaga saga.
 * @param rollingBack whether the saga is currently rolled back.
 * @param orderDetailsId identifies the associated order details.
 * @param orderId identifies created order. If -1, then the order has not been created yet.
 * @param ticketId identifies created ticket. If -1, then the ticket has not been created yet.
 */
class CreateOrderSagaStateEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CreateOrderSagaStateEntity>(CreateOrderSagaStates)

    var currentStep by CreateOrderSagaStates.currentStep
    var rollingBack by CreateOrderSagaStates.rollingBack
    var orderDetailsId by CreateOrderSagaStates.orderDetailsId
}

object CreateOrderSagaStates : IntIdTable() {
    var currentStep = integer("currentStep")
    val rollingBack = bool("rollingBack")

    // TODO: Reference!
    val orderDetailsId = integer("orderDetailsId")
}