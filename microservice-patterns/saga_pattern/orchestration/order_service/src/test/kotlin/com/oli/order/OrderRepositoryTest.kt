package com.oli.order

import com.oli.persistence.CreateOrderSagaStateDAOImpl
import com.oli.persistence.DatabaseFactory
import com.oli.persistence.OrderDAOImpl
import com.oli.persistence.OrderSagaAssociationDAOImpl
import com.oli.saga.CreateOrderSagaStateDAO
import com.oli.saga.EntityStates
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OrderRepositoryTest {
    companion object {
        private lateinit var orderRepository: OrderRepository
        private lateinit var sagaStateDAO: CreateOrderSagaStateDAO
        private lateinit var orderSagaAssociationDAO: OrderSagaAssociationDAO
        private lateinit var orderDAO: OrderDAO

        @BeforeClass
        @JvmStatic
        fun init() {
            DatabaseFactory.init()
            sagaStateDAO = CreateOrderSagaStateDAOImpl()
            orderSagaAssociationDAO = OrderSagaAssociationDAOImpl()
            orderDAO = OrderDAOImpl()
            orderRepository = OrderRepositoryImpl(orderDAO, orderSagaAssociationDAO, sagaStateDAO)
        }
    }

    @Test
    fun testCreateOrder() = runBlocking {
        val saga = sagaStateDAO.create(0, false, 1)
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val actual = orderRepository.createOrder(saga.sagaId, order)

        assertNotNull(actual)
        assertTrue(order.equalsIgnoreId(actual))

        val association = orderSagaAssociationDAO.readUsingSagaId(saga.sagaId)
        assertNotNull(association)
        assertEquals(saga.sagaId, association.sagaId.value)
        assertEquals(actual.id, association.orderId.value)

    }

    @Test
    fun testReadOrder() = runBlocking {
        val saga = sagaStateDAO.create(0, false, 1)
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val created = orderRepository.createOrder(saga.sagaId, order)!!
        val read = orderRepository.readOrder(created.id)

        assertTrue(order.equalsIgnoreId(read))
        assertTrue(created.equalsIgnoreId(read))
    }

    @Test
    fun testDeleteOrder() = runBlocking {
        val saga = sagaStateDAO.create(0, false, 1)
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val created = orderRepository.createOrder(saga.sagaId, order)!!

        val affectedRows = orderRepository.deleteOrder(created.id)
        assertEquals(1, affectedRows)

        val readAttempt = orderRepository.readOrder(created.id)
        assertNull(readAttempt)

        // Verify that associations are deleted as well.
        val association = orderSagaAssociationDAO.readUsingSagaId(saga.sagaId)
        assertNull(association)
    }

    @Test
    fun testUpdateOrderState() = runBlocking {
        val saga = sagaStateDAO.create(0, false, 1)
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val created = orderRepository.createOrder(saga.sagaId, order)!!

        val updatedRows = orderRepository.updateOrderState(saga.sagaId, EntityStates.CANCELED)
        assertEquals(1, updatedRows)

        val expected = Order(created.id, order.userId, order.timestamp, EntityStates.CANCELED, order.items)
        val read = orderRepository.readOrder(created.id)
        assertTrue(expected.equalsIgnoreId(read))
    }

}