package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Milestone(
    val id: Int,
    val title: String,
    val startTime: String,
    val dueTime: String,
    val subprojectId: Int,
    val description: String,
)

object Milestones : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val subprojectId = integer("project_id").references(Projects.id)
    val startTime = varchar("start_time",100)
    val endTime = varchar("end_time",100)
    val statue = varchar("statue", 100)
    val description = varchar("description", 300)


    override val primaryKey = PrimaryKey(id)
}

