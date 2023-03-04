package com.oli.user

import com.oli.utility.sanitizeLogEntry
import org.apache.commons.text.StringEscapeUtils
import org.slf4j.Logger

class UserService(
    private val userDAO: UserDAO,
    private val logger: Logger
) {

    suspend fun createUser(user: User): String? {
        val createdUser = userDAO.create(user)
        logger.info("Created user $user")
        return createdUser?.userName
    }

    suspend fun getUserLogEntryNotSanitized(id: String): User? {
        return try {
            // Try to parse the string into an integer. Throws a number if it is not a number.
            val userID = id.toInt()
            val user = userDAO.read(userID)
            user
        } catch (e: NumberFormatException) {
            // Log, that the provided string was not a number.
            // Vulnerable to Carriage Return Line Feed (CRLF) attacks,
            // since the log message is not sanitized.
            logger.error("Failed to parse number: $id")
            null
        }
    }

    suspend fun getUser(id: String): User? {
        return try {
            val userID = id.toInt()
            val user = userDAO.read(userID)
            user
        } catch (e: NumberFormatException) {
            logger.error("Failed to parse number: ${sanitizeLogEntry(id)}")
            null
        }
    }

    suspend fun getAll(): List<User> = userDAO.readAll()

    suspend fun updateUser(user: User): Boolean = userDAO.update(user)

    suspend fun deleteUser(userId: Int): Boolean = userDAO.delete(userId)


}
/*
fun main() {
    val str = "test \n test"
    val escaped = StringEscapeUtils.escapeJava(str)
    println(escaped)
}
*/