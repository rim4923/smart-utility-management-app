package com.example.capstoneutilitrack.data.network

import android.content.Context
import android.content.SharedPreferences

object AccessTokenUtilities {

    private const val PREF_NAME = "user_pref"
    private const val KEY_TOKEN = "access_token"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}