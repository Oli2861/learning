package com.oli.saga

import com.oli.order.Order
import com.oli.order.OrderService
import com.oli.orderdetails.OrderDetails
import com.oli.proxies.AccountingServiceProxy
import com.oli.proxies.ConsumerServiceProxy
import com.oli.proxies.KitchenServiceProxy
import org.slf4j.Logger

/**
 * Saga to create an order.
 * The saga consists of multiple steps which include (forward) transactions and compensating transactions.
 * @param logger Logger to log saga progress.
 * @param orderDetails Details of the order such as which articles are ordered
 * @param orderSagaState State of the saga.
 * @param orderService Order service to create orders.
 * @param consumerServiceProxy Proxy class of the consumer service used to make RPC calls.
 * @param kitchenServiceProxy Proxy class of the kitchen service used to make RPC calls.
 * @param accountingServiceProxy Proxy class of the accounting service used to make RPC calls.
 * @property saga Definition of the saga steps including transactions and compensating transactions.
 */
class CreateOrderSagaDefinition(
    logger: Logger,
    private val orderSagaState: CreateOrderSagaState,
    private val orderDetails: OrderDetails,
    private val orderService: OrderService,
    private val consumerServiceProxy: ConsumerServiceProxy,
    private val kitchenServiceProxy: KitchenServiceProxy,
    private val accountingServiceProxy: AccountingServiceProxy
) : SagaDefinition(orderSagaState, logger, CreateOrderSagaDefinition::class.java.name) {
    override val saga: Saga = saga {
        step {
            description = "Step 1: Create an order in the pending state. Compensate by setting its state to rejected."
            transaction = ::orderServiceCreateOrder
            compensatingTransaction = ::orderServiceRejectOrder
        }
        step {
            description = "Step 2: Verify the consumer details by calling the consumer service."
            transaction = ::consumerServiceVerifyConsumerDetails
        }
        step {
            description = "Step 3: Create a ticket in the kitchen service in the pending state. Compensate by setting its state to rejected."
            transaction = ::kitchenServiceCreateTicket
            compensatingTransaction = ::kitchenServiceCancelTicket
        }
        step {
            description = "Step 4: Authorize the payment in the accounting service. This is a pivot transaction."
            transaction = ::accountingServiceAuthorize
        }
        step {
            description = "Step 5: Approve the ticket in the kitchen service by settings its state to approved."
            transaction = ::kitchenServiceApproveTicket
        }
        step {
            description = "Step 6: Approve the ticket in the order service by setting its state to approved."
            transaction = ::orderServiceApproveTicket
        }
    }

    /**
     * Create an order.
     * @return Whether the order was created successfully.
     */
    private suspend fun orderServiceCreateOrder(): Boolean {
        val order =
            Order(0, orderDetails.userId, orderDetails.orderingDate, EntityStates.PENDING, orderDetails.articleNumbers)
        val createdOrder = orderService.createOrder(orderSagaState.sagaId, order)
        return createdOrder != null
    }

    /**
     * Compensate an order creation by setting its state to canceled.
     */
    private suspend fun orderServiceRejectOrder(): Boolean {
        val affectedEntries = orderService.updateOrderState(orderSagaState.sagaId, EntityStates.PENDING)
        return affectedEntries == 1
    }

    private suspend fun consumerServiceVerifyConsumerDetails(): Boolean {
        return consumerServiceProxy.verifyConsumerDetails(orderSagaState.sagaId)
    }

    private suspend fun kitchenServiceCreateTicket(): Boolean {
        return kitchenServiceProxy.createTicket(orderSagaState.sagaId)
    }

    private suspend fun kitchenServiceCancelTicket(): Boolean {
        val affectedEntries = kitchenServiceProxy.cancelOrder(orderSagaState.sagaId)
        return affectedEntries == 1
    }

    private suspend fun accountingServiceAuthorize(): Boolean {
        return accountingServiceProxy.authorize(orderSagaState.sagaId, orderDetails.userId, orderDetails.paymentInfo)
    }

    private suspend fun kitchenServiceApproveTicket(): Boolean {
        val affectedEntries = kitchenServiceProxy.approveTicket(orderSagaState.sagaId)
        return affectedEntries == 1
    }

    private suspend fun orderServiceApproveTicket(): Boolean {
        val affectedEntries = orderService.updateOrderState(orderSagaState.sagaId, EntityStates.APPROVED)
        return affectedEntries == 1
    }
}

