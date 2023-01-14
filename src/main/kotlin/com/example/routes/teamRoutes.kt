package com.example.routes


import com.example.data.dao.DAOFacade
import io.ktor.http.*
import io.ktor.server.application.*
import com.example.data.models.Team
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.teamRouting(dao: DAOFacade) {

        get("/teams") {
            val teams = dao.allTeams()
            call.respond(teams)
        }
        get("/teams/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid team ID")
            val team = dao.team(id)
            if (team == null) {
                call.respond(HttpStatusCode.NotFound, "Team not found")
            } else {
                call.respond(team)
            }
        }
        post("/teams") {
            val team = call.receive<Team>()
            val createdTeam = dao.addNewTeam(team.name)
            if (createdTeam == null) {
                call.respond(HttpStatusCode.BadRequest, "Error creating team")
            } else {
                call.respond(HttpStatusCode.Created, createdTeam)
            }
        }
        put("/teams/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid team ID")
            val team = call.receive<Team>()
            val success = dao.editTeam(id, team.name)
            if (success) {
                call.respond(HttpStatusCode.OK, "Team edited successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team not found")
            }
        }
        delete("/teams/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid team ID")
            val success = dao.deleteTeam(id)
            if (success) {
                call.respond(HttpStatusCode.OK, "Team deleted successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team not found")
            }
        }
}