package com.example.capstoneutilitrack.ui.camera


import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.capstoneutilitrack.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.onSuccess

@HiltViewModel
class IDUploadViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var imageUri by mutableStateOf<Uri?>(null)
        private set
    var pickedMethod by mutableStateOf<String?>(null)

    var isSubmitting by mutableStateOf(false)
        private set

    var userId by mutableStateOf<String?>(null)
    var passportId by mutableStateOf<String?>(null)
    var uploadSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun setImage(uri: Uri) {
        imageUri = uri
    }

    fun clearImage() {
        imageUri = null
    }

    fun uriToFile(uri: Uri, fileName: String, context: android.content.Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File(context.cacheDir, fileName)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    fun submitPassport(file: File) {
        isSubmitting = true

        viewModelScope.launch {
            val result = authRepository.scanPassport(file)

            isSubmitting = false

            result.onSuccess {
                if (it.isSuccess) {
                    passportId = it.passportNumber
                    uploadSuccess = true
                } else {
                    errorMessage = it.errorMessage
                }
            }.onFailure {
                errorMessage = it.message
            }
        }
    }
}
