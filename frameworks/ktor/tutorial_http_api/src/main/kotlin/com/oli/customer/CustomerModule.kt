package com.oli.customer

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.routing.*


fun Application.customerModule() {
    configureCustomerRouting()
}

fun Application.configureCustomerRouting() {
    // Routing is a plugin used to associate requests with the correct handler (application logic)
    // Before requests are send to handlers, they go through one or more plugins
    // Before the response is returned, they go through one or more plugins again
    routing {
        // Plugins can be installed to specific routes or the whole application:
        this.install(CachingHeaders) {
            options { call, outgoingContent ->
                when (outgoingContent.contentType?.withoutParameters()) {
                    ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
                    else -> null
                }
            }
        }
        // Register customer routes
        customerRouting()
    }
}
