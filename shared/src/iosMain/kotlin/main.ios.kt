import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import components.root.DefaultRootComponent
import di.initKoin
import helpers.LocalSafeArea
import platform.UIKit.UIViewController
import theme.AppTheme

fun MainViewController(
    lifecycle: LifecycleRegistry,
    topSafeArea: Float,
    bottomSafeArea: Float
): UIViewController {
    initKoin()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    val root = DefaultRootComponent(
        componentContext = rootComponentContext
    )
    return ComposeUIViewController {
        val density = LocalDensity.current

        val topSafeAreaDp = with(density) { topSafeArea.toDp() }
        val bottomSafeAreaDp = with(density) { bottomSafeArea.toDp() }
        val safeArea = PaddingValues(top = topSafeAreaDp + 10.dp, bottom = bottomSafeAreaDp)
        // Bind safe area as the value for helpers.getLocalSafeArea
        CompositionLocalProvider(LocalSafeArea provides safeArea) {
            AppTheme {
                AppRootUi(root)
            }
        }
    }
}
