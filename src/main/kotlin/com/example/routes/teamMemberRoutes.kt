package com.example.routes


import io.ktor.http.*
import io.ktor.server.application.*


import com.example.dao.dao
import com.example.models.TeamMember

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.teamMemberRouting() {
    route("/team-members") {
        // GET /team-members - get all team members
        get {
            val teamMembers = dao.allTeamMembers()
            call.respond(teamMembers)
        }
        // POST /team-members - add a new team member
        post {
            val teamMember = call.receive<TeamMember>()
            dao.addNewTeamMember(
                teamMember.userId,
                teamMember.teamId,
                teamMember.role,
                teamMember.isTeamLeader,
                teamMember.joinDate,
                teamMember.leaveDate
            )?.let {
                call.respond(HttpStatusCode.Created, it)
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid team member")
        }
        // PUT /team-members/{userId}/{teamId} - update an existing team member
        put {
            val userId = call.parameters["userId"]?.toIntOrNull() ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Invalid user ID"
            )
            val teamId = call.parameters["teamId"]?.toIntOrNull() ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Invalid team ID"
            )
            val teamMember = call.receive<TeamMember>()
            if (dao.editTeamMember(
                    userId,
                    teamId,
                    teamMember.role,
                    teamMember.isTeamLeader,
                    teamMember.joinDate,
                    teamMember.leaveDate
                )
            ) {
                call.respond(HttpStatusCode.OK, "Team member updated")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team member not found")
            }
        }
        // DELETE /team-members/{userId}/{teamId}/{joinDate} - delete an existing team member
        delete {
            val userId = call.parameters["userId"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid user ID"
            )
            val teamId = call.parameters["teamId"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid team ID"
            )
            val joinDate = call.parameters["joinDate"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid team ID"
            )
            if (dao.deleteTeamMember(userId, teamId, joinDate)) {
                call.respond(HttpStatusCode.OK, "Team member deleted")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team member not found")
            }
        }
        // patch /team-members/{userId}/{teamId}/{joinDate}/ - have a team member leave the team

        patch("/{userId}/{teamId}/{joinDate}") {
            val userId = call.parameters["userId"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            val teamId = call.parameters["teamId"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest, "Invalid team ID")
            val joinDate = call.parameters["joinDate"] ?: return@patch call.respond(HttpStatusCode.BadRequest, "Invalid join date")
            val leaveDate = call.receive<String>()
            if (dao.leaveTeam(userId, teamId, joinDate, leaveDate)) {
                call.respond(HttpStatusCode.OK, "Team member has left the team")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team member not found")
            }
        }
    }

}