package com.oli.persistence

import com.oli.order.OrderItems
import com.oli.order.Orders
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val host = System.getenv("POSTGRES_HOST")
        val port = System.getenv("POSTGRES_PORT")
        val user = System.getenv("POSTGRES_USER")
        val password = System.getenv("POSTGRES_PASSWORD")
        val databaseName = System.getenv("POSTGRES_DB")

        val database = Database.connect(
            url = "jdbc:postgresql://$host:$port/$databaseName?user=$user&password=$password",
            driver = "org.postgresql.Driver"
        )

        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Orders)
            SchemaUtils.create(OrderItems)

        }
    }

    // Function used to query the database. Will run blocking code on IO thread pool & won't block the current thread.
    suspend fun <T> dbQuery(blockingOperation: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { blockingOperation() }
}