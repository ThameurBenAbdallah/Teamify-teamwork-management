package com.example.routes

import com.example.data.dao.DAOFacade
import io.ktor.server.routing.*

import com.example.data.models.Task
import io.ktor.http.*
import io.ktor.server.application.*

import io.ktor.server.request.*
import io.ktor.server.response.*


fun Route.taskRouting(dao: DAOFacade) {
    // GET /tasks : List all tasks

    get {
        val tasks = dao.allTasks()
        call.respond(tasks)
    }
    get("/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val task = dao.task(id)
            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
        }
    }

    // POST /tasks : Add new task
    post {
        val task = call.receive<Task>()
        val newTask = dao.addNewTask(
            task.milestone.id,
            task.teamMember.userId,
            task.teamMember.teamId,
            task.teamMember.joinDate,
            task.startTime,
            task.dueTime,
            task.endTime,
            task.description,
            task.priority,
            task.status
        )
        if (newTask != null) {
            call.respond(HttpStatusCode.Created, newTask)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create task")
        }
    }
    //Delete /task{id} : Delete a task
    delete("/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        if (dao.deleteTask(id)) {
            call.respondText("Task removed correctly", status = HttpStatusCode.Accepted)
        }
        else {
            call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
    }


}