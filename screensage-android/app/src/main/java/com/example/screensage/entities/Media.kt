package com.example.screensage.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Media(
    @SerialName("average_rating"    ) val averageRating    : Double,
    @SerialName("user_rating"       ) val userRating       : Double,
    @SerialName("id"                ) val id               : Int,
    @SerialName("title"             ) val title            : String?        = null,
    @SerialName("original_title"    ) val originalTitle    : String?        = null,
    @SerialName("release_date"      ) val releaseDate      : String?        = null,
    @SerialName("original_language" ) val originalLanguage : String,
    @SerialName("overview"          ) val overview         : String,
    @SerialName("adult"             ) val adult            : Boolean,
    @SerialName("genre_ids"         ) val genreIds         : ArrayList<Int> = arrayListOf(),
    @SerialName("popularity"        ) val popularity       : Double,
    @SerialName("backdrop_path"     ) val backdropPath     : String?        = null,
    @SerialName("poster_path"       ) val posterPath       : String?        = null,
    @SerialName("video"             ) val video            : Boolean,

    // TV/Anime
    @SerialName("origin_country"    ) val originCountry    : List<String>?  = null,
    @SerialName("name"              ) val name             : String?        = null,
    @SerialName("original_name"     ) val originalName     : String?        = null,
    @SerialName("first_air_date"    ) val firstAirDate     : String?        = null
)
