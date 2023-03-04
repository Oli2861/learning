package com.oli.persistence

import com.oli.user.User
import com.oli.user.UserDAO
import com.oli.user.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDAOImpl : UserDAO {
    private fun resultRowToUser(row: ResultRow): User = User(
        id = row[Users.id],
        userName = row[Users.userName],
        password = row[Users.password],
        email = row[Users.email]
    )

    override suspend fun create(user: User): User? = DatabaseFactory.dbQuery {
        val insertStatement = Users.insert {
            it[userName] = user.userName
            it[password] = user.password
            it[email] = user.email
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun readAll(): List<User> = DatabaseFactory.dbQuery {
        Users.selectAll()
            .map { resultRowToUser(it) }
    }

    override suspend fun read(id: Int): User? = DatabaseFactory.dbQuery {
        Users.select { Users.id eq id }
            .map { resultRowToUser(it) }
            .singleOrNull()
    }

    override suspend fun update(user: User): Boolean {
        return if (user.id == null) false
        else DatabaseFactory.dbQuery {
            Users.update({ Users.id eq user.id }) {
                it[userName] = user.userName
                it[password] = user.password
                it[email] = user.email
            } > 0
        }
    }

    override suspend fun delete(id: Int): Boolean = DatabaseFactory.dbQuery {
        Users.deleteWhere { this.id eq id } > 0
    }
}