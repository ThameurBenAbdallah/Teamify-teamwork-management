package com.example.plugins

import com.example.data.dao.DAOFacade
import com.example.routes.*
import com.example.security.hashing.HashingService
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.http.content.static


fun Application.configureRouting(
    hashingService: HashingService,
    dao: DAOFacade,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {


        userRouting(hashingService, dao)
        issueRouting(dao)
        milestoneRouting(dao)
        projectRouting(dao)
        subprojectRouting(dao)
        taskRouting(dao)
        teamMemberRouting(dao)
        teamRouting(dao)
        signUp(hashingService, dao)
        signIn(dao, hashingService, tokenService, tokenConfig)
        static("/static") {
            resources("files")
        }
    }
}

