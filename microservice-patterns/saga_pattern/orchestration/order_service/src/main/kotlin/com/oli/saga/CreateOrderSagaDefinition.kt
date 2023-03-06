package com.oli.saga

import com.oli.order.Order
import com.oli.order.OrderService
import com.oli.orderdetails.OrderDetails
import com.oli.orderdetails.toOrderItems
import com.oli.proxies.AccountingServiceProxy
import com.oli.proxies.Customer
import com.oli.proxies.CustomerServiceProxy
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
    private val logger: Logger,
    private val orderSagaState: CreateOrderSagaState,
    private val orderDetails: OrderDetails,
    private val orderService: OrderService,
    private val consumerServiceProxy: CustomerServiceProxy,
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
            description =
                "Step 3: Create a ticket in the kitchen service in the pending state. Compensate by setting its state to rejected."
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
    private suspend fun orderServiceCreateOrder(): StepResult {
        val order = Order(0, orderDetails.userId, orderDetails.orderingDate, EntityStates.PENDING, orderDetails.orderDetailsItems.toOrderItems())
        val (createdOrder, createdAssociation) = orderService.createOrder(orderSagaState.sagaId, order)
        return if (createdOrder == null || createdAssociation == null) StepResult.FAILURE else StepResult.SUCCESS
    }

    /**
     * Compensate an order creation by setting its state to canceled.
     */
    private suspend fun orderServiceRejectOrder(): StepResult {
        val affectedEntries = orderService.updateOrderState(orderSagaState.sagaId, EntityStates.CANCELED)
        return if (affectedEntries == 1) StepResult.SUCCESS else StepResult.RETRY
    }

    private suspend fun consumerServiceVerifyConsumerDetails(): StepResult {
        // TODO Rework
        return if(consumerServiceProxy.sendVerifyCustomerDetailsCommand(orderDetails.userId, orderSagaState.sagaId, Customer(orderDetails.userId, 23, "Max", "Mustermann", listOf()))) StepResult.SUCCESS else StepResult.FAILURE
    }

    private suspend fun kitchenServiceCreateTicket(): StepResult {
        return if(kitchenServiceProxy.createTicket(orderSagaState.sagaId)) StepResult.SUCCESS else StepResult.FAILURE
    }

    private suspend fun kitchenServiceCancelTicket(): StepResult {
        val affectedEntries = kitchenServiceProxy.cancelOrder(orderSagaState.sagaId)
        return if(affectedEntries == 1) StepResult.SUCCESS else StepResult.RETRY
    }

    private suspend fun accountingServiceAuthorize(): StepResult {
        return if(accountingServiceProxy.authorize(orderSagaState.sagaId, orderDetails.userId, orderDetails.paymentInfo)) StepResult.SUCCESS else StepResult.FAILURE
    }

    private suspend fun kitchenServiceApproveTicket(): StepResult {
        val affectedEntries = kitchenServiceProxy.approveTicket(orderSagaState.sagaId)
        return if(affectedEntries == 1) StepResult.SUCCESS else StepResult.RETRY
    }

    private suspend fun orderServiceApproveTicket(): StepResult {
        val affectedEntries = orderService.updateOrderState(orderSagaState.sagaId, EntityStates.APPROVED)
        return if(affectedEntries == 1) StepResult.SUCCESS else StepResult.RETRY
    }
}

