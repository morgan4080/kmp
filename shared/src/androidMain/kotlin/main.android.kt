import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import components.root.RootComponent

@Composable fun MainView(component: RootComponent) = MaterialTheme {
    AppRootUi(component)
}