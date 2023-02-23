package com.oli

import com.oli.order.Order
import com.oli.order.OrderStates
import com.oli.persistence.DatabaseFactory
import com.oli.persistence.OrderDAOImpl
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderDAOTest {
    companion object {
        private lateinit var dao: OrderDAOImpl

        @BeforeClass
        @JvmStatic
        fun initialize() {
            DatabaseFactory.init()
            dao = OrderDAOImpl()
        }
    }

    @Test
    fun testInsert() = runBlocking {
        val order = Order(
            id = 0,
            userId = 1,
            timestamp = Timestamp(System.currentTimeMillis()),
            orderState = 1,
            items = listOf(0, 1, 5)
        )
        val actual = dao.createOrder(order)!!
        println(actual)
        assertTrue(actual.id > 0)
        assertEquals(order.userId, actual.userId)
        assertEquals(order.timestamp, actual.timestamp)
        assertEquals(order.orderState, actual.orderState)
        order.items.forEach { assertTrue(actual.items.contains(it)) }
    }

    @Test
    fun testReadOrder() = runBlocking {
        val order = insertSampleOrder()
        val actual = dao.readOrder(order.id)!!
        assertTrue(actual.id > 0)
        assertEquals(order.userId, actual.userId)
        assertEquals(order.timestamp, actual.timestamp)
        assertEquals(order.orderState, actual.orderState)
        order.items.forEach { assertTrue(actual.items.contains(it)) }
    }

    @Test
    fun testDeleteOrder() = runBlocking {
        val order = insertSampleOrder()
        val result = dao.deleteOrder(order.id)
        val readAttempt = dao.readOrder(order.id)

        assertTrue(result)
        assertEquals(null, readAttempt)

    }

    @Test
    fun testUpdateOrderState() = runBlocking {
        val order = insertSampleOrder()
        val affectedRows = dao.updateOrderState(order.id, OrderStates.CANCELED)
        val updatedEntry = dao.readOrder(order.id)!!

        assertEquals(1, affectedRows)
        assertEquals(order.id, updatedEntry.id)
        assertEquals(OrderStates.CANCELED, updatedEntry.orderState)

    }

    private suspend fun insertSampleOrder(): Order {
        val order = Order(
            id = 0,
            userId = 1,
            timestamp = Timestamp(System.currentTimeMillis()),
            orderState = OrderStates.PENDING,
            items = listOf(0, 1, 5)
        )
        return dao.createOrder(order)!!
    }

}