package com.example.routes


import io.ktor.http.*
import io.ktor.server.application.*


import com.example.data.dao.dao
import com.example.data.models.TeamMember

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
        // PUT /team-members/{id} - update an existing team member
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Invalid team member ID"
            )

            val teamMember = call.receive<TeamMember>()
            if (dao.editTeamMember(
                    id,
                    teamMember.userId,
                    teamMember.teamId,
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
        // DELETE /team-members/{id} - delete an existing team member
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid team member ID"

            )
            if (dao.deleteTeamMember(id)) {
                call.respond(HttpStatusCode.OK, "Team member deleted")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team member not found")
            }
        }
        // patch /team-members/{userId}/{teamId}/{joinDate}/ - have a team member leave the team

        patch("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest, "Invalid team member ID")
            val leaveDate = call.receive<String>()
            if (dao.leaveTeam(id, leaveDate)) {
                call.respond(HttpStatusCode.OK, "Team member has left the team")
            } else {
                call.respond(HttpStatusCode.NotFound, "Team member not found")
            }
        }
    }

}