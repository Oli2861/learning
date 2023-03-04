package com.oli.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object UserRouteConstants {
    const val USER_ROUTE_PATH = "/user"
}

fun Route.userRouting() {
    val userService by inject<UserService>()

    route(UserRouteConstants.USER_ROUTE_PATH) {

        authenticate("auth-basic-hashed") {
            get {
                call.respond(userService.getAll())
            }
        }

        get("{id}") {
            val userId = call.parameters["id"] ?: return@get call.respondText("Missing userId", status = HttpStatusCode.BadRequest)

            val user = userService.getUser(userId)

            return@get if (user == null) {
                call.respondText("No user found for the provided userId", status = HttpStatusCode.NotFound)
            } else {
                call.respond(user)
            }
        }

        get("/notsanitized/{id}"){
            val userId = call.parameters["id"] ?: return@get call.respondText("Missing userId", status = HttpStatusCode.BadRequest)

            val user = userService.getUserLogEntryNotSanitized(userId)

            return@get if (user == null) {
                call.respondText("No user found for the provided userId", status = HttpStatusCode.NotFound)
            } else {
                call.respond(user)
            }
        }

        post {
            val user: User = call.receive()
            val createdUserName: String? = userService.createUser(user)

            return@post if (createdUserName == null) {
                call.respondText("Unable to create the user.", status = HttpStatusCode.Conflict)
            } else {
                call.respondText("Created user with username $createdUserName", status = HttpStatusCode.Created)
            }
        }

    }

}
