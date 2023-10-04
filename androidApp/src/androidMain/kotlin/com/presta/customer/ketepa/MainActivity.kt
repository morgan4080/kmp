package com.presta.customer.ketepa

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.presta.customer.MainView
import com.presta.customer.Platform
import com.presta.customer.SharedStatus
import com.presta.customer.di.initKoin
import com.presta.customer.ui.components.root.DefaultRootComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.core.error.KoinAppAlreadyStartedException

class MainActivity : AppCompatActivity() {
    private var platform: Platform? = null
    private var currentActivity: Activity? = null
    private fun setCurrentActivity(activity: Activity) {
        currentActivity = activity
    }

    private var connectivityStatus: SharedStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCurrentActivity(this)

        connectivityStatus = SharedStatus(this)
        platform = Platform(this)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val defaultComponentCtx = defaultComponentContext()

        try {
            val koinApplication = initKoin(
                // add to build configuration, false in prod
                componentContext = defaultComponentCtx,
                storeFactory = DefaultStoreFactory(),
                enableNetworkLogs = true,
                platform = platform,
            )
            koinApplication.androidContext(applicationContext)
        } catch (e: KoinAppAlreadyStartedException) {
            e.printStackTrace()
        }

        val root = DefaultRootComponent(
            componentContext = defaultComponentCtx,
            storeFactory = DefaultStoreFactory()
        )

        setContent {
            MainView(root, connectivityStatus)
        }
    }

    override fun onResume() {
        connectivityStatus?.start()
        setCurrentActivity(this)
        super.onResume()
    }

    override fun onDestroy() {
        if (currentActivity == this) {
            currentActivity = null
        }
        stopKoin()
        super.onDestroy()
    }
}