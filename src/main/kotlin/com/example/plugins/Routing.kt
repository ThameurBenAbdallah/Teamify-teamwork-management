package com.example.plugins

import com.example.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.http.content.static


fun Application.configureRouting() {
    routing {


        articleRouting()
        static("/static") {
            resources("files")
        }
    }
}

