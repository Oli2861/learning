package com.oli.order

import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.OrderDetailsDAO
import com.oli.persistence.*
import com.oli.saga.CreateOrderSagaState
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
        private lateinit var orderDetailsDAO: OrderDetailsDAO
        private lateinit var sagaStateDAO: CreateOrderSagaStateDAO
        private lateinit var orderSagaAssociationDAO: OrderSagaAssociationDAO
        private lateinit var orderDAO: OrderDAO

        @BeforeClass
        @JvmStatic
        fun init() {
            DatabaseFactory.init(true)
            orderDetailsDAO = OrderDetailsDAOImpl()
            sagaStateDAO = CreateOrderSagaStateDAOImpl()
            orderSagaAssociationDAO = OrderSagaAssociationDAOImpl()
            orderDAO = OrderDAOImpl()
            orderRepository = OrderRepositoryImpl(orderDAO, orderSagaAssociationDAO)
        }
    }

    @Test
    fun testCreateOrder() = runBlocking {
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 1, listOf(), Timestamp(System.currentTimeMillis())))
        val saga = sagaStateDAO.create(CreateOrderSagaState(0, 0, false, orderDetails!!.id))
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val (createdOrder: Order?, createdAssociation: OrderSagaAssociation?) = orderRepository.createOrder(saga!!.sagaId, order)

        assertNotNull(createdOrder)
        assertTrue(order.equalsIgnoreId(createdOrder))

        assertNotNull(createdAssociation)
        assertEquals(saga.sagaId, createdAssociation.sagaId)
        assertEquals(createdOrder.id, createdAssociation.orderId)
    }

    @Test
    fun testReadOrder() = runBlocking {
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 1, listOf(), Timestamp(System.currentTimeMillis())))
        val saga = sagaStateDAO.create(CreateOrderSagaState(0, 0, false, orderDetails!!.id))
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val created = orderRepository.createOrder(saga!!.sagaId, order).first
        val read = orderRepository.readOrder(created!!.id)

        assertTrue(order.equalsIgnoreId(read))
        assertTrue(created.equalsIgnoreId(read))
    }

    @Test
    fun testDeleteOrder() = runBlocking {
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 1, listOf(), Timestamp(System.currentTimeMillis())))
        val saga = sagaStateDAO.create(CreateOrderSagaState(0, 0, false, orderDetails!!.id))
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val created = orderRepository.createOrder(saga!!.sagaId, order).first!!

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
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 1, listOf(), Timestamp(System.currentTimeMillis())))
        val saga = sagaStateDAO.create(CreateOrderSagaState(0, 0, false, orderDetails!!.id))
        val order = Order(1, 1, Timestamp(System.currentTimeMillis()), EntityStates.PENDING, listOf(OrderItem(1, 1)))
        val created = orderRepository.createOrder(saga!!.sagaId, order).first!!

        val updatedRows = orderRepository.updateOrderState(saga.sagaId, EntityStates.CANCELED)
        assertEquals(1, updatedRows)

        val expected = Order(created.id, order.userId, order.timestamp, EntityStates.CANCELED, order.items)
        val read = orderRepository.readOrder(created.id)
        assertTrue(expected.equalsIgnoreId(read))
    }

}