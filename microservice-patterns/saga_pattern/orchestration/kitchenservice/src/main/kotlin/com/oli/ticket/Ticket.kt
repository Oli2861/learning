package com.oli.ticket

import org.jetbrains.exposed.dao.id.IntIdTable


data class Ticket(
    val id: Int,
    val customerId: Int,
    val state: Int,
    val items: List<MenuItem>
) {
    fun equalsIgnoreTicketId(other: Any?): Boolean {
        if (other !is Ticket) return false
        if (other.customerId != customerId) return false
        if (other.state != state) return false
        if (!containsAllIgnoreId(other.items, items)) return false
        if (!containsAllIgnoreId(items, other.items)) return false
        return true
    }

    private fun containsAllIgnoreId(list: List<MenuItem>, list1: List<MenuItem>): Boolean = list.map { otherItem ->
        list1.any { it.equalIgnoreTicketId(otherItem) }
    }.all { true }

}

object Tickets : IntIdTable() {
    val customerId = integer("customerId")
    var state = integer("state")
}