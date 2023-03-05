package com.oli.ticket

interface TicketDAO {
    suspend fun create(ticket: Ticket): Ticket?
    suspend fun updateState(ticketId: Int, entityState: Int): Int
    suspend fun read(ticketId: Int): Ticket?
}