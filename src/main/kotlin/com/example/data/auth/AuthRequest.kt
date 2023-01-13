package com.example.data.auth

import kotlinx.serialization.Serializable


@Serializable
data class AuthRequest(
    val fullName: String,
    val email: String,
    val password: String
)
