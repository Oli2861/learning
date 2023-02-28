package com.oli.persistence

import com.oli.order.OrderItem
import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.OrderDetailsDAO
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderDetailsDAOTest {

    companion object {
        private lateinit var orderDetailsDAO: OrderDetailsDAO

        @BeforeClass
        @JvmStatic
        fun initialize() {
            DatabaseFactory.init()
            orderDetailsDAO = OrderDetailsDAOImpl()
        }
    }

    @Test
    fun testCreate() = runBlocking {
        val time = Timestamp(System.currentTimeMillis())
        val created = orderDetailsDAO.create(
            0,
            "test",
            listOf(OrderItem(1, 1), OrderItem(2, 3)),
            time
        )

        assertTrue(created.id >= 0)
        assertEquals("test", created.paymentInfo)
        assertEquals(listOf(OrderItem(1, 1), OrderItem(2, 3)), created.articleNumbers)
        assertEquals(time, created.orderingDate)
    }
}