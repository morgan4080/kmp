import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import components.root.RootComponent

actual fun getPlatformName(): String = "Android"

@Composable fun MainView(component: RootComponent) = MaterialTheme {
    AppRootUi(component)
}
