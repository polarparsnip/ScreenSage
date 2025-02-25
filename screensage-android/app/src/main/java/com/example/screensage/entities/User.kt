package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val profileImg: String,
    val passwordPlaceholder: String,
    val lists: List<MediaList>? = null,
    val watchlists: List<MediaList>? = null
)
