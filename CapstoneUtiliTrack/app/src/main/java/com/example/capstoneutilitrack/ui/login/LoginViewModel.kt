package com.example.capstoneutilitrack.ui.login

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneutilitrack.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : AndroidViewModel(application) {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    fun validateInputs(): Boolean {
        var isValid = true

        emailError = null
        passwordError = null

        if (email.isBlank()) {
            emailError = "Please enter your email."
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email."
            isValid = false
        }else if (password.isBlank()) {
            passwordError = "Please enter your password."
            isValid = false
        }
        else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }
        return isValid
    }

    fun onLoginClick() {
        if (!validateInputs()) return

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                authRepository.login(email, password)
            }
            isLoading = false

            if (result.isSuccessful) {
                val token = result.body()?.token

                if (token.isNullOrEmpty()) {
                    errorMessage = "Invalid server response"
                    return@launch
                }

                val prefs = application.getSharedPreferences("auth", Context.MODE_PRIVATE)
                prefs.edit().putString("jwt", token).apply()

                loginSuccess = true
                errorMessage = null
                emailError = null
                passwordError = null
            } else {
                val raw = result.errorBody()?.string()
                val errorMsg = raw ?: "Login failed"

                when {
                    errorMsg.contains("not found", true) ->
                        emailError = "Email not registered"

                    errorMsg.contains("password", true) ->
                        passwordError = "Incorrect password"

                    errorMsg.contains("not verified", true) ->
                        emailError = "Please verify your account first"

                    else ->
                        errorMessage = errorMsg
                }
            }
        }
    }
}