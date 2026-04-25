package com.example.capstoneutilitrack.components.billDetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capstoneutilitrack.model.UtilityModelDto
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.BillDetailsDto

@Composable
fun ProviderSection(bill: BillDetailsDto) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.card))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = bill.type.outlinedIcon(),
                tint = bill.type.iconColor(),
                contentDescription = null
            )

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = bill.providerName)
                Text(text = bill.providerPhone, fontSize = 13.sp, color = Color.Gray)
                val context = LocalContext.current

                Row(
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://${bill.providerWebsite}"))
                        context.startActivity(intent)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bill.providerWebsite,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

        }
    }
}