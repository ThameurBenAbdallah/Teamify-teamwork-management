package com.example.routes

import com.example.data.auth.AuthRequest
import com.example.data.auth.AuthResponse
import com.example.data.dao.DAOFacade
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signIn(
    dao: DAOFacade,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveOrNull<AuthRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)



        val user =dao.findUserByEmail(request.email)
        if(user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect email")
            return@post
        }
        println("user password is : ${user.password}")
        println("user salt is : ${user.salt}")

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if(!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString(),
            ),
            TokenClaim(
                name = "role",
                value = user.role.name
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}