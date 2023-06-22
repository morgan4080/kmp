package com.presta.customer

import android.app.Activity
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
    private var currentActivity: Activity? = null
    private fun getCurrentActivity(): Activity? {
        return currentActivity
    }
    private fun setCurrentActivity(activity: Activity) {
        currentActivity = activity
    }


    private var connectivityStatus: SharedStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCurrentActivity(this)

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
        setCurrentActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (currentActivity == this) {
            currentActivity = null
        }
        stopKoin()
    }
}