package com.oli.persistence

import com.oli.order.Orders
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    init {
        val host = System.getenv("POSTGRES_HOST")
        val port = System.getenv("POSTGRES_PORT")
        val user = System.getenv("POSTGRES_USER")
        val password = System.getenv("POSTGRES_PASSWORD")
        val databaseName = System.getenv("POSTGRES_DATABASE")

        val driver = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://$host:$port/$databaseName?user=$user&password=$password"
        val database = Database.connect(driver, jdbcURL)
        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Orders)

        }

        suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
    }
}