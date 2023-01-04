package com.example.dao

import com.example.models.*

interface DAOFacade {
    suspend fun allProjects(): List<Project>
    suspend fun project(id: Int): Project?
    suspend fun addNewProject(title: String, endTime: String, startTime: String, description: String): Project?
    suspend fun editProject(id: Int, title: String, endTime: String, startTime: String, description: String): Boolean
    suspend fun deleteProject(id: Int): Boolean

    suspend fun allMilestones(): List<Milestone>
    suspend fun milestone(id: Int): Milestone?
    suspend fun addNewMilestone(
        title: String,
        startTime: String,
        endTime: String,
        dueTime: String,
        subprojectId: Int,
        description: String
    ): Milestone?

    suspend fun editMilestone(
        id: Int,
        title: String,
        startTime: String,
        endTime: String,
        dueTime: String,
        subprojectId: Int,
        description: String
    ): Boolean

    suspend fun deleteMilestone(id: Int): Boolean

    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun addNewUser(
        email: String,
        fullName: String,
        password: String,
        isTeamMember: Boolean,
        isAdmin: Boolean,
        isManager: Boolean
    ): User?

    suspend fun editUser(
        id: Int,
        email: String,
        fullName: String,
        password: String,
        isTeamMember: Boolean,
        isAdmin: Boolean,
        isManager: Boolean
    ): Boolean

    suspend fun deleteUser(id: Int): Boolean

    suspend fun allSubprojects(): List<Subproject>
    suspend fun subproject(id: Int): Subproject?
    suspend fun addNewSubproject(title: String, projectId: Int, endTime: String): Subproject?
    suspend fun editSubproject(id: Int, title: String, endTime: String): Boolean
    suspend fun deleteSubproject(id: Int): Boolean

    suspend fun allTeams(): List<Team>
    suspend fun team(id: Int): Team?
    suspend fun addNewTeam(name: String): Team?
    suspend fun editTeam(id: Int, name: String): Boolean
    suspend fun deleteTeam(id: Int): Boolean

    suspend fun allTasks(): List<Task>
    suspend fun task(id: Int): Task?
    suspend fun addNewTask(
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
    ): Task?

    suspend fun editTask(
        id: Int,
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
    ): Boolean

    suspend fun deleteTask(id: Int): Boolean

    suspend fun allTeamMembers(): List<TeamMember>
    suspend fun teamMember(userId: Int, teamId: Int): TeamMember?
    suspend fun teamMemberByTeam(teamId: Int): TeamMember?
    suspend fun addNewTeamMember(
        userId: Int,
        teamId: Int,
        role: String,
        isTeamLeader: Boolean,
        joinDate: String,
        leaveDate: String
    ): TeamMember?

    suspend fun editTeamMember(
        userId: Int,
        teamId: Int,
        role: String,
        isTeamLeader: Boolean,
        joinDate: String,
        leaveDate: String
    ): Boolean

    suspend fun deleteTeamMember(userId: Int, teamId: Int, joinDate: String): Boolean

    suspend fun hasLeftTeam(userId: Int, teamId: Int): Boolean

    suspend fun leaveTeam(userId: Int, teamId: Int, joinDate: String, leaveDate: String): Boolean

        suspend fun allIssues(): List<Issue>
        suspend fun issue(id: Int): Issue?
        suspend fun addNewIssue(
            taskId: Int,
            title: String,
            description: String,
            status: String,
            priority: String,
            assigneeId: Int
        ): Issue?
        suspend fun editIssue(
            id: Int,
            taskId: Int,
            title: String,
            description: String,
            status: String,
            priority: String,
            assigneeId: Int
        ): Boolean
        suspend fun deleteIssue(id: Int): Boolean



}
