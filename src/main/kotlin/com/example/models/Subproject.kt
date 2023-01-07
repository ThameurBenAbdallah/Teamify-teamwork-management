package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Subproject(
    val id: Int,
    val title: String,
    val endTime: String,
    val projectId: Int,
)

object Subprojects : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val endTime = varchar("end_time",100)
    val projectId = integer("project_id").references(Projects.id)


    override val primaryKey = PrimaryKey(id)
}