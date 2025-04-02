package com.example.screensage.network

import com.example.screensage.entities.Genre
import com.example.screensage.entities.JWTPayload
import com.example.screensage.entities.IndexApiResponse
import com.example.screensage.entities.Media
import com.example.screensage.entities.MediaDetailed
import com.example.screensage.entities.MediaList
import com.example.screensage.entities.Pageable
import com.example.screensage.entities.Review
import com.example.screensage.entities.Sort
import com.example.screensage.entities.User
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

val dotenv = dotenv {
    directory = "/assets"
    filename = "env"
}
private val BASE_URL = dotenv["BASE_URL"]

val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.HEADERS
}

val client: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
}.build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class MediaResponse(
    @SerialName("page"         ) val page: Int,
    @SerialName("total_pages"  ) val totalPages: Int,
    @SerialName("total_results") val totalResults: Int,
    @SerialName("results"      ) val results: List<Media>
)

@Serializable
data class MediaGenres(
    @SerialName("genres") var genres : List<Genre> = listOf()
)

@Serializable
data class ReviewResponse(
    @SerialName("content"          ) var content          : List<Review>       = listOf(),
    @SerialName("pageable"         ) var pageable         : Pageable?          = Pageable(),
    @SerialName("last"             ) var last             : Boolean?           = null,
    @SerialName("totalPages"       ) var totalPages       : Int,
    @SerialName("totalElements"    ) var totalElements    : Int?               = null,
    @SerialName("first"            ) var first            : Boolean?           = null,
    @SerialName("size"             ) var size             : Int?               = null,
    @SerialName("number"           ) var number           : Int?               = null,
    @SerialName("sort"             ) var sort             : Sort?              = Sort(),
    @SerialName("numberOfElements" ) var numberOfElements : Int?               = null,
    @SerialName("empty"            ) var empty            : Boolean?           = null
)

@Serializable
data class ReviewRequest(
    val rating: Double,
    val content: String
)

@Serializable
data class UsernameRequest(
    val username: String
)

@Serializable
data class PasswordRequest(
    val password: String
)

@Serializable
data class MediaListResponse(
    @SerialName("content"          ) var content          : List<MediaList>    = listOf(),
    @SerialName("pageable"         ) var pageable         : Pageable?          = Pageable(),
    @SerialName("last"             ) var last             : Boolean?           = null,
    @SerialName("totalPages"       ) var totalPages       : Int,
    @SerialName("totalElements"    ) var totalElements    : Int?               = null,
    @SerialName("first"            ) var first            : Boolean?           = null,
    @SerialName("size"             ) var size             : Int?               = null,
    @SerialName("number"           ) var number           : Int?               = null,
    @SerialName("sort"             ) var sort             : Sort?              = Sort(),
    @SerialName("numberOfElements" ) var numberOfElements : Int?               = null,
    @SerialName("empty"            ) var empty            : Boolean?           = null
)

interface NetworkService {
    @GET("/")
    suspend fun getIndex(): Response<IndexApiResponse>

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<JWTPayload>

    @Multipart
    @POST("/register")
    suspend fun register(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<User>

    @GET("/{mediaType}")
    suspend fun getMedia(
        @Header("Authorization") token: String,
        @Path("mediaType") mediaType: String,
        @Query("page") page: Int = 1,
        @Query("genreId") genreId: Int? = null,
        @Query("search") search: String? = null
    ): Response<MediaResponse>

    @GET("/{mediaType}/{id}")
    suspend fun getMediaById(
        @Header("Authorization") token: String,
        @Path("mediaType") media: String,
        @Path("id") id: Int
    ): Response<MediaDetailed>

    @GET("/{mediaType}/genres")
    suspend fun getMediaGenres(
        @Header("Authorization") token: String,
        @Path("mediaType") media: String
    ): Response<MediaGenres>

    @GET("/{mediaType}/{id}/reviews")
    suspend fun getMediaReviewsById(
        @Header("Authorization") token: String,
        @Path("mediaType") media: String,
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): Response<ReviewResponse>

    @POST("/{mediaType}/{id}/reviews")
    suspend fun postMediaReview(
        @Header("Authorization") token: String,
        @Path("mediaType") media: String,
        @Path("id") id: Int,
        @Query("title") title: String,
        @Body request: ReviewRequest
    ): Response<Review>

    @GET("/users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<User>

    @PATCH("/users/profile/username")
    suspend fun updateUsername(
        @Header("Authorization") token: String,
        @Body request: UsernameRequest
    ): Response<JWTPayload>

    @PATCH("/users/profile/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body request: PasswordRequest
    ): Response<User>

    @Multipart
    @PATCH("/users/profile/image")
    suspend fun updateProfileImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part? = null
    ): Response<User>

    @GET("/users/profile/reviews")
    suspend fun getUserReviews(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Response<ReviewResponse>

    @POST("/{mediaType}/{id}/likes")
    suspend fun likeMedia(
        @Header("Authorization") token: String,
        @Path("mediaType") media: String,
        @Path("id") id: Int
    ): Response<ResponseBody>

    @GET("/lists")
    suspend fun getMediaLists(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
    ): Response<MediaListResponse>

    @GET("/users/profile/lists")
    suspend fun getMyMediaLists(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
    ): Response<MediaListResponse>

    @GET("/users/profile/watchlists")
    suspend fun getWatchlists(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
    ): Response<MediaListResponse>
}

object ScreensageApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}