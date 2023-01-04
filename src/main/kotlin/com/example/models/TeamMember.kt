package com.example.models

import org.jetbrains.exposed.sql.Table

data class TeamMember(
    val userId: Int,
    val teamId: Int,
    val role: String,
    val isTeamLeader: Boolean,
    val joinDate: String,
    val leaveDate: String
)

object TeamMembers : Table() {

    val userId = integer("user_id").references(Users.id).uniqueIndex()
    val teamId = integer("team_id").references(Teams.id)
    val role = varchar("role", 100)
    val isTeamLeader = bool("is_team_leader")
    val leaveDate = varchar("leave_date",100)
    val joinDate = varchar("join_date",100)
    override val primaryKey = PrimaryKey(userId,teamId, joinDate)
}