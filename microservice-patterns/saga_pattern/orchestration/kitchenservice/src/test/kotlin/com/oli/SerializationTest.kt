package com.oli

import com.oli.event.CreateTicketCommandEvent
import com.oli.event.EventSerializer
import com.oli.ticket.MenuItem
import org.junit.Test
import kotlin.test.assertEquals

class SerializationTest {

    @Test
    fun test(){
        val msg = "{\"type\":\"com.oli.event.CreateTicketCommandEvent\",\"sagaId\":1,\"customerId\":1,\"menuItems\":[{\"articleNumber\":1,\"amount\":2},{\"articleNumber\":3,\"amount\":4}]}"
        val actual = EventSerializer.deserialize(msg)
        val expected = CreateTicketCommandEvent(1, 1, listOf(MenuItem(0, 1, 2), MenuItem(0, 3, 4)))
        assertEquals(expected, actual)
    }

}