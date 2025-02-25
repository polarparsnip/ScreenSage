package com.example.screensage.network

import com.example.screensage.entities.JWTPayload
import com.example.screensage.entities.IndexApiResponse
import com.example.screensage.entities.User
import io.github.cdimascio.dotenv.dotenv
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
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

}

object ScreensageApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}