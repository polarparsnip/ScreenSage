package com.example.screensage.network

import com.example.screensage.entities.Genre
import com.example.screensage.entities.JWTPayload
import com.example.screensage.entities.IndexApiResponse
import com.example.screensage.entities.Media
import com.example.screensage.entities.MediaDetailed
import com.example.screensage.entities.User
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
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
}

object ScreensageApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}