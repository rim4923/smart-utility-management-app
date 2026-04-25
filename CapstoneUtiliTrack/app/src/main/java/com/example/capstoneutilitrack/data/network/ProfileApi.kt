package com.example.capstoneutilitrack.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Body
import okhttp3.MultipartBody
import retrofit2.http.Part

data class ProfileDto(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,

    val profileImageUrl: String?,
    val notificationsEnabled: Boolean
)

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String
)
data class UpdateNotificationRequest(
    val enabled: Boolean
)
interface ProfileApi {
    @GET("api/User/profile")
    suspend fun getProfile(): Response<ProfileDto>

    @PUT("api/User/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<Unit>

    @Multipart
    @PUT("api/User/profile/image")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @PUT("api/User/notifications")
    suspend fun updateNotifications(
        @Body request: UpdateNotificationRequest
    ): Response<Unit>
}
