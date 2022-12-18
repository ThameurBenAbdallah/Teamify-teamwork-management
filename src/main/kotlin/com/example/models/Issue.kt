package com.example.models

import org.jetbrains.exposed.sql.Table

data class Issue(
    val id: Int,
    val title: String,
    val taskId: Int,
    val teamMembers: List<TeamMember>,
    val description: String
)

object Issues : Table() {
    val id = integer("id").autoIncrement()
    val taskId = integer("milestone_id") references (Tasks.id)
    val title = varchar("title", 300)
    val description = varchar("description", 500)
    override val primaryKey = PrimaryKey(id)

}
