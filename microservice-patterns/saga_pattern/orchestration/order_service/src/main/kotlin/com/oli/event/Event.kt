package com.oli.event

interface Event {
}
interface SagaEvent : Event {
    val sagaId: Int
}

