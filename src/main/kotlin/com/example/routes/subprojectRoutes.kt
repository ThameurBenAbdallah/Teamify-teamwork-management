package com.example.routes

import com.example.data.dao.dao
import com.example.data.models.Subproject
import io.ktor.http.*
import io.ktor.server.application.*

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.subprojectRouting() {
    // Récupère tous les sous-projets
    get("/subprojects") {
        val subprojects = dao.allSubprojects()
        call.respond(subprojects)
    }

    // Récupère un sous-projet par son ID
    get("/subprojects/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid subproject ID")
        val subproject = dao.subproject(id)
        if (subproject == null) call.respond(HttpStatusCode.NotFound, "Subproject not found")
        else call.respond(subproject)
    }

    // Ajoute un nouveau sous-projet
    post("/subprojects") {
        val subproject = call.receive<Subproject>()
        val newSubproject = dao.addNewSubproject(subproject.title, subproject.projectId, subproject.endTime)
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid subproject data")
        call.respond(newSubproject)
    }

    // Modifie un sous-projet
    put("/subprojects/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid subproject ID")
        val subproject = call.receive<Subproject>()
        if (!dao.editSubproject(id, subproject.title, subproject.endTime)) {
            return@put call.respond(HttpStatusCode.NotFound, "Subproject not found")
        }
        call.respond(HttpStatusCode.OK, "Subproject updated")
    }

    // Supprime un sous-projet
    delete("/subprojects/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid subproject ID")
        if (!dao.deleteSubproject(id)) {
            return@delete call.respond(HttpStatusCode.NotFound, "Subproject not found")
        }
        call.respond(HttpStatusCode.OK, "Subproject deleted")
    }
}