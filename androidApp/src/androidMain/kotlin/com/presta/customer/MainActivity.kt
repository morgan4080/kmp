package com.presta.customer

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import components.root.DefaultRootComponent
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import di.initKoin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKoin(
            // add to build configuration, false in prod
            enableNetworkLogs = true
        ) {
            androidContext(applicationContext)
        }

        val root = DefaultRootComponent(
            componentContext = defaultComponentContext()
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