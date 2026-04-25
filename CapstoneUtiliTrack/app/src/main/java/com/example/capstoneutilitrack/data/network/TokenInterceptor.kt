package com.example.capstoneutilitrack.data.network

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {

        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt", null)

        Log.d("TOKEN", "Token = $token")

        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(request)
    }
}