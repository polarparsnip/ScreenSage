package com.example.screensage.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaDetailed(
    @SerialName("average_rating"       ) var averageRating       : Double?                        = null,
    @SerialName("user_rating"          ) var userRating          : Double?                        = null,
    @SerialName("like_count"           ) var likeCount           : Int?                           = null,
    @SerialName("user_has_liked"       ) var userHasLiked        : Boolean?                       = null,
    @SerialName("id"                   ) var id                  : Int?                           = null,
    @SerialName("title"                ) var title               : String?                        = null,
    @SerialName("original_title"       ) var originalTitle       : String?                        = null,
    @SerialName("original_language"    ) var originalLanguage    : String?                        = null,
    @SerialName("overview"             ) var overview            : String?                        = null,
    @SerialName("backdrop_path"        ) var backdropPath        : String?                        = null,
    @SerialName("poster_path"          ) var posterPath          : String?                        = null,
    @SerialName("popularity"           ) var popularity          : Double?                        = null,
    @SerialName("adult"                ) var adult               : Boolean?                       = null,
    @SerialName("budget"               ) var budget              : Int?                           = null,
    @SerialName("genres"               ) var genres              : List<Genre>                    = listOf(),
    @SerialName("homepage"             ) var homepage            : String?                        = null,
    @SerialName("origin_country"       ) var originCountry       : List<String>                   = listOf(),
    @SerialName("production_companies" ) var productionCompanies : List<ProductionCompanies>      = listOf(),
    @SerialName("production_countries" ) var productionCountries : List<ProductionCountries>      = listOf(),
    @SerialName("release_date"         ) var releaseDate         : String?                        = null,
    @SerialName("revenue"              ) var revenue             : Int?                           = null,
    @SerialName("runtime"              ) var runtime             : Int?                           = null,
    @SerialName("spoken_languages"     ) var spokenLanguages     : List<SpokenLanguages>          = listOf(),
    @SerialName("status"               ) var status              : String?                        = null,
    @SerialName("tagline"              ) var tagline             : String?                        = null,
    @SerialName("video"                ) var video               : Boolean?                       = null,
    @SerialName("videos"               ) var videos              : Videos?                        = Videos(),
    @SerialName("number_of_episodes"   ) var numberOfEpisodes    : Int?                           = null,
    @SerialName("number_of_seasons"    ) var numberOfSeasons     : Int?                           = null,
    @SerialName("name"                 ) var name                : String?                        = null,
    @SerialName("original_name"        ) var originalName        : String?                        = null,
    @SerialName("first_air_date"       ) var firstAirDate        : String?                        = null,
)

@Serializable
data class Genre (
    @SerialName("id"  ) var id   : Int,
    @SerialName("name") var name : String
)

@Serializable
data class ProductionCompanies (
    @SerialName("id"             ) var id            : Int?    = null,
    @SerialName("logo_path"      ) var logoPath      : String? = null,
    @SerialName("name"           ) var name          : String? = null,
    @SerialName("origin_country" ) var originCountry : String? = null
)

@Serializable
data class ProductionCountries (
    @SerialName("iso_3166_1" ) var iso31661 : String? = null,
    @SerialName("name"       ) var name     : String? = null
)

@Serializable
data class SpokenLanguages (
    @SerialName("english_name" ) var englishName : String? = null,
    @SerialName("iso_639_1"    ) var iso6391     : String? = null, @SerialName("name"         ) var name        : String? = null
)

@Serializable
data class Videos (
    @SerialName("results" ) var results : ArrayList<VideoResults> = arrayListOf()
)

@Serializable
data class VideoResults (
    @SerialName("iso_639_1"    ) var iso6391     : String?  = null,
    @SerialName("iso_3166_1"   ) var iso31661    : String?  = null,
    @SerialName("name"         ) var name        : String?  = null,
    @SerialName("key"          ) var key         : String?  = null,
    @SerialName("site"         ) var site        : String?  = null,
    @SerialName("size"         ) var size        : Int?     = null,
    @SerialName("type"         ) var type        : String?  = null,
    @SerialName("official"     ) var official    : Boolean? = null,
    @SerialName("published_at" ) var publishedAt : String?  = null,
    @SerialName("id"           ) var id          : String?  = null
)