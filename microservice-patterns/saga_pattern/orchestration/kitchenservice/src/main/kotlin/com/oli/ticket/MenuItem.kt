package com.oli.ticket

import org.jetbrains.exposed.dao.id.IntIdTable

data class MenuItem(val ticketId: Int, val itemId: Int, val amount: Int) {
    fun equalIgnoreTicketId(other: Any?): Boolean {
        if (other !is MenuItem) return false
        if (other.itemId != itemId) return false
        if (other.amount != amount) return false
        return true
    }
}

object MenuItems : IntIdTable() {
    val ticketId = reference("ticketId", Tickets.id)
    val itemId = integer("itemId")
    val amount = integer("amount")
}
