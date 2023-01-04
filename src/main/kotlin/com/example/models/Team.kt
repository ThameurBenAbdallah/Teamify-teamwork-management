package com.example.models
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Team(val id: Int,
                val name: String,
)

object Teams : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    override val primaryKey = PrimaryKey(id)
}
object TeamsProjects : Table() {
    val teamId = integer("team_id").references(Teams.id)
    val subprojectId = integer("project_id").references(Subprojects.id)
    override val primaryKey = PrimaryKey(teamId, subprojectId)

}

