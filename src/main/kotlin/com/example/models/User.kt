package com.example.models


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val password: String,
    val isTeamMember: Boolean,
    val isAdmin: Boolean,
    val isManager: Boolean
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("name", 128)
    val fullName = varchar("last_name", 300)
    val password = varchar("password", 300)
    val isTeamMember = bool("is_team_member")
    val isAdmin = bool("is_admin")
    val isManager = bool("is_manager")
    override val primaryKey = PrimaryKey(id)
}