package com.example.capstoneutilitrack.data.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.Serializable

data class RegisterStep2Request(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val nationality: String
) : Serializable

data class LoginResponse(
    val token: String,
    val email: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class CheckEmailRequest(
    val email: String
)

interface AuthApi {
    @POST("api/Auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): Response<String>

    @POST("api/Auth/register-step2")
    suspend fun registerStep2(@Body request: RegisterStep2Request): Response<Any>

    @POST("api/Auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    @POST("api/Auth/forgot-password")
    suspend fun sendResetCode(@Body body: Map<String, String>): Response<Unit>

    @POST("api/Auth/verify-code")
    suspend fun verifyCode(@Body body: Map<String, String>): Response<Unit>

    @POST("api/Auth/reset-password")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<Unit>
}