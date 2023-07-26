package com.presta.customer

import android.app.Activity
import android.content.pm.PackageManager
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
import org.koin.core.error.KoinAppAlreadyStartedException

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

        try {
            initKoin(
                // add to build configuration, false in prod
                enableNetworkLogs = true
            ) {
                androidContext(applicationContext)
            }
        } catch (e: KoinAppAlreadyStartedException) {
            e.printStackTrace()
        }

        val root = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory(),
        )

        setContent {
            MainView(root, connectivityStatus)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode ==  123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The user granted the permission, you can now access contacts
                // Call your contacts handler and get contacts
            } else {
                // The user denied the permission, handle this situation as needed
                // For example, show a message explaining why contacts cannot be accessed
            }
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