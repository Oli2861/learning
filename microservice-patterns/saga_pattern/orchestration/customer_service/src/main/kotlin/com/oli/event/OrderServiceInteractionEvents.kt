package com.oli.event

import com.oli.customer.Customer
import kotlinx.serialization.Serializable

interface CreateOrderSagaEvent : SagaEvent

@Serializable
data class VerifyCustomerCommandEvent(
    override val sagaId: Int,
    val customer: Customer
) : CreateOrderSagaEvent

@Serializable
data class ReplyEvent(
    override val sagaId: Int,
    val result: Boolean
): CreateOrderSagaEvent
