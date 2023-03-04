package com.oli

import com.oli.event.MessageReceiver
import com.oli.persistence.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.oli.plugins.*
import org.slf4j.LoggerFactory

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(isTest: Boolean = false) {
    DatabaseFactory.init(false)
    configureKoin()

    configureSerialization()
    configureMonitoring()
    MessageReceiver.init()

}
