package com.oli.persistence

import com.oli.user.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val postgresHost = System.getenv("POSTGRES_HOST")
        val postgresPort = System.getenv("POSTGRES_PORT")
        val postgresUser = System.getenv("POSTGRES_USER")
        val postgresPassword = System.getenv("POSTGRES_PASSWORD")
        val postgresDatabase = System.getenv("POSTGRES_DB")

        val driverClassName = "org.postgresql.Driver"
        val jdbcURL =
            "jdbc:postgresql://$postgresHost:$postgresPort/$postgresDatabase?user=$postgresUser&password=$postgresPassword"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            // Equivalent to create user table if not exists; Based on the Users object
            SchemaUtils.create(Users)
        }
    }

    // Used to query the database
    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}