package com.example.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
