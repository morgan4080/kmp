package com.presta.customer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.moriatsushi.insetsx.WindowInsetsUIViewController
import com.presta.customer.di.initKoin
import com.presta.customer.ui.AppRootUi
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.AppTheme
import platform.UIKit.*

@Suppress("unused", "FunctionName")
fun MainViewController(
    lifecycle: LifecycleRegistry,
    topSafeArea: Float,
    bottomSafeArea: Float
): UIViewController {
    initKoin()
    val root = DefaultRootComponent(
        componentContext = DefaultComponentContext(lifecycle = lifecycle),
        storeFactory = DefaultStoreFactory(),
    )
    return WindowInsetsUIViewController {
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


