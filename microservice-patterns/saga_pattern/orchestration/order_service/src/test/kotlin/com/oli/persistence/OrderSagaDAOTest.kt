package com.oli.persistence

import com.oli.saga.CreateOrderSagaStateDAO
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderSagaDAOTest {
    companion object {
        private lateinit var createOrderSagaStateDAO: CreateOrderSagaStateDAO

        @BeforeClass
        @JvmStatic
        fun initialize() {
            DatabaseFactory.init()
            createOrderSagaStateDAO = CreateOrderSagaStateDAOImpl()
        }
    }

    @Test
    fun testCreate() = runBlocking {
        val created = createOrderSagaStateDAO.create(1, false, 0)

        assertEquals(1, created.currentStep)
        assertEquals(false, created.rollingBack)
        assertEquals(0, created.orderDetailsId)
    }

    @Test
    fun testRead() = runBlocking {
        val created = createOrderSagaStateDAO.create(1, false, 0)
        val readValue = createOrderSagaStateDAO.read(created.sagaId)!!

        assertEquals(1, readValue.currentStep)
        assertEquals(false, readValue.rollingBack)
        assertEquals(0, readValue.orderDetailsId)
    }

    @Test
    fun testDelete() = runBlocking {
        val created = createOrderSagaStateDAO.create(1, false, 0)
        val retVal = createOrderSagaStateDAO.delete(created.sagaId)
        val readValue = createOrderSagaStateDAO.read(created.sagaId)

        assertEquals(1, retVal)
        assertEquals(null, readValue)
    }

}