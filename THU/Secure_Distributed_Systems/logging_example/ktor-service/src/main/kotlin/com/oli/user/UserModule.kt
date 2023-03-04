package com.oli.user

import com.oli.persistence.UserDAOImpl
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.ktor.plugin.koin

fun Application.userModule() {
    koin {
        modules(userKoinModule)
        configureUserRouting()
    }
}

fun Application.configureUserRouting() {
    routing {
        userRouting()
    }
}

private val userKoinModule = module {
    single<UserDAO> {
        UserDAOImpl().apply {
            runBlocking {
                // Add admin user if empty
                if (readAll().isEmpty()) {
                    create(User(0, "admin", "1234", "mail@mail.com"))
                }
            }
        }
    }
    single { UserService(userDAO = get(), logger = get()) }
}
