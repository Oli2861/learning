package com.oli.ticket

import com.oli.persistence.DatabaseFactory
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TicketDAOImpl : TicketDAO {
    private fun resultRowToTicket(resultRow: ResultRow, items: List<MenuItem>): Ticket {
        return Ticket(
            id = resultRow[Tickets.id].value,
            customerId = resultRow[Tickets.customerId],
            sagaId = resultRow[Tickets.sagaId],
            state = resultRow[Tickets.state],
            items = items
        )
    }

    private fun resultRowToMenuItem(resultRow: ResultRow): MenuItem = MenuItem(
        ticketId = resultRow[MenuItems.ticketId].value,
        articleNumber = resultRow[MenuItems.itemId],
        amount = resultRow[MenuItems.amount]
    )

    override suspend fun create(ticket: Ticket): Ticket? = DatabaseFactory.dbQuery {
        val createdTicketId =
            Tickets.insertAndGetId {
                it[customerId] = ticket.customerId
                it[state] = ticket.state
                it[sagaId] = ticket.sagaId
            }
        ticket.items.map { menuItem ->
            MenuItems.insert {
                it[ticketId] = createdTicketId.value
                it[itemId] = menuItem.articleNumber
                it[amount] = menuItem.amount
            }
        }
        return@dbQuery readQuery(createdTicketId.value)
    }

    override suspend fun updateState(sagaId: Int, entityState: Int): Int = DatabaseFactory.dbQuery {
        Tickets.update({ Tickets.sagaId eq sagaId }) {
            it[state] = entityState
        }
    }

    override suspend fun read(ticketId: Int): Ticket? = DatabaseFactory.dbQuery { readQuery(ticketId) }

    // Without transaction to be used by the create function and the read function
    private fun readQuery(ticketId: Int): Ticket? {
        val items: List<MenuItem> = MenuItems.select(MenuItems.ticketId eq ticketId).toList().map(::resultRowToMenuItem)
        return Tickets.select(Tickets.id eq ticketId).map { resultRowToTicket(it, items) }.firstOrNull()
    }
}