package com.example.models

import com.example.models.Users.autoIncrement
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Team(val id: Int,
                val name: String,
                val leader: Int,
                val projects: List<Project>
)

object Teams : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val leader = integer("leader").references(Users.id)
    override val primaryKey = PrimaryKey(id)
}
object TeamsProjects : Table() {
    val teamId = integer("team_id").references(Teams.id)
    val projectId = integer("project_id").references(Projects.id)
    override val primaryKey = PrimaryKey(Teams.id)

}