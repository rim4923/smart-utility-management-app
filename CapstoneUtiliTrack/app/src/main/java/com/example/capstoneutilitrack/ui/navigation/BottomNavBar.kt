package com.example.capstoneutilitrack.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.Screen

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Dashboard", Icons.Outlined.Home, Screen.Dashboard.route),
        BottomNavItem("Bills", Icons.Outlined.Payments, Screen.Bills.route),
        BottomNavItem("Profile", Icons.Outlined.Person, Screen.Profile.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dark3 = colorResource(id = R.color.steel_blue)

    Divider(color = Color.Gray, thickness = 0.96f.dp)
    NavigationBar(
        containerColor = Color.Transparent,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label, tint = if (selected) dark3 else Color.Gray) },
                label = { Text(item.label, color = if (selected) colorResource(R.color.deep_blue) else colorResource(R.color.burgundy)) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}
