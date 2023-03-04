package com.oli.event

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EventSerializerTest {

    @Test
    fun serializeAuthorizationCommandEventTest(){
        val event: Event = AuthorizationCommandEvent(1, 2, 3, "test")
        val actual = EventSerializer.serialize(event)

        val expected = "{\"type\":\"com.oli.event.AuthorizationCommandEvent\",\"correlationId\":1,\"sagaId\":2,\"userId\":3,\"paymentInfo\":\"test\"}"
        assertEquals(expected, actual)
    }

    @Test
    fun deserializeAuthorizationCommandEventTest() {
        val actual = EventSerializer.deserialize("{\"type\":\"com.oli.event.AuthorizationCommandEvent\",\"correlationId\":1,\"sagaId\":2,\"userId\":3,\"paymentInfo\":\"test\"}")

        val expected: Event = AuthorizationCommandEvent(1, 2, 3, "test")
        assertTrue(actual is SagaEvent)
        assertTrue(actual is CreateOrderSagaEvent)
        assertTrue(actual is AuthorizationCommandEvent)
        assertEquals(expected, actual)
    }

    @Test
    fun serializeAndDeserializeAuthorizationCommandEvent(){
        val event: Event = AuthorizationCommandEvent(1, 2, 3, "test")
        val serialized = EventSerializer.serialize(event)
        val deserialized = EventSerializer.deserialize(serialized)

        assertEquals(event, deserialized)
    }

    @Test
    fun serializeAuthorizationReplyEventTest(){
        val event: Event = AuthorizationReplyEvent(1, 2, false)
        val actual = EventSerializer.serialize(event)

        val expected = "{\"type\":\"com.oli.event.AuthorizationReplyEvent\",\"correlationId\":1,\"sagaId\":2,\"result\":false}"
        assertEquals(expected, actual)
    }

    @Test
    fun deserializeAuthorizationReplyEventTest() {
        val actual = EventSerializer.deserialize("{\"type\":\"com.oli.event.AuthorizationReplyEvent\",\"correlationId\":1,\"sagaId\":2,\"result\":false}")

        val expected: Event = AuthorizationReplyEvent(1, 2, false)
        assertTrue(actual is AuthorizationReplyEvent)
        assertEquals(expected, actual)
    }

    @Test
    fun serializeAndDeserializeReplyCommandEvent(){
        val event: Event = AuthorizationReplyEvent(1, 2, false)
        val serialized = EventSerializer.serialize(event)
        val deserialized = EventSerializer.deserialize(serialized)

        assertTrue(deserialized is SagaEvent)
        assertTrue(deserialized is CreateOrderSagaEvent)
        assertTrue(deserialized is AuthorizationReplyEvent)
        assertEquals(event, deserialized)
    }
}