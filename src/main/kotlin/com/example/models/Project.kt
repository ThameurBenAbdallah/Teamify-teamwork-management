package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Project(val id: Int,
                   val title: String,
                   val startTime: String,
                   val dueTime: String,
                   val endTime: String,
                   val statue: String,
                   val description: String
                   )

object Projects : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val dueTime = varchar("body", 100)
    val startTime = varchar("start_time",100)
    val endTime = varchar("end_time",100)
    val statue = varchar("statue", 100)
    val description = varchar("description", 300)


    override val primaryKey = PrimaryKey(id)
}