package com.example.plugins

import com.example.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.http.content.static


fun Application.configureRouting() {
    routing {


        userRouting()
        issueRouting()
        milestoneRouting()
        projectRouting()
        subprojectRouting()
        taskRouting()
        teamMemberRouting()
        teamRouting()
        static("/static") {
            resources("files")
        }
    }
}

