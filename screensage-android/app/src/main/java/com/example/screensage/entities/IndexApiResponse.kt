package com.example.screensage.entities

import kotlinx.serialization.Serializable

@Serializable
data class IndexApiResponse(
    val endpoints: List<EndpointCategory>
)

@Serializable
data class EndpointCategory(
    val users: List<ApiEndpoint>? = null,
    val media: List<ApiEndpoint>? = null,
    val lists: List<ApiEndpoint>? = null,
    val challenge: List<ApiEndpoint>? = null
)

@Serializable
data class ApiEndpoint(
    val method: String,
    val description: String,
    val path: String
)