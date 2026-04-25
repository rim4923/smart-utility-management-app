package com.example.capstoneutilitrack.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import coil.request.CachePolicy

@Composable
fun ProfileHeaderCard(
    state: ProfileUiState,
    onPickImage: () -> Unit,
    onEditClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.card)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(state.profileImage)
                            .crossfade(true)
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_profile_placeholder),
                        error = painterResource(R.drawable.ic_profile_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )

                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onPickImage() }
                            .padding(4.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    state.fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color= colorResource(R.color.burgundy)
                )

                Text(
                    state.phone,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )

                Text(
                    state.email,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Text(
                "Edit",
                fontSize = 13.sp,
                color= colorResource(R.color.burgundy),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFDAD2D0))
                    .clickable(enabled = !state.isLoading) { onEditClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}