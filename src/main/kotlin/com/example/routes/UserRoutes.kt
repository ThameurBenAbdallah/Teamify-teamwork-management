package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*


import com.example.data.dao.dao
import com.example.data.models.User
import com.example.security.hashing.HashingService
import io.ktor.server.auth.*

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.userRouting(
    hashingService: HashingService,
) {
    authenticate {

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
                call.receive<User>()

            }
            post {

                val user = call.receive<User>()

                val newUser = dao.addNewUser(
                    email = user.email,
                    fullName = user.fullName,
                    password = user.password,
                    isTeamMember = user.isTeamMember,
                    isAdmin = user.isAdmin,
                    isManager = user.isManager,
                    hashingService = hashingService
                ) ?: return@post call.respond(HttpStatusCode.BadRequest, "Error adding new user")
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
                val user = call.receive<User>()

                if (dao.editUser(
                        id = id,
                        email = user.email,
                        fullName = user.fullName,
                        isTeamMember = user.isTeamMember,
                        isAdmin = user.isAdmin,
                        isManager = user.isManager
                    )
                )
                    call.respondText("User edited correctly", status = HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound, "User not found")

            }
        }


    }
}
