package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Issue(
    val id: Int,
    val taskId: Int,
    val title: String,
    val description: String,
    val status: String,
    val priority: String,
    val assigneeId: Int
)
object Issues : Table() {
    val id = integer("id").autoIncrement()
    val taskId = integer("task_id").references(Tasks.id)
    val title = varchar("title", 128)
    val description = text("description")
    val status = varchar("status", 128)
    val priority = varchar("priority", 128)
    val assigneeId = integer("assignee_id").references(Users.id)

    override val primaryKey = PrimaryKey(id)
}




