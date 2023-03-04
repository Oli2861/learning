package com.oli.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Event {
    val correlationId: Int
}

interface SagaEvent : Event {
    val sagaId: Int
}

@Serializable
@SerialName("AuthorizationCommandEvent")
data class AuthorizationCommandEvent(
    override val correlationId: Int,
    override val sagaId: Int,
    val userId: Int,
    val paymentInfo: String
) : SagaEvent

@Serializable
@SerialName("AuthorizationReplyEvent")
data class AuthorizationReplyEvent(
    override val correlationId: Int,
    override val sagaId: Int,
    val result: Boolean
) : SagaEvent
