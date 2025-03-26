package com.example.screensage.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName("id"         ) var id         : Int?    = null,
    @SerialName("mediaId"    ) var mediaId    : Int?    = null,
    @SerialName("mediaTitle" ) var mediaTitle : String? = null,
    @SerialName("user"       ) var user       : User?   = null,
    @SerialName("rating"     ) var rating     : Double? = null,
    @SerialName("content"    ) var content    : String? = null,
    @SerialName("type"       ) var type       : String? = null,
    @SerialName("createdAt"  ) var createdAt  : String? = null
)
