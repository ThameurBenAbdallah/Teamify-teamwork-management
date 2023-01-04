package com.example.routes

import io.ktor.server.routing.*
import com.example.dao.dao
import com.example.models.Issue
import io.ktor.http.*
import io.ktor.server.application.*

import io.ktor.server.request.*
import io.ktor.server.response.*


fun Route.issueRouting() {
    // GET /tasks : List all tasks

    get {
        val issues = dao.allIssues()
        call.respond(issues)
    }
    get("/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid issue ID")

        val issue = dao.issue(id) ?: call.respond(HttpStatusCode.NotFound, "Issue not found")

        call.respond(issue)
    }

    // POST /Issues : Add new Issue
    post {
        val issue = call.receive<Issue>()
        val newIssue = dao.addNewIssue(
            issue.taskId,
            issue.title,
            issue.description,
            issue.status,
            issue.priority,
            issue.assigneeId

        )
        if (newIssue != null) {
            call.respond(HttpStatusCode.Created, newIssue)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create issue")
        }
    }
    //Delete /issue{id} : Delete an issue
    delete("/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        if (dao.deleteIssue(id)) {
            call.respondText("Issue removed correctly", status = HttpStatusCode.Accepted)
        }
        else {
            call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
    }


}