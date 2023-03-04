package com.oli.user

import org.jetbrains.exposed.sql.Table

@kotlinx.serialization.Serializable
data class User(
    val id: Int? = null,
    val userName: String,
    val password: String,
    val email: String
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val userName = varchar("username", 128)
    val password = varchar("password", 128)
    val email = varchar("email", 128)

    override val primaryKey = PrimaryKey(id)
}