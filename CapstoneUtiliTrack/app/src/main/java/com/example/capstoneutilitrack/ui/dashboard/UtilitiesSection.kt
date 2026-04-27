package com.example.capstoneutilitrack.ui.dashboard


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capstoneutilitrack.model.UtilityModelDto
import com.example.capstoneutilitrack.components.SectionHeader
import com.example.capstoneutilitrack.components.cards.UtilityCard


@Composable
fun UtilitiesSection(
    utilities: List<UtilityModelDto>
) {

    var showAll by remember { mutableStateOf(false) }

    val visibleUtilities =
        if (showAll) utilities else utilities.take(2)

    Column {
        SectionHeader(
            title = "My utilities",
            showAll = showAll,
            onToggle = { showAll = !showAll }
        )

        Spacer(Modifier.height(10.dp))
        if (utilities.isEmpty()) {
            Text(
                "No utilities available",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
            return@Column
        }else {
            visibleUtilities.forEach { utility ->
                UtilityCard(
                    utility = utility
                )

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}