package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.network.ProfileApi
import com.example.capstoneutilitrack.data.network.ProfileDto
import com.example.capstoneutilitrack.data.network.UpdateNotificationRequest
import com.example.capstoneutilitrack.data.network.UpdateProfileRequest
import com.example.capstoneutilitrack.data.remote.safeApiCall
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ProfileApi
) {
    suspend fun getProfile(): ProfileDto {
        return safeApiCall { api.getProfile() }
    }

    suspend fun updateProfile(request: UpdateProfileRequest) {
        val response = api.updateProfile(request)
        if (!response.isSuccessful) {
            throw Exception("Update failed: ${response.code()}")
        }
    }

    suspend fun uploadProfileImage(image: MultipartBody.Part) {
        val response = api.uploadProfileImage(image)
        if (!response.isSuccessful) {
            throw Exception("Image upload failed: ${response.code()}")
        }
    }

    suspend fun updateNotifications(request: UpdateNotificationRequest) {
        val response = api.updateNotifications(request)
        if (!response.isSuccessful) {
            throw Exception("Notification update failed: ${response.code()}")
        }
    }
}
