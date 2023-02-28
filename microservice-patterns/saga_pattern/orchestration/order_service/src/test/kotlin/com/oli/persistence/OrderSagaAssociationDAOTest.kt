package com.oli.persistence

import com.oli.order.Order
import com.oli.order.OrderDAO
import com.oli.order.OrderItem
import com.oli.order.OrderSagaAssociationDAO
import com.oli.saga.CreateOrderSagaStateDAO
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrderSagaAssociationDAOTest {

    companion object {
        private lateinit var orderDAO: OrderDAO
        private lateinit var sagaStateDAO: CreateOrderSagaStateDAO
        private lateinit var orderSagaAssociationDAO: OrderSagaAssociationDAO

        @BeforeClass
        @JvmStatic
        fun initialize() {
            DatabaseFactory.init()
            orderDAO = OrderDAOImpl()
            sagaStateDAO = CreateOrderSagaStateDAOImpl()
            orderSagaAssociationDAO = OrderSagaAssociationDAOImpl()
        }
    }

    @Test
    fun testCreate() = runBlocking {
        val order = orderDAO.createOrderReturnEntity(Order(0, 0, Timestamp(System.currentTimeMillis()), 0, listOf(OrderItem(1,1)))).first!!
        val createOrderSagaState = sagaStateDAO.create(0, false, 0)
        val orderSagaAssociation = orderSagaAssociationDAO.create(order, createOrderSagaState)

        assertEquals(order.id.value, orderSagaAssociation.orderId.value)
        assertEquals(createOrderSagaState.id.value, orderSagaAssociation.sagaId.value)
    }

    @Test
    fun testReadUsingSagaId() = runBlocking {
        val order = orderDAO.createOrderReturnEntity(Order(0, 0, Timestamp(System.currentTimeMillis()), 0, listOf(OrderItem(1, 2)))).first!!
        val createOrderSagaState = sagaStateDAO.create(0, false, 0)
        orderSagaAssociationDAO.create(order, createOrderSagaState)
        val association = orderSagaAssociationDAO.readUsingSagaId(createOrderSagaState.id.value)!!

        assertEquals(order.id.value, association.orderId.value)
        assertEquals(createOrderSagaState.id.value, association.sagaId.value)
    }

    @Test
    fun testDeleteAllForSaga() = runBlocking {
        val order = orderDAO.createOrderReturnEntity(Order(0, 0, Timestamp(System.currentTimeMillis()), 0, listOf(OrderItem(1, 1)))).first!!
        val createOrderSagaState = sagaStateDAO.create(0, false, 0)
        orderSagaAssociationDAO.create(order, createOrderSagaState)

        val retVal = orderSagaAssociationDAO.deleteAllForSaga(createOrderSagaState.id.value)
        val association = orderSagaAssociationDAO.readUsingSagaId(createOrderSagaState.id.value)

        assertEquals(1, retVal)
        assertNull(association)
    }

}