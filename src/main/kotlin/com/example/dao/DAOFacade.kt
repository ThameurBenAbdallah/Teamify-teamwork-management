package com.example.dao

import com.example.models.*

interface DAOFacade {
    suspend fun allProjects(): List<Project>
    suspend fun project(id: Int): Project?
    suspend fun addNewProject(title: String, dueTime: String, endTime: String, startTime: String, statue: String, description: String): Project?
    suspend fun editProject(id: Int, title: String, dueTime: String, endTime: String, startTime: String, statue: String,description: String): Boolean
    suspend fun deleteProject(id: Int): Boolean

    suspend fun allMilestones(): List<Milestone>
    suspend fun milestone(id: Int): Milestone?
    suspend fun addNewMilestone(title: String, startTime: String, dueTime: String, subprojectId: Int, description: String,): Milestone?
    suspend fun editMilestone(id: Int, title: String, startTime: String, dueTime: String, description: String): Boolean
    suspend fun deleteMilestone(id: Int): Boolean

    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun addNewUser(email: String, fullName: String, password: String, isTeamMember: Boolean, isAdmin: Boolean): User?
    suspend fun editUser(id: Int, email: String, fullName: String, password: String, isTeamMember: Boolean, isAdmin: Boolean): Boolean
    suspend fun deleteUser(id: Int): Boolean

    suspend fun allSubprojects(): List<Subproject>
    suspend fun subproject(id: Int): Subproject?
    suspend fun addNewSubproject(title: String, endTime: String, startTime: String, statue: String, projectId: Int): Subproject?
    suspend fun editSubproject(id: Int, title: String, body: String): Boolean
    suspend fun deleteSubproject(id: Int): Boolean

    suspend fun allTeams(): List<Team>
    suspend fun team(id: Int): Team?
    suspend fun addNewTeam(name: String,leader: Int): Team?
    suspend fun editTeam(id: Int, title: String, body: String): Boolean
    suspend fun deleteTeam(id: Int): Boolean
}