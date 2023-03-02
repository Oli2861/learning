package com.oli.persistence

import com.oli.address.Addresses
import com.oli.customer.Customers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(isTest: Boolean) {
        val database = if (isTest) connectToH2() else connectToPostgres()

        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Customers)
            SchemaUtils.create(Addresses)
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


    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}