package com.example.routes



import io.ktor.http.*
import io.ktor.server.application.*


import com.example.data.dao.dao
import com.example.data.models.Milestone

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.milestoneRouting() {

    get("/milestones") {
        val milestones = dao.allMilestones()
        call.respond(milestones)
    }

    // Define a GET route to retrieve a specific milestone by ID
    get("/milestones/{id}") {
        // Get the milestone ID from the path parameters
        val id = call.parameters["id"]?.toIntOrNull()
            ?: // The id path parameter is not present or is not a valid integer
            return@get call.respond(HttpStatusCode.BadRequest, "Invalid milestone ID")

        val milestone = dao.milestone(id)
        if (milestone == null) {
            call.respond(HttpStatusCode.NotFound, "Milestone not found")
        } else {
            call.respond(milestone)
        }
    }
    // Define a POST route to create a new milestone
    post("/milestones") {
        // Parse the request body as a Milestone object
        val milestone = call.receive<Milestone>()

        val newMilestone = dao.addNewMilestone(milestone.title, milestone.startTime, milestone.endTime, milestone.dueTime, milestone.subprojectId, milestone.description)
        if (newMilestone == null) {
            call.respond(HttpStatusCode.BadRequest, "Error adding new milestone")
        } else {
            call.respond(newMilestone)
        }
    }
    // Define a PUT route to update an existing milestone
    put("/milestones/{id}") {
        // Get the milestone ID from the path parameters
        val id = call.parameters["id"]?.toIntOrNull()
            ?: // The id path parameter is not present or is not a valid integer
            return@put call.respond(HttpStatusCode.BadRequest, "Invalid milestone ID")

        // Parse the request body as a Milestone object
        val milestone = call.receive<Milestone>()

        val success = dao.editMilestone(id, milestone.title, milestone.startTime, milestone.endTime, milestone.dueTime, milestone.subprojectId, milestone.description)
        if (success) {
            call.respond(HttpStatusCode.OK, "Milestone updated successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Milestone not found")
        }
    }
    // Define a DELETE route to delete a milestone
    delete("/milestones/{id}") {
        // Get the milestone ID from the path parameters
        val id = call.parameters["id"]?.toIntOrNull()?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid milestone ID")



        val success = dao.deleteMilestone(id)
        if (success) call.respond(HttpStatusCode.OK, "Milestone deleted successfully") else call.respond(HttpStatusCode.NotFound, "Milestone not found")
    }
}






