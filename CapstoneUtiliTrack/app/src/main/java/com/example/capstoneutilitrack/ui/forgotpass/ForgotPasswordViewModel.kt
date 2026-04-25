package com.example.capstoneutilitrack.ui.forgotpass

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneutilitrack.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class ResetState(
    val email: String = "",
    val codeSent: String = "",
    val enteredCode: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val retries: Int = 0,
    val isLockedOut: Boolean = false,
    val isVerifying: Boolean = false
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(ResetState())
        private set

    fun onEmailChange(newEmail: String) {
        state = state.copy(email = newEmail, error = null)
    }

    fun onCodeEntered(code: String) {
        state = state.copy(enteredCode = code, error = null)
    }

    fun onPasswordChange(pw: String) {
        state = state.copy(newPassword = pw, error = null)
    }

    fun onConfirmPasswordChange(pw: String) {
        state = state.copy(confirmPassword = pw, error = null)
    }

    fun setError(message: String) {
        state = state.copy(error = message)
    }

    fun sendCodeToBackend(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = authRepository.sendResetCode(state.email)

                if (result.isSuccess) {
                    state = state.copy(error = null)
                    onSuccess()
                } else {
                    val msg = result.exceptionOrNull()?.message ?: "Failed to send code"
                    state = state.copy(error = msg)
                    onFailure(msg)
                }

            } catch (e: Exception) {
                val msg = e.message ?: "Something went wrong"
                state = state.copy(error = msg)
                onFailure(msg)
            }
        }
    }

    fun verifyCode(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
        onLockout: () -> Unit
    ) {
        val code = state.enteredCode.trim()

        if (code.length != 4 || code.any { !it.isDigit() }) {
            state = state.copy(error = "Enter valid 4-digit code")
            onFailure("Enter valid 4-digit code")
            return
        }

        if (state.isLockedOut) return

        viewModelScope.launch {
            try {
                state = state.copy(isVerifying = true, error = null)

                val result = authRepository.verifyResetCode(state.email, code)

                if (result.isSuccess) {
                    state = state.copy(retries = 0, isVerifying = false)
                    onSuccess()
                } else {
                    val newRetries = state.retries + 1

                    if (newRetries >= 3) {
                        state = state.copy(
                            retries = newRetries,
                            isLockedOut = true,
                            isVerifying = false,
                            error = "Too many attempts"
                        )
                        onLockout()
                    } else {
                        state = state.copy(
                            retries = newRetries,
                            isVerifying = false,
                            error = "Invalid code"
                        )
                        onFailure("Invalid code")
                    }
                }

            } catch (e: Exception) {
                state = state.copy(isVerifying = false)
                onFailure(e.message ?: "Verification failed")
            }
        }
    }

    fun resetPassword(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (state.newPassword != state.confirmPassword) {
                    onFailure("Passwords do not match")
                    return@launch
                }

                val result = authRepository.resetPassword(
                    state.email,
                    state.newPassword
                )

                if (result.isSuccess) {
                    onSuccess()
                } else {
                    val msg = result.exceptionOrNull()?.message ?: "Reset failed"
                    onFailure(msg)
                }

            } catch (e: Exception) {
                onFailure(e.message ?: "Something went wrong")
            }
        }
    }

    fun resetState() {
        state = ResetState()
    }
}
