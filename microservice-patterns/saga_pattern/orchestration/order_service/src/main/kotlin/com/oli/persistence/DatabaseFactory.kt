package com.oli.persistence

import com.oli.order.OrderItems
import com.oli.order.OrderSagaAssociations
import com.oli.order.Orders
import com.oli.orderdetails.OrderDetailsItems
import com.oli.orderdetails.OrderDetailsTable
import com.oli.saga.CreateOrderSagaStates
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(useEmbeddedDatabase: Boolean) {
        val database = if (useEmbeddedDatabase) connectToH2() else connectToPostgres()

        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(OrderItems)
            SchemaUtils.create(Orders)
            SchemaUtils.create(OrderDetailsTable)
            SchemaUtils.create(OrderDetailsItems)
            SchemaUtils.create(CreateOrderSagaStates)
            SchemaUtils.create(OrderSagaAssociations)
        }
    }

    private fun connectToPostgres(): Database {
        val host = System.getenv("POSTGRES_HOST")
        val port = System.getenv("POSTGRES_PORT")
        val user = System.getenv("POSTGRES_USER")
        val password = System.getenv("POSTGRES_PASSWORD")
        val databaseName = System.getenv("POSTGRES_DB")

        return Database.connect(
            url = "jdbc:postgresql://$host:$port/$databaseName?user=$user&password=$password",
            driver = "org.postgresql.Driver"
        )
    }

    private fun connectToH2(): Database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )


    // Function used to query the database. Will run blocking code on IO thread pool & won't block the current thread.
    suspend fun <T> dbQuery(blockingOperation: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { blockingOperation() }
}