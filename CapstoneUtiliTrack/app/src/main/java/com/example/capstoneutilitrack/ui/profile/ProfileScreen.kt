package com.example.capstoneutilitrack.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar
import java.io.File

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            Content(
                navController = navController,
                viewModel = viewModel
            )
        }
        BottomNavBar(navController = navController)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.profileState
    state.error?.let {
        Text(
            text = it,
            color = Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val context = LocalContext.current

        var showImagePicker by remember { mutableStateOf(false) }

        val galleryLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let { viewModel.uploadImage(it, context) }
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            bitmap?.let {
                val uri = saveBitmapToCache(context, it)
                viewModel.uploadImage(uri, context)
            }
        }
        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                cameraLauncher.launch(null)
            }
        }

        if (viewModel.showEditSheet) {
            EditProfileSheet(
                state = state,
                isLoading = state.isLoading,
                onSave = { f, l, e, p ->
                    viewModel.updateProfile(f, l, e, p)
                },
                onDismiss = { viewModel.closeEdit() }
            )
        }

        if (showImagePicker) {
            ModalBottomSheet(
                onDismissRequest = { showImagePicker = false },
                containerColor = colorResource(R.color.creamex)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text("Change Profile Picture",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally))

                    Spacer(Modifier.height(16.dp))

                    ProfileMenuItem("Take Photo", Icons.Default.CameraAlt) {
                        showImagePicker = false
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }

                    ProfileMenuItem("Choose from Gallery", Icons.Default.Image) {
                        showImagePicker = false
                        galleryLauncher.launch("image/*")
                    }
                }
            }
        }

        Column {
            ProfileTopBar()

            Spacer(Modifier.height(16.dp))

            ProfileHeaderCard(
                state = state,
                onPickImage = { showImagePicker = true },
                onEditClick = { viewModel.openEdit() }
            )

            Spacer(Modifier.height(20.dp))

            ProfileMenuItem(
                title = "Billing & Payments",
                icon = Icons.Default.CreditCard,
                onClick = {
                    navController.navigate("cards")
                }
            )

            ProfileMenuItem(
                title = "Forgot Password",
                icon = Icons.Default.Lock,
                onClick = {
                    navController.navigate("forgot_password")
                }
            )
            Spacer(Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.card))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp, end = 17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Notifications", modifier = Modifier.weight(1f),
                        color = colorResource(R.color.burgundy),
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = state.notificationsEnabled,
                        modifier = Modifier.scale(0.7f),
                        onCheckedChange = { viewModel.toggleNotifications(it) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        ProfileMenuItem("Log Out", Icons.Default.ExitToApp,
            textColor = Color.Red) {
            viewModel.logout(context) {
                navController.navigate("authentication") {
                    popUpTo(0)
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(text = label)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = value, color = Color.Gray)
        }
    }
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return file.toUri()
}