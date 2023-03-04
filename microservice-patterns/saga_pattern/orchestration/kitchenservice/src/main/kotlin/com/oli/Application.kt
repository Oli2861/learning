package com.oli

import com.oli.persistence.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.oli.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(useEmbeddedDatabase: Boolean = false) {
    DatabaseFactory.init(useEmbeddedDatabase)
    configureKoin()

    configureMonitoring()
    configureSerialization()
    configureRouting()
}
