package com.example.routes

import com.auth0.jwt.JWT
import com.example.data.auth.Role
import com.example.data.dao.DAOFacade
import io.ktor.http.*
import io.ktor.server.application.*


import com.example.data.models.User
import com.example.security.hashing.HashingService
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Route.userRouting(
    hashingService: HashingService, dao: DAOFacade
) {

    authenticate {


        route("/user") {

            get {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ") //get the token from the header

                 val decodedToken = JWT.decode(token)
                 val role = decodedToken.getClaim("role").asString()//extract the role from the claim

                 if (role != Role.MANAGER.name) {
                     call.respond(HttpStatusCode.Conflict, "Only Admin is Authorised")
                 }

                val allUsers = dao.allUsers()
                if (allUsers.isNotEmpty()) {
                    call.respond(allUsers)
                } else {
                    call.respondText("No users found", status = HttpStatusCode.OK)
                }

            }
            get("/find{id?}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )

                val user = dao.user(id) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    "No user with id $id"
                )
                call.respond(user)

            }

            post {

                val user = call.receive<User>()

                dao.addNewUser(
                    email = user.email,
                    fullName = user.fullName,
                    password = user.password,
                    role = user.role,
                    hashingService = hashingService
                ) ?: return@post call.respond(HttpStatusCode.BadRequest, "Error adding new user")
                call.respondText("User added correctly", status = HttpStatusCode.Created)

            }
            delete("{id?}") {
                val id =
                    call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (dao.deleteUser(id)) {
                    call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
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
                        role = user.role
                    )
                )
                    call.respondText("User edited correctly", status = HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound, "User not found")

            }
        }


    }
}










