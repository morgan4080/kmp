import androidx.compose.runtime.Composable
import components.root.RootComponent
import theme.AppTheme

@Composable fun MainView(component: RootComponent) = AppTheme {
    AppRootUi(component)
}