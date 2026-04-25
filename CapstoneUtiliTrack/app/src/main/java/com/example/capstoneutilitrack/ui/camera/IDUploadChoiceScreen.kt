package com.example.capstoneutilitrack.ui.camera

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.buttons.AppButton
import com.example.capstoneutilitrack.components.dashedBorder
import com.example.capstoneutilitrack.ui.signup.SignUpViewModel

@Composable
fun IDUploadChoiceScreen(
    navController: NavController,
    viewModel: IDUploadViewModel = hiltViewModel(),
    onSubmitSuccess: () -> Unit
) {
    val dark3 = colorResource(id = R.color.deep_blue)
    val context = LocalContext.current
    val imageUri = viewModel.imageUri
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle?.get<Uri?>("imageUri")) {
        val image = savedStateHandle?.get<Uri?>("imageUri")
        val method = savedStateHandle?.get<String?>("pickedMethod")
        if (image != null && method != null) {
            viewModel.setImage(image)
            viewModel.pickedMethod = method
            savedStateHandle.remove<Uri>("imageUri")
            savedStateHandle.remove<String>("pickedMethod")
        }
    }


    LaunchedEffect(viewModel.uploadSuccess, viewModel.errorMessage) {
        if (viewModel.uploadSuccess) {
            navController.navigate("login") {
                popUpTo("signup") { inclusive = true }
            }
        } else if (viewModel.errorMessage != null) {
            Toast.makeText(context, viewModel.errorMessage ?: "Upload failed.", Toast.LENGTH_SHORT).show()
            viewModel.clearImage()
        }
    }


    val signUpRequest = hiltViewModel<SignUpViewModel>().signUpRequest

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setImage(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Log.d("photo", "pickedMethod = ${viewModel.pickedMethod}")
        if ((viewModel.pickedMethod == "upload" || viewModel.pickedMethod == "camera") && imageUri != null)
        {
            Log.d("photo", "Photo here" )
            IconButton(
                onClick = {
                    viewModel.clearImage()
                    viewModel.pickedMethod = null
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBackIos, contentDescription = "Back", tint = Color.White)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.cream))
        ) {
            Box(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Verify Your Identity",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Upload or take a photo of your ID.",
                        fontSize = 16.sp,
                        color = Color.Black,
                        letterSpacing = 0.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 5.dp, bottom = 15.dp)
                    )

                    Spacer(Modifier.height(32.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(221.dp)
                            .height(148.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .dashedBorder(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(16.dp),
                                strokeWidth = 3.dp,
                                dashLength = 6.dp,
                                gapLength = 6.dp
                            )
                            .clickable {
                                navController.navigate("id_camera1")
                            }
                    ) {
                        Image(
                            painter = if (imageUri != null)
                                rememberAsyncImagePainter(viewModel.imageUri)
                            else
                                painterResource(id = R.drawable.id_card_bg),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(216.dp)
                                .height(138.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(dark3, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "or",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(221.dp)
                            .height(148.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .dashedBorder(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(16.dp),
                                strokeWidth = 3.dp,
                                dashLength = 6.dp,
                                gapLength = 6.dp
                            )
                            .clickable {
                                viewModel.pickedMethod = "upload"
                                imagePickerLauncher.launch("image/*")
                            }
                    ) {
                        Image(
                            painter = if (imageUri != null)
                                rememberAsyncImagePainter(viewModel.imageUri)
                            else
                                painterResource(id = R.drawable.id_card_bg),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(216.dp)
                                .height(138.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(dark3, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FileUpload,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }

                    if (viewModel.imageUri != null) {
                        AppButton(
                            text = "Submit",
                            isLoading = viewModel.isSubmitting,
                            modifier = Modifier.padding(top = 24.dp),
                            onClick = {
                                val file = viewModel.imageUri?.let {
                                    viewModel.uriToFile(it, "passport_upload.jpg", context)
                                } ?: run {
                                    Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
                                    return@AppButton
                                }


                                if (signUpRequest == null) {
                                    return@AppButton
                                }

                                viewModel.submitPassport(
                                    file = file
                                )
                            }
                        )

                    }


                    Spacer(modifier = Modifier.height(26.dp))

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
}

