package com.oli.event

import com.oli.customer.Customer
import kotlinx.serialization.Serializable

interface CreateOrderSagaEvent : SagaEvent

@Serializable
data class VerifyCustomerCommandEvent(
    override val correlationId: Int,
    override val sagaId: Int,
    val customer: Customer
) : CreateOrderSagaEvent

@Serializable
data class VerifyCustomerReplyEvent(
    override val correlationId: Int,
    override val sagaId: Int,
    val customer: Customer,
    val result: Boolean?
) : CreateOrderSagaEvent {
    constructor(
        verifyCustomerCommandEvent: VerifyCustomerCommandEvent,
        result: Boolean?
    ) : this(
        correlationId = verifyCustomerCommandEvent.correlationId,
        sagaId = verifyCustomerCommandEvent.sagaId,
        customer = verifyCustomerCommandEvent.customer,
        result = result
    )
}

