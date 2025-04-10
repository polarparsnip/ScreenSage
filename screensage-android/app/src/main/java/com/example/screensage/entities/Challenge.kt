package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: Int,
    val question: String,
    val instructions: String? = null,
    val image: String,
    val type: String,
    val points: Int,
    val options: List<ChallengeOption>
)

@Serializable
data class ChallengeOption(
    val id: Int,
    val option: String,
)
