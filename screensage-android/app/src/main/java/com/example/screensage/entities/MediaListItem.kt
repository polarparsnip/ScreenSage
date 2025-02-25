package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class MediaListItem(
    val id: Int,
    val mediaId: Int,
    val mediaTitle: String,
    val mediaSummary: String,
    val mediaImg: String,
    val type: String,
    val createdAt: String
)
