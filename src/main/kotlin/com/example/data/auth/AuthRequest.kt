package com.example.data.auth

import kotlinx.serialization.Serializable


@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)
