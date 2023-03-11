package com.oli.persistence

import com.oli.proxies.Customer
import com.oli.proxies.Address
import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.OrderDetailsDAO
import com.oli.orderdetails.MenuItem
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
        val customer = Customer(0, 23, "Max", "Mustermann", listOf(Address(0, 12345, "Mustertown", "5e")))
        val items = listOf(
            MenuItem(0, 1, 1),
            MenuItem(0, 2, 3)
        )
        val created = orderDetailsDAO.create(OrderDetails(0, "test", time, customer, items))!!

        assertTrue(created.id >= 0)
        assertEquals("test", created.paymentInfo)
        assertTrue(MenuItem(0, 1, 1).equalsIgnoreOrderDetailsId(created.menuItems[0]))
        assertTrue(MenuItem(0, 2, 3).equalsIgnoreOrderDetailsId(created.menuItems[1]))
        assertEquals(time, created.orderingDate)
    }
}