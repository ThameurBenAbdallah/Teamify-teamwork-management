package com.example.models

import com.example.models.Issues.autoIncrement
import com.example.models.TeamMembers
import org.jetbrains.exposed.sql.Table

data class Task(
    val id: Int,
    val milestone: Milestone,
    val teamMember: TeamMember,
    val startTime: String,
    val dueTime: String,
    val statue: String,
    val description: String
)

object Tasks : Table("tasks") {
    val id = integer("id").autoIncrement()
    val milestoneId = integer("milestone_id") references (Milestones.id)
    val teamMemberId = integer("team_member_id") references (TeamMembers.userId)
    val title = varchar("title", 300)
    val statue = varchar("statue", 100)
    val description = varchar("description", 300)

    override val primaryKey = PrimaryKey(id)


}