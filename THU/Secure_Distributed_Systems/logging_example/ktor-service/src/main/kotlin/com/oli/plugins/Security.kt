package com.oli.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
import org.slf4j.Logger
import java.time.Instant

fun Application.configureSecurity() {
    val logger by inject<Logger>()

    // SHA-256 hash function
    val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
    // In memory table used to store SHA-256 hashed usernames and passwords
    val hashedUserTable = UserHashedTableAuth(
        table = mapOf(
            "Harry" to digestFunction("1234"),
            "jetbrains" to digestFunction("foobar"),
            "admin" to digestFunction("password")
        ),
        digester = digestFunction
    )

    val failedLoginAttempts: MutableMap<String, MutableList<Instant>> = mutableMapOf()

    // Installing basing authentication
    install(Authentication) {
        // Basic auth configuration
        basic("auth-basic-hashed") {
            // Realm = Protection space, allows the partitioning into different protection spaces https://datatracker.ietf.org/doc/html/rfc2617#page-3
            realm = "/"
            validate { userPasswordCredential ->

                val userName = userPasswordCredential.name
                // Forget about attempts older than 5 minutes.
                failedLoginAttempts[userPasswordCredential.name]?.removeAll { it.isBefore(Instant.now().minusSeconds(600)) }
                // Only allow a login attempt if the user did not try to log in more than 5 times in the past 5 minutes.
                if ((failedLoginAttempts[userName]?.size ?: 0) >= 5){
                    return@validate null
                }

                // Principal: Authenticated entity or null if unsuccessful
                val userIdPrincipal: UserIdPrincipal? = hashedUserTable.authenticate(userPasswordCredential)

                // Log, whether authentication was successful
                if (userIdPrincipal == null) {
                    // Do not log sensitive information such as passwords.
                    logger.info("Login attempt failed for user: $userName")
                    // TODO: Store failed login attempts in a database.

                    if (failedLoginAttempts.keys.contains(userName)) {
                        failedLoginAttempts[userName]!!.add(Instant.now())
                    } else {
                        failedLoginAttempts[userName] = mutableListOf(Instant.now())
                    }

                } else {
                    logger.info("Login attempt successful for user: $userName")
                }

                userIdPrincipal
            }
        }
    }
}
