package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import com.example.models.Projects.statue
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {
    // Projects:
    private fun resultRowToProject(row: ResultRow) = Project(
        id = row[Articles.id],
        title = row[Articles.title],
        dueTime = row[Projects.dueTime],
        endTime = row[Projects.endTime],
        startTime = row[Projects.startTime],
        statue = row[Projects.statue],
        description = row[Projects.description]
    )

    override suspend fun allProjects(): List<Project> = dbQuery {
        Projects.selectAll().map(::resultRowToProject)

    }

    override suspend fun project(id: Int): Project? = dbQuery {
        Projects
            .select { Projects.id eq id }
            .map(::resultRowToProject)
            .singleOrNull()
    }

    override suspend fun addNewProject(
        title: String,
        dueTime: String,
        endTime: String,
        startTime: String,
        statue: String,
        description: String
    ): Project? = dbQuery {
        val insertStatement = Projects.insert {
            it[Projects.title] = title
            it[Projects.dueTime] = dueTime
            it[Projects.endTime] = endTime
            it[Projects.startTime] = startTime
            it[Projects.statue] = statue
            it[Projects.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProject)
    }

    override suspend fun editProject(
        id: Int,
        title: String,
        dueTime: String,
        endTime: String,
        startTime: String,
        statue: String,
        description: String
    ): Boolean = dbQuery {
        Projects.update({ Projects.id eq id }) {
            it[Projects.title] = title
            it[Projects.dueTime] = dueTime
            it[Projects.startTime] = startTime
            it[Projects.endTime] = endTime
            it[Projects.statue] = statue
            it[Projects.description] = description
        } > 0
    }

    override suspend fun deleteProject(id: Int): Boolean = dbQuery {
        Projects.deleteWhere { Articles.id eq id } > 0
    }

    override suspend fun allMilestones(): List<Milestone> {
        TODO("Not yet implemented")
    }

    override suspend fun milestone(id: Int): Milestone? {
        TODO("Not yet implemented")
    }

    override suspend fun addNewMilestone(
        title: String,
        startTime: String,
        dueTime: String,
        subprojectId: Int,
        description: String
    ): Milestone? {
        TODO("Not yet implemented")
    }

    override suspend fun editMilestone(
        id: Int,
        title: String,
        startTime: String,
        dueTime: String,
        description: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMilestone(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun allUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun user(id: Int): User? {
        TODO("Not yet implemented")
    }

    override suspend fun addNewUser(
        email: String,
        fullName: String,
        password: String,
        isTeamMember: Boolean,
        isAdmin: Boolean
    ): User? {
        TODO("Not yet implemented")
    }

    override suspend fun editUser(
        id: Int,
        email: String,
        fullName: String,
        password: String,
        isTeamMember: Boolean,
        isAdmin: Boolean
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    //Subproject:

    private fun resultRowToSubproject(row: ResultRow) = Subproject(
        id = row[Articles.id],
        title = row[Articles.title],
        endTime = row[Subprojects.endTime],
        startTime = row[Subprojects.startTime],
        statue = row[Subprojects.statue],
        projectId = row[Subprojects.projectId]

    )

    override suspend fun allSubprojects(): List<Subproject> = dbQuery {
        Subprojects.selectAll().map(::resultRowToSubproject)

    }

    override suspend fun subproject(id: Int): Subproject? = dbQuery {
        Subprojects
            .select { Subprojects.id eq id }
            .map(::resultRowToSubproject)
            .singleOrNull()
    }

    override suspend fun addNewSubproject(
        title: String,
        endTime: String,
        startTime: String,
        statue: String,
        projectId: Int
    ): Subproject? = dbQuery {
        val insertStatement = Subprojects.insert {
            it[Subprojects.title] = title
            it[Subprojects.endTime] = endTime
            it[Subprojects.startTime] = startTime
            it[Subprojects.statue] = statue
            it[Subprojects.projectId] = projectId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToSubproject)
    }

    override suspend fun editSubproject(id: Int, title: String, dueTime: String): Boolean = dbQuery {
        Subprojects.update({ Subprojects.id eq id }) {
            it[Subprojects.title] = title
            it[Subprojects.startTime] = startTime
            it[Subprojects.endTime] = endTime
            it[Subprojects.statue] = statue
        } > 0
    }

    override suspend fun deleteSubproject(id: Int): Boolean = dbQuery {
        Subprojects.deleteWhere { Articles.id eq id } > 0
    }

    override suspend fun allTeams(): List<Team> {
        TODO("Not yet implemented")
    }

    override suspend fun team(id: Int): Team? {
        TODO("Not yet implemented")
    }

    override suspend fun addNewTeam(name: String, leader: Int): Team? {
        TODO("Not yet implemented")
    }

    override suspend fun editTeam(id: Int, title: String, body: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTeam(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    //Milestones:


    val dao: DAOFacade = DAOFacadeImpl().apply {
        runBlocking {

            }
        }


}