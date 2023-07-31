package com.presta.customer

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

    companion object {
        const val READ_CONTACTS_PERMISSION_REQUEST_CODE = 123
    }

    private val contactPicker = AndroidContactPicker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkContactsPermission()

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

    private fun pickContact() {
        contactPicker.pickContact { name, phoneNumber ->
            // Use the 'name' and 'phoneNumber' here as needed
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        contactPicker.handleActivityResult(requestCode, resultCode, data) { name, phoneNumber ->
            // Use the 'name' and 'phoneNumber' here as needed
        }
    }

    private fun checkContactsPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                "android.permission.READ_CONTACTS"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, read contacts
            readContacts()
        } else {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.READ_CONTACTS"),
                READ_CONTACTS_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun readContacts() {
        val contactsUtils = ContactsUtils(this)
        val contactList = contactsUtils.getContactList()

        if (contactList.isNotEmpty()) {
            for (contact in contactList) {
                //println("Contact name: ${contact.name}, Phone number: ${contact.phoneNumber}")
            }
        } else {
            println("No contacts found.")
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_CONTACTS_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, read contacts
                    readContacts()
                } else {
                    // Permission is denied, handle accordingly (e.g., show a message)
                    println("Permission denied. Cannot read contacts.")
                }
            }
            // Handle other permission request results if needed
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