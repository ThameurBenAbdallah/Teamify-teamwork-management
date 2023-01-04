package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*


import com.example.dao.dao

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.userRouting() {
    data class UserRequest(
        val email: String,
        val fullName: String,
        val password: String,
        val isTeamMember: Boolean,
        val isAdmin: Boolean,
        val isManager: Boolean
    )
    route("/user") {
        get {
            val allUsers = dao.allUsers()
            if (allUsers.isNotEmpty()) {
                call.respond(allUsers)
            } else {
                call.respondText("No users found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val user =
                dao.user(id) ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(user)

        }
        post {

            val userRequest = call.receive<UserRequest>()

            dao.addNewUser(
                email = userRequest.email,
                fullName = userRequest.fullName,
                password = userRequest.password,
                isTeamMember = userRequest.isTeamMember,
                isAdmin = userRequest.isAdmin,
                isManager = userRequest.isManager
            )
            call.respondText("User added correctly", status = HttpStatusCode.Created)

        }
        delete("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (dao.deleteUser(id)) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }

        }
        put("{id?}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val userRequest = call.receive<UserRequest>()

            if (dao.editUser(
                id = id,
                email = userRequest.email,
                fullName = userRequest.fullName,
                password = userRequest.password,
                isTeamMember = userRequest.isTeamMember,
                isAdmin = userRequest.isAdmin,
                isManager = userRequest.isManager
            )
            )
            call.respondText("User edited correctly", status = HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound, "User not found")

        }
    }


}
