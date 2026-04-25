package com.example.capstoneutilitrack.data.repository

import com.example.capstoneutilitrack.data.network.AuthApi
import com.example.capstoneutilitrack.data.network.LoginRequest
import com.example.capstoneutilitrack.data.network.CheckEmailRequest
import com.example.capstoneutilitrack.data.network.OcrApi
import com.example.capstoneutilitrack.data.network.RegisterStep2Request
import com.example.capstoneutilitrack.model.OcrResponse
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


@Singleton
class AuthRepository @Inject constructor(
    private val authService: AuthApi,
    private val ocrApi: OcrApi
) {
    suspend fun checkEmail(email: String) =
        authService.checkEmail(CheckEmailRequest(email))

    suspend fun register(request: RegisterStep2Request) =
        authService.registerStep2(request)

    suspend fun login(email: String, password: String) =
        authService.login(LoginRequest(email, password))

    private val useMockOcr = true

    suspend fun sendResetCode(email: String): Result<Unit> {
        return try {
            val response = authService.sendResetCode(mapOf("email" to email))

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun scanPassport(file: File): Result<OcrResponse> {
        return if (useMockOcr) {
            mockOcr()
        } else {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestFile
            )

            val response = ocrApi.scanPassport(multipart)

            Result.success(response)
        }
    }

    private suspend fun mockOcr(): Result<OcrResponse> {
        kotlinx.coroutines.delay(1500)

        return Result.success(
            OcrResponse(
                passportNumber = "A12345678",
                isSuccess = true,
                confidenceScore = 93.4,
                errorMessage = null
            )
        )
    }
    suspend fun verifyResetCode(email: String, code: String): Result<Unit> {
        return try {
            val response = authService.verifyCode(
                mapOf(
                    "email" to email,
                    "code" to code
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid code"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        return try {
            val response = authService.resetPassword(
                mapOf(
                    "email" to email,
                    "newPassword" to newPassword
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Reset failed"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
