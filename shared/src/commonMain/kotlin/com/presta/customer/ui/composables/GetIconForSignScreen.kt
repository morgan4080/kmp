package com.presta.customer.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun GetIconForSignScreen(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Outlined.Home
        "Request" -> Icons.Outlined.Assignment
        "Setting" -> Icons.Outlined.Settings
        "Lms" -> Icons.Outlined.Assignment
        else -> Icons.Outlined.Home
    }
}