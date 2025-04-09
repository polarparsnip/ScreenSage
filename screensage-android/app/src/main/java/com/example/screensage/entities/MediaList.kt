package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class MediaList(
    val id             : Int,
    val title          : String,
    val watchlist      : Boolean,
    val description    : String?              = null,
    val mediaListItems : List<MediaListItem>  = listOf(),
    val sharedWith     : List<Int>?           = null,
    var likeCount      : Long?                = null,
    var userHasLiked   : Boolean?             = null,
    val createdAt      : String?              = null,
    val user           : User?                = null
)
