package com.example.data.models


import com.example.data.auth.Role
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val password: String,
    val role: Role,
    val salt: String
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 128)
    val fullName = varchar("full_name", 300)
    val password = varchar("password", 300)
    val role= varchar("role",50)
    val salt = varchar("salt",300)
    init {
        uniqueIndex(email)
    }
    override val primaryKey = PrimaryKey(id)
}