package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.network.ProfileApi
import com.example.capstoneutilitrack.data.network.ProfileDto
import com.example.capstoneutilitrack.data.network.UpdateNotificationRequest
import com.example.capstoneutilitrack.data.network.UpdateProfileRequest
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ProfileApi
) {
    suspend fun getProfile(): ProfileDto {
        val response = api.getProfile()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Profile fetch failed: ${response.code()}")
        }
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
