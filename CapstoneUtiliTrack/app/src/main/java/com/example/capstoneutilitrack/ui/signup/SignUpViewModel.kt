package com.example.capstoneutilitrack.ui.signup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneutilitrack.data.repository.AuthRepository
import com.example.capstoneutilitrack.data.network.RegisterStep2Request
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var signUpSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var nationality by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var countryCode by mutableStateOf("+961")
    var signUpRequest by mutableStateOf<RegisterStep2Request?>(null)

    var emailError by mutableStateOf<String?>(null)
    var passwordError: String? by mutableStateOf(null)
    var confirmPasswordError by mutableStateOf<String?>(null)
    var phoneNumberError by mutableStateOf<String?>(null)
    var nationalityError by mutableStateOf<String?>(null)
    var generalError by mutableStateOf<String?>(null)

    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible :Boolean by mutableStateOf(false)

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var firstNameError by mutableStateOf<String?>(null)
    var lastNameError by mutableStateOf<String?>(null)

    private val _step = MutableStateFlow(1)
    val step: StateFlow<Int> = _step

    var emailAvailable by mutableStateOf(false)
        private set

    fun setStep(newStep: Int) {
        _step.value = newStep
    }

    private fun isStrongPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}")
        return regex.matches(password)
    }

    fun onSignUpClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        emailError = null
        passwordError = null
        confirmPasswordError = null
        phoneNumberError = null
        nationalityError = null
        errorMessage = null
        firstNameError = null
        lastNameError = null

        if (firstName.isBlank() || firstName.length < 3) {
            firstNameError = "First name must be at least 3 characters."
            return
        }
        if (lastName.isBlank() || lastName.length < 3) {
            lastNameError = "Last name must be at least 3 characters."
            return
        }
        if (phoneNumber.isBlank()) {
            phoneNumberError = "Please add your phone number"
            return
        }

        val util = PhoneNumberUtil.getInstance()
        val fullNumber = "$countryCode$phoneNumber"
        val number = try {
            util.parse(fullNumber, null)
        } catch (e: Exception) {
            phoneNumberError = "Invalid phone number format"
            return
        }

        if (!util.isValidNumber(number)) {
            phoneNumberError = "Invalid phone number for selected country"
            return
        }

        if (nationality.isBlank()) {
            nationalityError = "Please enter your nationality."
            return
        }


        signUpRequest = RegisterStep2Request(
            email = email,
            password = password,
            phone = "$countryCode$phoneNumber",
            nationality = nationality,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            isLoading = true

            try {
                val result = withContext(Dispatchers.IO) {
                    authRepository.register(signUpRequest!!)
                }

                if (result.isSuccessful) {
                    signUpSuccess = true
                    onSuccess()
                } else {
                    errorMessage = result.errorBody()?.string() ?: "Registration failed"
                    onError(errorMessage!!)
                }

            } catch (e: Exception) {
                errorMessage = "Network error. Please try again."
                onError(errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun validateStepOne(onSuccess: () -> Unit) {
        emailError = null
        passwordError = null
        confirmPasswordError = null
        errorMessage = null

        if (email.isBlank()) {
            emailError = "Please enter your email."
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Enter a valid email."
            return
        }

        if (password.isBlank()) {
            passwordError = "Please enter your password."
            return
        } else if (!isStrongPassword(password)) {
            passwordError =
                "Password must be 8+ characters, include upper/lowercase, digit & symbol."
            return
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Please confirm your password."
            return
        } else if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match."
            return
        }

        viewModelScope.launch {
            isLoading = true
            emailError = null

            try {
                val result = withContext(Dispatchers.IO) {
                    authRepository.checkEmail(email)
                }

                if (result.isSuccessful) {
                    emailAvailable = true
                    onSuccess()
                } else {
                    val error = result.errorBody()?.string() ?: "Error"

                    emailError = if (error.contains("exists", true)) {
                        "Email already exists"
                    } else {
                        error
                    }
                }
            }catch (e: Exception) {
                emailError = "Network error. Please try again."
            } finally {
                isLoading = false
            }
        }
    }


    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String,
        nationality: String
    ) {
        viewModelScope.launch {
            isLoading = true
            generalError = null

            val request = RegisterStep2Request(
                email, password, firstName, lastName, phone, nationality
            )

            val result = withContext(Dispatchers.IO) {
                authRepository.register(request)
            }

            isLoading = false

            if (result.isSuccessful) {
                signUpSuccess = true
            } else {
                generalError = result.errorBody()?.string() ?: "Registration failed"
            }
        }
    }
}