package com.example.capstoneutilitrack.ui.camera

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneutilitrack.components.buttons.AppButton
@Composable
fun IDCameraScreen(
    navController: NavController,
    viewModel: IDUploadViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val permissionGranted = remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!permissionGranted.value) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission is required.")
        }
        return
    }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    val photoUri = photoUriString?.let { Uri.parse(it) }


    fun takePicture(onCaptured: (Uri) -> Unit) {
        val file = File(context.filesDir, "passport.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    Log.d("Camera", "Saved to: ${uri.path}")
                    onCaptured(uri)
                }


                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Failed to take photo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (photoUri == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AndroidView(factory = { ctx ->
                    val pv = PreviewView(ctx).also { previewView = it }
                    val preview = Preview.Builder().build().apply {
                        setSurfaceProvider(pv.surfaceProvider)
                    }
                    val capture = ImageCapture.Builder().build()
                    imageCapture = capture

                    val selector = CameraSelector.DEFAULT_BACK_CAMERA
                    val provider = cameraProviderFuture.get()
                    provider.unbindAll()
                    provider.bindToLifecycle(lifecycleOwner, selector, preview, capture)

                    pv
                }, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth())

                AppButton(
                    text = "Take Passport Photo",
                    isLoading = false,
                    modifier = Modifier
                        .padding(16.dp),
                    onClick = {
                        takePicture { uri ->
                            photoUriString = uri.toString()
                            viewModel.setImage(uri)
                            viewModel.pickedMethod = "camera"
                        }

                    }
                )
            }

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppButton(
                    text = "Retake",
                    isLoading = false,
                    modifier = Modifier.weight(1f),
                    onClick = { photoUriString = null }
                )

                AppButton(
                    text = "Continue",
                    isLoading = false,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("imageUri", photoUri)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("pickedMethod", "camera")

                        navController.popBackStack()
                    }

                )
            }
        }
    }
}
