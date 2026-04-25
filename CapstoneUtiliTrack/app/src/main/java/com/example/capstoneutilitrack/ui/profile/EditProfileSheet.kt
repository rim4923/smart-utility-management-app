package com.example.capstoneutilitrack.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^[0-9]{7,15}$"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(
    state: ProfileUiState,
    isLoading: Boolean,
    onSave: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val parts = state.fullName.trim().split(" ")

    var firstName by remember { mutableStateOf(parts.firstOrNull() ?: "") }
    var lastName by remember {
        mutableStateOf(
            if (parts.size > 1) parts.drop(1).joinToString(" ")
            else ""
        )
    }

    var email by remember { mutableStateOf(state.email) }
    var phone by remember { mutableStateOf(state.phone) }

    val isValid =
        firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                isValidEmail(email) &&
                isValidPhone(phone)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(R.color.card),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Edit Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            ProfileInput(
                firstName,
                "First Name",
                isError = firstName.isBlank(),
                errorText = if (firstName.isBlank()) "Required" else null
            ) { firstName = it }

            Spacer(Modifier.height(12.dp))

            ProfileInput(
                lastName,
                "Last Name"
            ) { lastName = it }
            Spacer(Modifier.height(12.dp))

            ProfileInput(
                email,
                "Email",
                isError = email.isNotEmpty() && !isValidEmail(email),
                errorText = if (email.isNotEmpty() && !isValidEmail(email)) "Invalid email" else null
            ) { email = it }

            Spacer(Modifier.height(12.dp))

            ProfileInput(
                phone,
                "Phone",
                isError = phone.isNotEmpty() && !isValidPhone(phone),
                errorText = if (phone.isNotEmpty() && !isValidPhone(phone)) "Invalid phone number" else null
            ) {  phone = it.filter { char -> char.isDigit() } }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(firstName, lastName, email, phone)
                },
                enabled = isValid && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF8FA8C0),
                                Color(0xFF48596B)
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .height(48.dp),
                shape = RoundedCornerShape(50)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Save")
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun ProfileInput(
    value: String,
    label: String,
    isError: Boolean = false,
    errorText: String? = null,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF5B7287),
            unfocusedBorderColor = Color.LightGray
        )
    )
    if (isError && errorText != null) {
        Text(
            errorText,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
        )
    }
}