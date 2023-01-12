package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import com.example.security.hashing.HashingService
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {

    // Projects------------------------------------------------------------------------------------------
    private fun resultRowToProject(row: ResultRow) = Project(
        id = row[Projects.id],
        title = row[Projects.title],
        endTime = row[Projects.endTime],
        startTime = row[Projects.startTime],
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
        endTime: String,
        startTime: String,
        description: String
    ): Project? = dbQuery {
        val insertStatement = Projects.insert {
            it[Projects.title] = title
            it[Projects.endTime] = endTime
            it[Projects.startTime] = startTime
            it[Projects.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProject)
    }

    override suspend fun editProject(
        id: Int,
        title: String,
        endTime: String,
        startTime: String,
        description: String
    ): Boolean = dbQuery {
        Projects.update({ Projects.id eq id }) {
            it[Projects.title] = title
            it[Projects.startTime] = startTime
            it[Projects.endTime] = endTime
            it[Projects.description] = description
        } > 0
    }

    override suspend fun deleteProject(id: Int): Boolean = dbQuery {
        Projects.deleteWhere { Projects.id eq id } > 0
    }

    //Milestones ----------------------------------------------------------------------------------------
    private fun resultRowToMilestone(row: ResultRow) = Milestone(
        id = row[Milestones.id],
        title = row[Milestones.title],
        subprojectId = row[Milestones.subprojectId],
        startTime = row[Milestones.startTime],
        endTime = row[Milestones.endTime],
        dueTime = row[Milestones.dueTime],
        description = row[Projects.description]
    )

    override suspend fun allMilestones(): List<Milestone> = dbQuery {
        Milestones.selectAll().map(::resultRowToMilestone)
    }

    override suspend fun milestone(id: Int): Milestone? = dbQuery {
        Milestones
            .select { Milestones.id eq id }
            .map(::resultRowToMilestone)
            .singleOrNull()
    }

    override suspend fun addNewMilestone(
        title: String,
        startTime: String,
        endTime: String,
        dueTime: String,
        subprojectId: Int,
        description: String
    ): Milestone? = dbQuery {
        val insertStatement = Milestones.insert {
            it[Milestones.title] = title
            it[Milestones.startTime] = startTime
            it[Milestones.endTime] = endTime
            it[Milestones.dueTime] = dueTime
            it[Milestones.subprojectId] = subprojectId
            it[Milestones.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToMilestone)
    }


    override suspend fun editMilestone(
        id: Int,
        title: String,
        startTime: String,
        endTime: String,
        dueTime: String,
        subprojectId: Int,
        description: String
    ): Boolean = Milestones.update({ Milestones.id eq id }) {
        it[Milestones.title] = title
        it[Milestones.startTime] = startTime
        it[Milestones.endTime] = endTime
        it[Milestones.dueTime] = dueTime
        it[Milestones.subprojectId] = subprojectId
        it[Milestones.description] = description
    } > 0

    override suspend fun deleteMilestone(id: Int): Boolean = dbQuery {
        Milestones.deleteWhere { Milestones.id eq id } > 0
    }

    //Users------------------------------------------------------------------------------------

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        fullName = row[Users.fullName],
        email = row[Users.email],
        password = row[Users.password],
        isAdmin = row[Users.isAdmin],
        isTeamMember = row[Users.isTeamMember],
        isManager = row[Users.isManager],
        salt = ""
    )

    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }


    override suspend fun user(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun addNewUser(
        email: String,
        fullName: String,
        password: String,
        isTeamMember: Boolean,
        isAdmin: Boolean,
        isManager: Boolean,
        salt : String,
        hashingService: HashingService
    ): User?  {
        val saltedHash= hashingService.generateSaltedHash(password)
        val user = dbQuery {
            val insertStatement = Users.insert {
                it[Users.fullName] = fullName
                it[Users.email] = email
                it[Users.isAdmin] = isAdmin
                it[Users.isTeamMember] = isTeamMember
                it[Users.password] = saltedHash.hash
                it[Users.isManager] = isManager
                it[Users.salt] = saltedHash.salt

            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
        }
        return user
    }

    override suspend fun editUser(
        id: Int,
        email: String,
        fullName: String,
        isTeamMember: Boolean,
        isAdmin: Boolean,
        isManager: Boolean,
    ): Boolean = Users.update({ Users.id eq id }) {
        it[Users.fullName] = fullName
        it[Users.email] = email
        it[Users.isAdmin] = isAdmin
        it[Users.isTeamMember] = isTeamMember

        it[Users.isManager] = isManager
    } > 0

    override suspend fun editPassword(email: String, password: String, hashingService: HashingService): Boolean {
        val saltedHash = hashingService.generateSaltedHash(password)
        return Users.update({ Users.email eq email }) {
            it[Users.password] = saltedHash.hash
            it[salt] = saltedHash.salt
        } > 0
    }



    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    //Subproject------------------------------------------------------------------------------------------

    private fun resultRowToSubproject(row: ResultRow) = Subproject(
        id = row[Subprojects.id],
        title = row[Subprojects.title],
        endTime = row[Subprojects.endTime],
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
        projectId: Int,
        endTime: String
    ): Subproject? = dbQuery {
        val insertStatement = Subprojects.insert {
            it[Subprojects.title] = title
            it[Subprojects.projectId] = projectId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToSubproject)
    }

    override suspend fun editSubproject(id: Int, title: String, endTime: String): Boolean = dbQuery {
        Subprojects.update({ Subprojects.id eq id }) {
            it[Subprojects.title] = title
            it[Subprojects.endTime] = endTime
        } > 0
    }

    override suspend fun deleteSubproject(id: Int): Boolean = dbQuery {
        Subprojects.deleteWhere { Subprojects.id eq id } > 0
    }
// Teams -------------------------------------------------------------------------

    private fun resultRowToTeam(row: ResultRow) = Team(
        id = row[Subprojects.id],
        name = row[Subprojects.title]
    )

    override suspend fun allTeams(): List<Team> = dbQuery {
        Teams.selectAll().map(::resultRowToTeam)

    }

    override suspend fun team(id: Int): Team? = dbQuery {
        Teams
            .select { Teams.id eq id }
            .map(::resultRowToTeam)
            .singleOrNull()
    }

    override suspend fun addNewTeam(name: String): Team? = dbQuery {
        val insertStatement = Teams.insert {
            it[Teams.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeam)
    }

    override suspend fun editTeam(id: Int, name: String): Boolean = dbQuery {
        Teams.update({ Teams.id eq id }) {
            it[Teams.name] = name
        } > 0
    }

    override suspend fun deleteTeam(id: Int): Boolean = dbQuery {
        Teams.deleteWhere { Teams.id eq id } > 0
    }
// Tasks ---------------------------------------------------------------------------------------


    private fun resultRowToTask(row: ResultRow): Task {
        val teamMember = runBlocking { teamMember(row[Tasks.teamMemberId]) }
        val milestone = runBlocking { milestone(row[Tasks.milestoneId]) }

        return Task(
            id = row[Tasks.id],
            milestone = milestone!!,
            teamMember = teamMember!!,
            startTime = row[Tasks.startTime],
            dueTime = row[Tasks.dueTime],
            endTime = row[Tasks.endTime],
            description = row[Tasks.description],
            priority = row[Tasks.priority],
            status = row[Tasks.status]
        )
    }

    override suspend fun allTasks(): List<Task> = dbQuery {
        Tasks.selectAll().map(::resultRowToTask)

    }

    override suspend fun task(id: Int): Task? = dbQuery {
        Tasks
            .select { Tasks.id eq id }
            .map(::resultRowToTask)
            .singleOrNull()
    }

    override suspend fun addNewTask(
        milestoneId: Int,
        teamMemberId: Int,
        teamId: Int,
        joinDate: String,
        startTime: String,
        dueTime: String,
        endTime: String,
        description: String,
        priority: String,
        status: String
    ): Task? {
        val insertStatement = Tasks.insert {
            it[Tasks.milestoneId] = milestoneId
            it[Tasks.teamMemberId] = teamMemberId
            it[Tasks.startTime] = startTime
            it[Tasks.dueTime] = dueTime
            it[Tasks.endTime] = endTime
            it[Tasks.description] = description
            it[Tasks.priority] = priority
            it[Tasks.status] = status
        }
        return insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTask)

    }

    override suspend fun editTask(
        id: Int,
        milestoneId: Int,
        teamMemberId: Int,
        startTime: String,
        dueTime: String,
        endTime: String,
        description: String,
        priority: String,
        status: String
    ): Boolean = dbQuery {
        Tasks.update({ Tasks.id eq id }) {
            it[Tasks.milestoneId] = milestoneId
            it[Tasks.teamMemberId] = teamMemberId
            it[Tasks.startTime] = startTime
            it[Tasks.endTime] = endTime
            it[Tasks.description] = description
            it[Tasks.priority] = priority
            it[Tasks.status] = status


        } > 0
    }


    override suspend fun deleteTask(id: Int): Boolean = dbQuery {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

    //Team members -----------------------------------------------------------------------------------

    private fun resultRowToTeamMember(row: ResultRow) = TeamMember(
        id= row[TeamMembers.id],
        userId = row[TeamMembers.userId],
        teamId = row[TeamMembers.teamId],
        role = row[TeamMembers.role],
        isTeamLeader = row[TeamMembers.isTeamLeader],
        joinDate = row[TeamMembers.joinDate],
        leaveDate = row[TeamMembers.leaveDate]
    )

    override suspend fun allTeamMembers(): List<TeamMember> = dbQuery {
        TeamMembers.selectAll().map(::resultRowToTeamMember)

    }

    override suspend fun teamMember(id: Int): TeamMember? = dbQuery {
        TeamMembers
            .select { TeamMembers.id eq id }
            .map(::resultRowToTeamMember)
            .singleOrNull()
    }

    override suspend fun teamMemberByTeam(teamId: Int): TeamMember? = dbQuery {
        TeamMembers
            .select { TeamMembers.teamId eq teamId}
            .map(::resultRowToTeamMember)
            .singleOrNull()
    }

    override suspend fun addNewTeamMember(
        userId: Int,
        teamId: Int,
        role: String,
        isTeamLeader: Boolean,
        joinDate: String,
        leaveDate: String
    ): TeamMember? = dbQuery {
        val insertStatement = TeamMembers.insert {
            it[TeamMembers.teamId] = teamId
            it[TeamMembers.userId] = userId
            it[TeamMembers.role] = role
            it[TeamMembers.isTeamLeader] = isTeamLeader
            it[TeamMembers.joinDate] = joinDate
            it[TeamMembers.leaveDate] = leaveDate


        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeamMember)
    }

    override suspend fun editTeamMember(
        id: Int,
        userId: Int,
        teamId: Int,
        role: String,
        isTeamLeader: Boolean,
        joinDate: String,
        leaveDate: String
    ): Boolean = dbQuery {
        TeamMembers.update({ TeamMembers.id eq id }) {
            it[TeamMembers.teamId] = teamId
            it[TeamMembers.role] = role
            it[TeamMembers.isTeamLeader] = isTeamLeader
            it[TeamMembers.joinDate] = joinDate
            it[TeamMembers.leaveDate] = leaveDate

        } > 0
    }

    override suspend fun deleteTeamMember(id: Int): Boolean = dbQuery {
        TeamMembers.deleteWhere { userId eq id } > 0
    }

    override suspend fun hasLeftTeam(userId: Int, teamId: Int, joinDate: String): Boolean {
        return dbQuery {
            TeamMembers.select { (TeamMembers.userId eq userId) and (TeamMembers.teamId eq teamId) and (TeamMembers.joinDate eq joinDate) }
                .mapNotNull { it[TeamMembers.leaveDate] }
                .singleOrNull() != ""
        }
    }

    override suspend fun leaveTeam(id: Int, leaveDate: String): Boolean {
        return dbQuery {
            TeamMembers.update({ (TeamMembers.id eq id) }) {
                it[this.leaveDate] = leaveDate
            } > 0
        }
    }

    private fun resultRowToIssue(row: ResultRow): Issue {
        return Issue(
            id = row[Issues.id],
            taskId = row[Issues.taskId],
            title = row[Issues.title],
            description = row[Issues.description],
            status = row[Issues.status],
            priority = row[Issues.priority],
            assigneeId = row[Issues.assigneeId]
        )
    }

    override suspend fun allIssues(): List<Issue> = dbQuery {
        Issues.selectAll().map(::resultRowToIssue)
    }

    override suspend fun issue(id: Int): Issue? = dbQuery {
        Issues
            .select { Issues.id eq id }
            .map(::resultRowToIssue)
            .singleOrNull()
    }

    override suspend fun addNewIssue(
        taskId: Int,
        title: String,
        description: String,
        status: String,
        priority: String,
        assigneeId: Int
    ): Issue? = dbQuery {
        val insertStatement = Issues.insert {
            it[Issues.taskId] = taskId
            it[Issues.title] = title
            it[Issues.description] = description
            it[Issues.status] = status
            it[Issues.priority] = priority
            it[Issues.assigneeId] = assigneeId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToIssue)
    }

    override suspend fun editIssue(
        id: Int,
        taskId: Int,
        title: String,
        description: String,
        status: String,
        priority: String,
        assigneeId: Int
    ): Boolean = dbQuery {
        Issues.update({ Issues.id eq id }) {
            it[Issues.taskId] = taskId
            it[Issues.title] = title
            it[Issues.description] = description
            it[Issues.status] = status
            it[Issues.priority] = priority
            it[Issues.assigneeId] = assigneeId

        } > 0

    }

    override suspend fun deleteIssue(id: Int): Boolean = dbQuery {
        Issues.deleteWhere { Issues.id eq id } > 0
    }
}
val dao: DAOFacade = DAOFacadeImpl()