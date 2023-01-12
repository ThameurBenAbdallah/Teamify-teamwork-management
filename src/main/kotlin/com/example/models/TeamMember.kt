package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
@Serializable
data class TeamMember(
    val id: Int,
    val userId: Int,
    val teamId: Int,
    val role: String,
    val isTeamLeader: Boolean,
    val joinDate: String,
    val leaveDate: String
)

object TeamMembers : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val teamId = integer("team_id").references(Teams.id)
    val role = varchar("role", 100)
    val isTeamLeader = bool("is_team_leader")
    val leaveDate = varchar("leave_date",100)
    val joinDate = varchar("join_date",100)
    init {
        uniqueIndex(userId, teamId, joinDate)
    }
        override val primaryKey = PrimaryKey(id)

}