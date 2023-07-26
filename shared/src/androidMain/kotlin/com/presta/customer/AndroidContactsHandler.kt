package com.presta.customer

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

actual class AndroidContactsHandler(private val activity: Activity) : ContactsHandler {

    private val READ_CONTACTS_PERMISSION_REQUEST = 123

    actual override fun requestReadContactsPermission(callback: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            callback(true) // Permission is already granted
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST
            )
        }
    }

    actual override fun getContacts(callback: (List<Contact>) -> Unit) {
        val contactsList = mutableListOf<Contact>()
        // Use Android's native contacts API to fetch contacts here
        // ...
        callback(contactsList)
    }
}