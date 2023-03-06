package com.oli.saga

import com.oli.orderdetails.OrderDetailsTable
import org.jetbrains.exposed.dao.id.IntIdTable


data class CreateOrderSagaState(
    override val sagaId: Int,
    override var currentStep: Int,
    override var rollingBack: Boolean,
    val orderDetailsId: Int?
) : SagaState {

    fun equalsIgnoreId(other: Any?): Boolean {
        if (other !is CreateOrderSagaState) return false
        if(other.currentStep != currentStep) return false
        if(other.rollingBack != rollingBack) return false
        if(other.orderDetailsId != orderDetailsId) return false
        return true
    }
}

object CreateOrderSagaStates : IntIdTable() {
    var currentStep = integer("currentStep")
    val rollingBack = bool("rollingBack")

    val orderDetailsId = reference("orderDetailsId", OrderDetailsTable.id).nullable()
}