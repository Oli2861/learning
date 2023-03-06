package com.oli

import com.oli.address.configureAddressRouting
import com.oli.customer.customerModule
import com.oli.event.MessageReceiver
import com.oli.persistence.DatabaseFactory
import com.oli.plugins.configureMonitoring
import com.oli.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

/**
 * Base module of the application. Configures all submodules.
 * @param isTest Whether the application is being tested. This is relevant, as the DatabaseFactory will use an embedded H2 in memory database instead of a postgres.
 */
fun Application.module(isTest: Boolean = false) {
    DatabaseFactory.init(isTest)
    configureKoin()

    configureMonitoring()
    configureSerialization()

    customerModule()
    configureAddressRouting()
    MessageReceiver.init()
}
