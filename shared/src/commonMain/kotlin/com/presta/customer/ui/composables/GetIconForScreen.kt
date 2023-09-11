import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun GetIconForScreen(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Outlined.Home
        "Loans" -> Icons.Outlined.Wallet
        "Savings" -> Icons.Outlined.Savings
        "Sign" -> Icons.Outlined.Assignment
        else -> Icons.Outlined.Home
    }
}