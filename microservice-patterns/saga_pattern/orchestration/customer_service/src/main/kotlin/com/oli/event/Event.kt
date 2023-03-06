package com.oli.event

interface Event {
    val correlationId: Int
}
interface SagaEvent : Event {
    val sagaId: Int
}

