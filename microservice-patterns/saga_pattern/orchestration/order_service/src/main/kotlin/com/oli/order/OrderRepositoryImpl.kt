package com.oli.order

import com.oli.saga.CreateOrderSagaStateDAO
import com.oli.saga.CreateOrderSagaStateEntity


class OrderRepositoryImpl(
    private val orderDAO: OrderDAO,
    private val orderSagaAssociationDAO: OrderSagaAssociationDAO,
    private val createOrderSagaStateDAO: CreateOrderSagaStateDAO
) : OrderRepository {

    override suspend fun createOrder(sagaId: Int, order: Order): Order? {
        val saga = createOrderSagaStateDAO.readEntity(sagaId) ?: return null
        return createOrder(saga, order)
    }

    private suspend fun createOrder(saga: CreateOrderSagaStateEntity, order: Order): Order {
        val (orderEntity, orderItems) = orderDAO.createOrderReturnEntity(order)
        val orderSagaAssociation = orderSagaAssociationDAO.create(orderEntity, saga)
        return Order(orderEntity, orderItems)
    }

    override suspend fun readOrder(orderId: Int): Order? {
        return orderDAO.readOrder(orderId)
    }

    override suspend fun deleteOrder(orderId: Int): Int {
        orderSagaAssociationDAO.deleteAllForOrder(orderId)
        return orderDAO.deleteOrder(orderId)
    }

    override suspend fun updateOrderState(sagaId: Int, orderState: Int): Int {
        val orderSagaAssociation = orderSagaAssociationDAO.readUsingSagaId(sagaId) ?: return -1
        return orderDAO.updateOrderState(orderSagaAssociation.orderId.value, orderState)
    }

}