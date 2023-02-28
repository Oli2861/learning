package com.oli.order

import com.oli.saga.CreateOrderSagaStates
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class OrderSagaAssociationEntity(id: EntityID<Int>): IntEntity(id){
    companion object: IntEntityClass<OrderSagaAssociationEntity>(OrderSagaAssociations)

    var orderId by OrderSagaAssociations.orderId
    var sagaId by OrderSagaAssociations.sagaId
}

object OrderSagaAssociations: IntIdTable(){
    val orderId = reference("orderId", Orders)
    val sagaId = reference("sagaId", CreateOrderSagaStates)
}