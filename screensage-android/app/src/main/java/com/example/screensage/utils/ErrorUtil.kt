package com.example.screensage.utils

import org.json.JSONObject
import retrofit2.Response

/**
 * Utility object for handling and parsing API error messages.
 *
 * This object provides a function to parse error messages from a Retrofit API response,
 * handling different content types and formats.
 */
object ErrorUtil {

    /**
     * Parses the error message from an API response.
     *
     * This function inspects the response to determine the format of the error message.
     * It first checks if the response contains JSON error content and tries to extract the error message.
     * If the content type is not JSON, it will return the plain text error message or default to "Unknown error".
     * In case of any failure during parsing, it will return an error message indicating the failure.
     *
     * @param response The Retrofit [Response] object containing the error details.
     * @return A string representing the error message.
     */
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
