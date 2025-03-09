package com.example.screensage.utils

import android.content.Context
import android.widget.Toast

/**
 * Utility object for displaying Toast messages.
 */
object ToastUtil {
    /**
     * Displays a Toast message with a long duration.
     *
     * @param context The context in which the Toast should be displayed.
     * @param message The message to be shown in the Toast.
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
