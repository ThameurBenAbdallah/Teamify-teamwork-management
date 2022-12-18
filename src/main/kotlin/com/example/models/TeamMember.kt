package com.example.models

import org.jetbrains.exposed.sql.Table

data class TeamMember(
    val userId: Int,
    val teamId: Int,
    val role: String,
    val isTeamLeader: Boolean,
)

object TeamMembers : Table() {

    val userId = integer("id").references(Users.id)
    val teamId = integer("team_id").references(Teams.id)
    val role = varchar("role", 100)
    val isTeamLeader = bool("is_team_leader")
    override val primaryKey = PrimaryKey(userId)
}