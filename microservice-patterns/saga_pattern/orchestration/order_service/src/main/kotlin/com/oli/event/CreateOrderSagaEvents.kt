package com.oli.event

import com.oli.proxies.Customer
import kotlinx.serialization.Serializable

interface CreateOrderSagaEvent: SagaEvent

@Serializable
data class VerifyCustomerCommandEvent(
    override val sagaId: Int,
    val customer: Customer
) : CreateOrderSagaEvent

@Serializable
data class VerifyCustomerReplyEvent(
    override val sagaId: Int,
    val customer: Customer,
    val result: Boolean?
) : CreateOrderSagaEvent

@Serializable
data class CreateTicketCommandEvent(
    override val sagaId: Int
    // TODO
) : CreateOrderSagaEvent

@Serializable
data class CreateTicketReplyEvent(
    override val sagaId: Int
    // TODO
) : CreateOrderSagaEvent

@Serializable
data class AuthorizationCommandEvent(
    override val sagaId: Int,
    val userId: Int,
    val paymentInfo: String
) : CreateOrderSagaEvent

@Serializable
data class AuthorizationReplyEvent(
    override val sagaId: Int,
    val result: Boolean
) : CreateOrderSagaEvent

@Serializable
data class ApproveTicketCommandEvent(
    override val sagaId: Int
    // TODO
) : CreateOrderSagaEvent

@Serializable
data class ApplyTicketReplyEvent(
    override val sagaId: Int
    // TODO
) : CreateOrderSagaEvent