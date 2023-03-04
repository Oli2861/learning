package com.oli.event

interface Event {
    val correlationId: Int
}

interface SagaEvent: Event{
    val sagaId: Int
}

data class AuthorizationCommandEvent(
    override val correlationId: Int,
    override val sagaId: Int,
    val userId: Int,
    val paymentInfo: String
) : SagaEvent

data class AuthorizationReplyEvent(
    override val correlationId: Int,
    override val sagaId: Int,
    val result: Boolean
) : SagaEvent
