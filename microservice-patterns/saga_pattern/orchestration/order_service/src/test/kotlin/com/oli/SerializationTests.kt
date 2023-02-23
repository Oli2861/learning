package com.oli

import com.oli.order.Order
import com.oli.plugins.TimestampAsLongSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals

class SerializationTests {
    // Test serialization
    @Test
    fun testSerialize() {
        val time = 1676973532903L
        val expected = JsonPrimitive(time)
        val actual = Json.encodeToJsonElement(TimestampAsLongSerializer, Timestamp(time))
        assertEquals(expected, actual)
    }

    @Test
    fun testOrderSerialization() {
        val order = Order(0, 0, Timestamp(1676973532903L), 1, listOf(1, 2))
        val actual = Json.encodeToString(order)
        val expected = "{\"id\":0,\"userId\":0,\"timestamp\":1676973532903,\"orderState\":1,\"items\":[1,2]}"
        assertEquals(expected, actual)
    }

    // Test deserialization
    @Test
    fun testDeserialize() {
        val time = 1676973532903L
        val jsonElement = Json.encodeToJsonElement(TimestampAsLongSerializer, Timestamp(time))
        val actual = Json.decodeFromJsonElement<Timestamp>(TimestampAsLongSerializer, jsonElement)
        val expected = Timestamp(time)
        assertEquals(expected, actual)
    }

    @Test
    fun testOrderDeserialization() {
        val jsonString = "{\"id\":0,\"userId\":0,\"timestamp\":1676973532903,\"orderState\":1,\"items\":[1,2]}"
        val actual = Json.decodeFromString<Order>(jsonString)
        val expected = Order(0, 0, Timestamp(1676973532903L), 1, listOf(1, 2))
        assertEquals(expected, actual)
    }

}