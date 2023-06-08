package com.presta.customer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import com.presta.customer.di.initKoin
import com.presta.customer.ui.components.root.DefaultRootComponent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        initKoin(
            // add to build configuration, false in prod
            enableNetworkLogs = true
        ) {
            androidContext(applicationContext)
        }

        val root = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory(),
        )

        setContent {
            MainView(root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}