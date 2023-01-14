package com.example

import com.example.data.dao.DAOFacade
import com.example.data.dao.DAOFacadeImpl
import com.example.data.dao.DatabaseFactory
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.security.hashing.SHA256HashingService
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseFactory.init()
    val dao: DAOFacade = DAOFacadeImpl()
    val hashingService = SHA256HashingService()
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )


    configureSecurity(tokenConfig)
    configureRouting(hashingService, dao,tokenService,tokenConfig)
    configureSerialization()

}
