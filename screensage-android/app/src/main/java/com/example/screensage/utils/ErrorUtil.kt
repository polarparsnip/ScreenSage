package com.example.screensage.utils

import org.json.JSONObject
import retrofit2.Response

object ErrorUtil {
    fun <T> parseApiErrorMessage(response: Response<T>): String {
        return try {
            val contentType = response.headers()["Content-Type"]
            val errorBody = response.errorBody()?.string()

            when {
                contentType != null && contentType.contains("application/json") -> {
                    val jsonObject = JSONObject(errorBody ?: "{}")
                    jsonObject.optString("error", "Unknown error")
                }
                !errorBody.isNullOrBlank() -> {
                    errorBody // Return plain text error message
                }
                else -> "Unknown error"
            }
        } catch (e: Exception) {
            "Failed to parse error: ${e.message}"
        }
    }
}
