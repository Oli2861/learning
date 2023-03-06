package com.oli.persistence

import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.OrderDetailsDAO
import com.oli.saga.CreateOrderSagaState
import com.oli.saga.CreateOrderSagaStateDAO
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals

class OrderSagaDAOTest {
    companion object {
        private lateinit var createOrderSagaStateDAO: CreateOrderSagaStateDAO
        private lateinit var orderDetailsDAO: OrderDetailsDAO

        @BeforeClass
        @JvmStatic
        fun initialize() {
            DatabaseFactory.init(true)
            createOrderSagaStateDAO = CreateOrderSagaStateDAOImpl()
            orderDetailsDAO = OrderDetailsDAOImpl()
        }
    }

    @Test
    fun testCreate() = runBlocking {
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 0, listOf(), Timestamp(System.currentTimeMillis())))
        val created = createOrderSagaStateDAO.create(CreateOrderSagaState(0, 1, false, orderDetails!!.id))

        assertEquals(1, created!!.currentStep)
        assertEquals(false, created.rollingBack)
    }

    @Test
    fun testRead() = runBlocking {
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 0, listOf(), Timestamp(System.currentTimeMillis())))
        val created = createOrderSagaStateDAO.create(
            CreateOrderSagaState(0, 1, false, orderDetails!!.id))
        val readValue = createOrderSagaStateDAO.read(created!!.sagaId)!!

        assertEquals(1, readValue.currentStep)
        assertEquals(false, readValue.rollingBack)
    }

    @Test
    fun testDelete() = runBlocking {
        val orderDetails = orderDetailsDAO.create(OrderDetails(0, "", 0, listOf(), Timestamp(System.currentTimeMillis())))
        val created = createOrderSagaStateDAO.create(CreateOrderSagaState(0, 1, false, orderDetails!!.id))
        val retVal = createOrderSagaStateDAO.delete(created!!.sagaId)
        val readValue = createOrderSagaStateDAO.read(created.sagaId)

        assertEquals(1, retVal)
        assertEquals(null, readValue)
    }

}