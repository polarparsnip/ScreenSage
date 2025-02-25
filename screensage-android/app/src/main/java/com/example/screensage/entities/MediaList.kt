package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class MediaList(
    val id: Int,
    val title: String,
    val description: String? = null,
    val mediaListItems: List<MediaListItem>? = null,
    val watchlist: Boolean,
    val sharedWith: List<Int>? = null,
    val likeCount: Long? = null,
    val userHasLiked: Boolean? = null,
    val createdAt: String? = null,
    val user: User? = null
)
