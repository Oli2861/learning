package com.oli.persistence

import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.OrderDetailsDAO
import com.oli.orderdetails.OrderDetailsItem
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
            DatabaseFactory.init(true)
            orderDetailsDAO = OrderDetailsDAOImpl()
        }
    }

    @Test
    fun testCreate() = runBlocking {
        val time = Timestamp(System.currentTimeMillis())
        val created = orderDetailsDAO.create(OrderDetails(0, "test", 1, listOf(OrderDetailsItem(0, 1, 1), OrderDetailsItem(0, 2, 3)), time))!!

        assertTrue(created.id >= 0)
        assertEquals("test", created.paymentInfo)
        assertTrue(OrderDetailsItem(100, 1, 1).equalsIgnoreOrderDetailsId(created.orderDetailsItems[0]))
        assertTrue(OrderDetailsItem(100, 2, 3).equalsIgnoreOrderDetailsId(created.orderDetailsItems[1]))
        assertEquals(time, created.orderingDate)
    }
}