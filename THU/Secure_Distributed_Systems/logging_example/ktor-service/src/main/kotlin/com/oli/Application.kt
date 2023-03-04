package com.oli

import com.oli.persistence.DatabaseFactory
import com.oli.plugins.*
import com.oli.user.userModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureKoin()

    configureSecurity()
    configureHTTP()
    configureRequestValidation()
    //configureMonitoring()
    configureSerialization()
    configureRouting()

    DatabaseFactory.init()

    userModule()

}
