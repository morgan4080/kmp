package com.presta.customer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat

actual class ContactsUtils actual constructor(private val context: Any) {
    @SuppressLint("Range")
   actual fun getContactList(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        // Check if the app has the necessary permission
        if (ActivityCompat.checkSelfPermission(
                context as Context,
                "android.permission.READ_CONTACTS"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val contentResolver: ContentResolver = context.contentResolver
            val cursor: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            cursor?.use {
                while (it.moveToNext()) {
                    val name =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contactList.add(Contact(name, phoneNumber))
                }
            }

            cursor?.close()
        } else {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf("android.permission.READ_CONTACTS"),
                READ_CONTACTS_PERMISSION_REQUEST_CODE
            )
        }

        return contactList
    }

    actual companion object {
        actual const val READ_CONTACTS_PERMISSION_REQUEST_CODE = 123
    }
}