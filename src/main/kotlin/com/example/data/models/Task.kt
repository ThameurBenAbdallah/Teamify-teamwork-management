package com.example.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


@Serializable
data class Task(
    val id: Int,
    val milestone: Milestone,
    val teamMember: TeamMember,
    val startTime: String,
    val dueTime: String,
    val endTime: String,
    val description: String,
    val priority: String,
    val status: String
)

object Tasks : Table("tasks") {
    val id = integer("id").autoIncrement()
    val startTime = varchar("startTime", 100)
    val dueTime = varchar("due_time", 100)
    val endTime = varchar("end_time", 100)
    val milestoneId = integer("milestone_id") references (Milestones.id)
    val teamMemberId = integer("team_member_id") references (TeamMembers.id)
    val title = varchar("title", 300)
    val status = varchar("status", 100)
    val description = varchar("description", 300)
    val priority = varchar("priority", 500)

    override val primaryKey = PrimaryKey(id)



}