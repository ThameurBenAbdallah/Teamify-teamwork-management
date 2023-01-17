package com.example.routes

import com.example.data.auth.Role
import com.example.data.dao.DAOFacade
import com.example.data.auth.SignUpRequest
import com.example.security.hashing.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    dao: DAOFacade
) {
    post("signup") {
        val request = call.receiveOrNull<SignUpRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val user = dao.addNewUser(
            email = request.email,
            fullName = request.fullName,
            password = request.password,
            role = Role.ADMIN,
            hashingService = hashingService
        )?: return@post call.respond(HttpStatusCode.BadRequest, "Error creating new Admin")
        call.respond(HttpStatusCode.Created,"Admin created correctly, you can now create users")


    }
}
