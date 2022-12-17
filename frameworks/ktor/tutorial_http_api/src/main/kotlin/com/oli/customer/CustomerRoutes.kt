package com.oli.customer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val customerStorage: MutableSet<Customer> = mutableSetOf()
fun Route.customerRouting() {

    route("/customer") {

        get {
            if (customerStorage.isNotEmpty()) call.respond(customerStorage)
            else call.respondText("No customers found", status = HttpStatusCode.OK)

        }

        get("{id?}") {
            // Get path variable & handling missing ones
            val customerId =
                call.parameters["id"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)
            // Search customer & Respond if missing
            when (val customer = customerStorage.find { it.id == customerId }) {
                null -> return@get call.respondText("Customer not found", status = HttpStatusCode.NotFound)
                else -> call.respond(customer)
            }
        }

        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText("Customer stored", status = HttpStatusCode.Created)

        }

        delete("{id?") {
            val customerId =
                call.parameters["id"] ?: return@delete call.respondText("Missing id", status = HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == customerId }) {
                call.respondText("Removed customer with id $customerId", status = HttpStatusCode.Accepted)
            } else {
               call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}