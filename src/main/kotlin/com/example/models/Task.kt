package com.example.models

import org.jetbrains.exposed.sql.Table

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
    val teamMemberId = integer("team_member_id") references (TeamMembers.userId)
    val teamId = integer("team_id") references (TeamMembers.teamId)
    val joinDate = varchar("join_date",100) references (TeamMembers.joinDate)
    val title = varchar("title", 300)
    val status = varchar("statue", 100)
    val description = varchar("description", 300)
    val priority = varchar("priority", 500)

    override val primaryKey = PrimaryKey(id)



}