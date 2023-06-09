import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.ScatterPlot
import androidx.compose.material.icons.outlined.SignLanguage
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