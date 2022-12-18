package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Subproject(
    val id: Int,
    val title: String,
    val startTime: String,
    val endTime: String,
    val projectId: Int,
    val statue: String
)

object Subprojects : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val projectId = integer("project_id").references(Projects.id)
    val startTime = varchar("start_time",100)
    val endTime = varchar("end_time",100)
    val statue = varchar("statue",100)

    override val primaryKey = PrimaryKey(id)
}