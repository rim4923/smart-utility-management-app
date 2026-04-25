package com.example.capstoneutilitrack.ui.signup

import android.app.DatePickerDialog
import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.buttons.AppButton
import com.example.capstoneutilitrack.components.AppTextField
import com.hbb20.CountryCodePicker
import java.util.Calendar

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignUpScreenTwo(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit
) {
    LaunchedEffect(viewModel.signUpSuccess) {
        if (viewModel.signUpSuccess) {
            onSignUpSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.cream).copy(0.7f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Complete your profile",
                    fontSize = 27.sp,
                    color = colorResource(R.color.cocoa),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Let’s get to know you better.",
                    fontSize = 16.sp,
                    color = colorResource(R.color.cocoa).copy(alpha = 0.7f),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 5.dp, bottom = 15.dp)
                )



                AppTextField(
                    value = viewModel.firstName,
                    onValueChange = {
                        viewModel.firstName = it
                        viewModel.firstNameError = null
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                        )
                    },
                    label = "First Name",
                    isError = viewModel.firstNameError != null,
                    errorMessage = viewModel.firstNameError
                )

                Spacer(modifier = Modifier.height(20.dp))

                AppTextField(
                    value = viewModel.lastName,
                    onValueChange = {
                        viewModel.lastName = it
                        viewModel.lastNameError = null
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                        )
                    },
                    label = "Last Name",
                    isError = viewModel.lastNameError != null,
                    errorMessage = viewModel.lastNameError
                )

                Spacer(modifier = Modifier.height(20.dp))


                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(
                                width = 1.dp,
                                color = if (viewModel.phoneNumberError != null) Color.Red else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(20)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AndroidView(
                                factory = { context ->
                                    CountryCodePicker(context).apply {
                                        setDefaultCountryUsingNameCode("LB")
                                        resetToDefaultCountry()
                                        setOnCountryChangeListener {
                                            viewModel.countryCode = selectedCountryCodeWithPlus
                                        }
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            BasicTextField(
                                value = viewModel.phoneNumber,
                                onValueChange = {
                                    viewModel.phoneNumber = it
                                    viewModel.phoneNumberError = null
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                decorationBox = { innerTextField ->
                                    if (viewModel.phoneNumber.isEmpty()) {
                                        Text(
                                            text = "Phone number",
                                            color = Color.Gray
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }

                    if (viewModel.phoneNumberError != null) {
                        Text(
                            text = viewModel.phoneNumberError ?: "",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val nationalityList = listOf(
                    "Afghan", "Albanian", "Algerian", "American", "Andorran", "Angolan", "Argentine", "Armenian",
                    "Australian", "Austrian", "Azerbaijani", "Bahamian", "Bahraini", "Bangladeshi", "Barbadian",
                    "Belarusian", "Belgian", "Belizean", "Beninese", "Bhutanese", "Bolivian", "Bosnian",
                    "Brazilian", "British", "Bruneian", "Bulgarian", "Burkinabé", "Burundian", "Cambodian",
                    "Cameroonian", "Canadian", "Cape Verdean", "Central African", "Chadian", "Chilean", "Chinese",
                    "Colombian", "Comoran", "Congolese", "Costa Rican", "Croatian", "Cuban", "Cypriot", "Czech",
                    "Danish", "Djiboutian", "Dominican", "Dutch", "Ecuadorian", "Egyptian", "Emirati", "English",
                    "Equatoguinean", "Eritrean", "Estonian", "Eswatini", "Ethiopian", "Fijian", "Finnish", "French",
                    "Gabonese", "Gambian", "Georgian", "German", "Ghanaian", "Greek", "Grenadian", "Guatemalan",
                    "Guinean", "Guyanese", "Haitian", "Honduran", "Hungarian", "Icelander", "Indian", "Indonesian",
                    "Iranian", "Iraqi", "Irish", "Italian", "Jamaican", "Japanese", "Jordanian",
                    "Kazakhstani", "Kenyan", "Kuwaiti", "Kyrgyz", "Lao", "Latvian", "Lebanese", "Liberian",
                    "Libyan", "Lithuanian", "Luxembourger", "Macedonian", "Malagasy", "Malawian", "Malaysian",
                    "Maldivian", "Malian", "Maltese", "Marshallese", "Mauritanian", "Mauritian", "Mexican",
                    "Micronesian", "Moldovan", "Monegasque", "Mongolian", "Montenegrin", "Moroccan", "Mozambican",
                    "Myanmar", "Namibian", "Nepali", "New Zealander", "Nicaraguan", "Nigerian", "North Korean",
                    "Norwegian", "Omani", "Pakistani", "Palestinian", "Panamanian", "Papua New Guinean",
                    "Paraguayan", "Peruvian", "Philippine", "Polish", "Portuguese", "Qatari", "Romanian", "Russian",
                    "Rwandan", "Salvadoran", "Sammarinese", "Samoan", "Saudi", "Scottish", "Senegalese", "Serbian",
                    "Seychellois", "Sierra Leonean", "Singaporean", "Slovak", "Slovenian", "Somali", "South African",
                    "South Korean", "South Sudanese", "Spanish", "Sri Lankan", "Sudanese", "Surinamese", "Swazi",
                    "Swedish", "Swiss", "Syrian", "Taiwanese", "Tajikistani", "Tanzanian", "Thai", "Togolese",
                    "Tongan", "Trinidadian", "Tunisian", "Turkish", "Turkmen", "Tuvaluan", "Ugandan", "Ukrainian",
                    "Uruguayan", "Uzbekistani", "Vanuatuan", "Vatican", "Venezuelan", "Vietnamese", "Welsh",
                    "Yemeni", "Zambian", "Zimbabwean"
                )

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {val interactionSource = remember { MutableInteractionSource() }

                    OutlinedTextField(
                        value = viewModel.nationality,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        label = { Text("Nationality", fontSize = 16.sp) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                            )
                        },
                        shape = RoundedCornerShape(25),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .height(60.dp),
                        interactionSource = interactionSource,
                        isError = viewModel.nationalityError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (viewModel.nationalityError != null) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            unfocusedBorderColor = if (viewModel.nationalityError != null) Color.Red else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedLabelColor = if (viewModel.nationalityError != null) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            unfocusedLabelColor = if (viewModel.nationalityError != null) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            cursorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        nationalityList.forEach { nationality ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        nationality,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    viewModel.nationality = nationality
                                    viewModel.nationalityError = null
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (viewModel.nationalityError != null) {
                    Text(
                        text = viewModel.nationalityError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                AppButton(
                    text = "Continue",
                    isLoading = viewModel.isLoading,
                    onClick = {
                        viewModel.onSignUpClick(
                            onSuccess = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("signUpRequest", viewModel.signUpRequest)
                                onSignUpSuccess()
                            },
                            onError = {}
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(16.dp))

                viewModel.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                )
            }
        }
    }

}


@Composable
fun DatePickerField(
    context: Context,
    dateOfBirth: String,
    onDateSelected: (String) -> Unit,
    errorMessage: String?
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val themedContext = ContextThemeWrapper(context,R.style.CustomDatePicker)
    val datePicker = DatePickerDialog(
        themedContext, { _, mYear, mMonth, mDayOfMonth ->
            val selectedDate = "$mDayOfMonth-${mMonth + 1}-$mYear"
            onDateSelected(selectedDate)
        }, year, month, day
    )

    Column {
        val interactionSource = remember { MutableInteractionSource() }

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            isError = errorMessage != null,
            label = { Text("DOB") },
            trailingIcon = {
                IconButton(onClick = { datePicker.show() }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(25),
            interactionSource = interactionSource,
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (errorMessage != null) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                unfocusedBorderColor = if (errorMessage != null) Color.Red else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                focusedLabelColor = if (errorMessage != null) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                unfocusedLabelColor = if (errorMessage != null) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                cursorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )


        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}