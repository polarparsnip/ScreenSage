package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class JWTPayload(
    val user: User,
    val token: String,
)