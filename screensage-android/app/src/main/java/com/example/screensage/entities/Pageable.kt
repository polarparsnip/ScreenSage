package com.example.screensage.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pageable(
    @SerialName("pageNumber" ) var pageNumber : Int?     = null,
    @SerialName("pageSize"   ) var pageSize   : Int?     = null,
    @SerialName("sort"       ) var sort       : Sort?    = Sort(),
    @SerialName("offset"     ) var offset     : Int?     = null,
    @SerialName("paged"      ) var paged      : Boolean? = null,
    @SerialName("unpaged"    ) var unpaged    : Boolean? = null
)

@Serializable
data class Sort (
    @SerialName("empty"    ) var empty    : Boolean? = null,
    @SerialName("sorted"   ) var sorted   : Boolean? = null,
    @SerialName("unsorted" ) var unsorted : Boolean? = null
)
