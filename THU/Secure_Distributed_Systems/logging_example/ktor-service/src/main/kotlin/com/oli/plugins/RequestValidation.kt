package com.oli.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<String> {
            if (it.isNotBlank()) ValidationResult.Valid
            else ValidationResult.Invalid("")
        }
    }
}