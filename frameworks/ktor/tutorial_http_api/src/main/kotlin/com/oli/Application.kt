package com.oli

import com.oli.customer.customerModule
import com.oli.plugins.configureRouting
import com.oli.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    // Configuration either via code or using HOCON / YAML files. In this case code:
    // Configuration of the netty engine. See docs for available options: https://api.ktor.io/ktor-server/ktor-server-netty/io.ktor.server.netty/-netty-application-engine/-configuration/index.html
    embeddedServer(
        factory = Netty,
        port = 8081,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    // Plugins: Provide functionality, that is not application logic. e.g. encoding, compression,..
    configureSerialization()
    configureRouting()
    // Logging on JVM using a SLF4J Logger retrieved from the ApplicationEnvironment
    log.info("Hello from Application")
    // Load itemModule
    customerModule()
}