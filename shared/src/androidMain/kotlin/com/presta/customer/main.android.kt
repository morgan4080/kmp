package com.presta.customer

import androidx.compose.runtime.Composable
import com.presta.customer.ui.AppRootUi
import com.presta.customer.ui.components.root.RootComponent
import com.presta.customer.ui.theme.AppTheme

@Composable fun MainView(component: RootComponent, connectivityStatus: SharedStatus?) = AppTheme {
    AppRootUi(component, connectivityStatus)
}