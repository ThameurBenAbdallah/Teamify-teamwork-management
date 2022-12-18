package com.example.routes

import com.example.models.Article

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.articleRouting() {

        get("/") {
            call.respondRedirect("articles")
        }
        route("articles") {
            get {

            }
            get("new") {
                // Show a page with fields for creating a new article
                call.respond(FreeMarkerContent("new.ftl", model = null))
            }
            post {

            }
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()

            }
            get("{id}/edit") {
                val id = call.parameters.getOrFail<Int>("id").toInt()

            }
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        val title = formParameters.getOrFail("title")
                        val body = formParameters.getOrFail("body")

                        call.respondRedirect("/articles/$id")
                    }

                    "delete" -> {

                        call.respondRedirect("/articles")
                    }
                }
            }
        }

}