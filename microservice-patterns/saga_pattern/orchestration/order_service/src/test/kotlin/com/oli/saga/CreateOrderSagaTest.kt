package com.oli.saga

import com.oli.order.*
import com.oli.orderdetails.OrderDetailsDAO
import com.oli.persistence.*
import com.oli.proxies.AccountingServiceProxy
import com.oli.proxies.ConsumerServiceProxy
import com.oli.proxies.KitchenServiceProxy
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import kotlin.test.assertEquals

class CreateOrderSagaTest {
    companion object {
        private lateinit var sagaDefinition: SagaDefinition
        private val logger: org.slf4j.Logger? = LoggerFactory.getLogger(CreateOrderSagaTest::class.java.name)
        private val consumerServiceProxyMock: ConsumerServiceProxy = Mockito.mock(ConsumerServiceProxy::class.java)
        private val kitchenServiceProxyMock: KitchenServiceProxy = Mockito.mock(KitchenServiceProxy::class.java)
        private val accountingServiceProxyMock: AccountingServiceProxy = Mockito.mock(AccountingServiceProxy::class.java)
        private lateinit var createOrderSagaStateDAO: CreateOrderSagaStateDAO
        private lateinit var orderDetailsDAO: OrderDetailsDAO
        private lateinit var orderDAO: OrderDAO
        private lateinit var orderRepository: OrderRepository
        private lateinit var orderSagaAssociationDAO: OrderSagaAssociationDAO
        private lateinit var orderService: OrderService

        @BeforeClass
        @JvmStatic
        fun init() = runBlocking {
            DatabaseFactory.init()
            createOrderSagaStateDAO = CreateOrderSagaStateDAOImpl()
            orderDetailsDAO = OrderDetailsDAOImpl()
            orderDAO = OrderDAOImpl()
            orderSagaAssociationDAO = OrderSagaAssociationDAOImpl()
            orderRepository = OrderRepositoryImpl(orderDAO, orderSagaAssociationDAO, createOrderSagaStateDAO)
            orderService = OrderService(orderRepository, logger!!)
        }
    }

    @Test
    fun testStep() = runBlocking {
        val sagaState = createOrderSagaStateDAO.create(0, false, 0)
        val orderDetails = orderDetailsDAO.create(0, "test", listOf(OrderItem(1, 1)), Timestamp(System.currentTimeMillis()))
        // Mock for remote services, actual service for order service
        sagaDefinition = CreateOrderSagaDefinition(
            logger!!,
            sagaState,
            orderDetails,
            orderService,
            consumerServiceProxyMock,
            kitchenServiceProxyMock,
            accountingServiceProxyMock
        )
        for(i in 0..4){
            val retVal = sagaDefinition.step()
            assertEquals(SagaStepResult.UNFINISHED, retVal)
        }
        assertEquals(SagaStepResult.FINISHED, sagaDefinition.step())
    }

}