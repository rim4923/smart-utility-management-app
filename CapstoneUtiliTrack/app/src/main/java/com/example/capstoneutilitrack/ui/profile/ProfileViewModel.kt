package com.example.capstoneutilitrack.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneutilitrack.data.network.AccessTokenUtilities
import com.example.capstoneutilitrack.data.network.ProfileDto
import com.example.capstoneutilitrack.data.network.UpdateNotificationRequest
import com.example.capstoneutilitrack.data.network.UpdateProfileRequest
import com.example.capstoneutilitrack.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


data class ProfileUiState(
    val id: Int = 0,

    val fullName: String = "",
    val email: String = "",
    val phone: String = "",

    val profileImage: String? = null,

    val notificationsEnabled: Boolean = true,

    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    var profileState by mutableStateOf(ProfileUiState())
        private set
    var showEditSheet by mutableStateOf(false)
        private set

    fun openEdit() { showEditSheet = true }
    fun closeEdit() { showEditSheet = false }

    init {
        loadProfile()
    }

    fun ProfileDto.toUi(): ProfileUiState {
        return ProfileUiState(
            id = id,
            fullName = "$firstName $lastName",
            email = email,
            phone = phone,
            profileImage = profileImageUrl,
            notificationsEnabled = notificationsEnabled
        )
    }
    fun loadProfile() {
        viewModelScope.launch {
            profileState = profileState.copy(
                isLoading = true,
                error = null
            )

            try {
                val user = repository.getProfile()

                profileState = ProfileUiState(
                    id = user.id,
                    fullName = "${user.firstName} ${user.lastName}",
                    email = user.email,
                    phone = user.phone,
                    profileImage = user.profileImageUrl?.let {
                        if (it.startsWith("http")) it
                        else "http://10.0.2.2:5082$it"
                    },
                    notificationsEnabled = user.notificationsEnabled,
                    isLoading = false
                )

            } catch (e: Exception) {
                profileState = profileState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun uploadImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val stream = context.contentResolver.openInputStream(uri)
                val file = stream!!.readBytes()

                val requestBody = file.toRequestBody("image/*".toMediaType())
                val part = MultipartBody.Part.createFormData(
                    "image",
                    "profile.jpg",
                    requestBody
                )

                repository.uploadProfileImage(part)

                loadProfile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateNotifications(
                    UpdateNotificationRequest(enabled)
                )

                profileState = profileState.copy(
                    notificationsEnabled = enabled
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout(context: Context, onLogout: () -> Unit) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().remove("jwt").apply()

        onLogout()
    }

    fun updateProfile(first: String, last: String, email: String, phone: String) {
        viewModelScope.launch {
            try {
                profileState = profileState.copy(isLoading = true)

                repository.updateProfile(
                    UpdateProfileRequest(first, last, email, phone)
                )

                loadProfile()
                delay(300)
                closeEdit()
            } catch (e: Exception) {
                profileState = profileState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun refresh() {
        loadProfile()
    }
}
