package com.example.routes


import com.example.data.dao.DAOFacade
import io.ktor.http.*
import io.ktor.server.application.*

import com.example.data.models.Project

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.projectRouting(dao:DAOFacade) {

    get("/projects") {
        val projects = dao.allProjects()
        call.respond(projects)
    }

    // Define a GET route to retrieve a specific project by ID
    get("/projects/{id}") {
        // Get the project ID from the path parameters
        val id = call.parameters["id"]?.toIntOrNull()
            ?: // The id path parameter is not present or is not a valid integer
            return@get call.respond(HttpStatusCode.BadRequest, "Invalid project ID")

        val project = dao.project(id)
        if (project == null) {
            call.respond(HttpStatusCode.NotFound, "Project not found")
        } else {
            call.respond(project)
        }
    }

    // Define a POST route to create a new project
    post("/projects") {
        // Parse the request body as a Project object
        val project = call.receive<Project>()

        val newProject = dao.addNewProject(project.title, project.endTime, project.startTime, project.description)
        if (newProject == null) {
            call.respond(HttpStatusCode.BadRequest, "Error adding new project")
        } else {
            call.respond(newProject)
        }
    }
    put("/projects/{id}") {
        // Get the project ID from the path parameters
        val id = call.parameters["id"]?.toIntOrNull()

        if (id == null) {
            // The id path parameter is not present or is not a valid integer
            call.respond(HttpStatusCode.BadRequest, "Invalid project ID")
            return@put
        }

        // Parse the request body as a Project object
        val project = call.receive<Project>()

        val success = dao.editProject(id, project.title, project.endTime, project.startTime, project.description)
        if (success) {
            call.respond(HttpStatusCode.OK, "Project updated successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Project not found")
        }


    }
}