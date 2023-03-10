package com.oli.ticket

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class MenuItem(val ticketId: Int = 0, val articleNumber: Int, val amount: Int) {
    fun equalIgnoreTicketId(other: Any?): Boolean {
        if (other !is MenuItem) return false
        if (other.articleNumber != articleNumber) return false
        if (other.amount != amount) return false
        return true
    }
}

object MenuItems : IntIdTable() {
    val ticketId = reference("ticketId", Tickets.id)
    val itemId = integer("itemId")
    val amount = integer("amount")
}
