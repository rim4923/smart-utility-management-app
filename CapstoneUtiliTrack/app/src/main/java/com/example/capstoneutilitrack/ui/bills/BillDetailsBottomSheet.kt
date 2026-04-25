package com.example.capstoneutilitrack.ui.bills

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.billDetails.BillHeaderCard
import com.example.capstoneutilitrack.components.billDetails.BillInfoSection
import com.example.capstoneutilitrack.components.billDetails.ConsumptionChartSection
import com.example.capstoneutilitrack.components.billDetails.ProviderSection
import com.example.capstoneutilitrack.model.BillDetailsDto
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.model.UtilityModelDto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetailsBottomSheet(
    bill: BillDetailsDto,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(R.color.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Bill Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(colorResource(R.color.cream))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    BillHeaderCard(bill)

                    Spacer(Modifier.height(12.dp))

                    BillInfoSection(bill)

                    Spacer(Modifier.height(8.dp))

                    ConsumptionChartSection(bill)

                    Spacer(Modifier.height(12.dp))

                    ProviderSection(bill)
                }
            }
        }
    }
}