package com.example.data.dao

import com.example.data.models.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val jdbcURL = "jdbc:mysql://localhost:3306/teamify"
        val user = "root"
        val passWord = ""
        val database = Database.connect(jdbcURL, driverClassName,user,passWord)
        transaction(database) {
            SchemaUtils.drop(Users,Projects,Milestones,Subprojects,Teams,TeamsProjects,TeamMembers)
            SchemaUtils.create(Users,Projects,Milestones,Subprojects,Teams,TeamsProjects,TeamMembers)

        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}