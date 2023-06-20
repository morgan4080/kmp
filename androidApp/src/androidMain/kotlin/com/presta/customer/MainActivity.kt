package com.presta.customer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.presta.customer.di.initKoin
import com.presta.customer.ui.components.root.DefaultRootComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity() {
    var connectivityStatus: SharedStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityStatus = SharedStatus(this)
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
            MainView(root, connectivityStatus)
        }
    }

    override fun onResume() {
        super.onResume()
        connectivityStatus?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}