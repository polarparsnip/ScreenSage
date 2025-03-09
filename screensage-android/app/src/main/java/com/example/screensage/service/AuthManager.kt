package com.example.screensage.service

import android.content.Context

/**
 * Object responsible for managing authentication tokens using SharedPreferences.
 */
object AuthManager {
    private const val PREFS_NAME = "auth_prefs" // SharedPreferences file name
    private const val TOKEN = "jwt_token" // Key for storing the JWT token

    /**
     * Checks if a user is logged in by verifying the existence of a stored token.
     *
     * @param context The application context used to access SharedPreferences.
     * @return `true` if a token exists, `false` otherwise.
     */
    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(TOKEN)
    }

    /**
     * Saves the JWT token to SharedPreferences.
     *
     * @param context The application context used to access SharedPreferences.
     * @param token The JWT token to be saved.
     */
    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(TOKEN, token)
            .apply()
    }

    /**
     * Retrieves the stored JWT token from SharedPreferences.
     *
     * @param context The application context used to access SharedPreferences.
     * @return The stored JWT token, or `null` if not found.
     */
    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN, null)
    }

    /**
     * Removes the stored JWT token from SharedPreferences, effectively logging the user out.
     *
     * @param context The application context used to access SharedPreferences.
     */
    fun removeToken(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(TOKEN)
            .apply()
    }
}
