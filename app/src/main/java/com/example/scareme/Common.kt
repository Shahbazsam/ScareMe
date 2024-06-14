package com.example.scareme

import android.content.Context
import android.content.SharedPreferences

object SaveTokenUtil {
    private const val PREFERENCES_FILE_KEY = "com.example.scareme.PREFERENCE_FILE_KEY"
    private const val TOKEN_KEY = "TOKEN_KEY"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        return getSharedPreferences(context).getString(TOKEN_KEY, null)
    }
}