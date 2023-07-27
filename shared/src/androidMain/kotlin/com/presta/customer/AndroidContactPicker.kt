package com.presta.customer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

actual class AndroidContactPicker(private val activity: Activity) : ContactPicker {
    actual override fun pickContact(onContactPicked: (name: String?, phoneNumber: String?) -> Unit) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        activity.startActivityForResult(intent, PICK_CONTACT_REQUEST_CODE)
    }
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, onContactPicked: (name: String?, phoneNumber: String?) -> Unit) {
        if (requestCode == PICK_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the contact URI
            val contactUri = data?.data
            if (contactUri != null) {
                // Use the contact URI to retrieve the contact's name and phone number
                val (name, phoneNumber) = getContactInfo(contactUri)
                onContactPicked(name, phoneNumber)
            } else {
                onContactPicked(null, null)
            }
        } else {
            onContactPicked(null, null)
        }
    }
    private fun getContactInfo(contactUri: Uri): Pair<String?, String?> {
        // Use the contactUri to query the contact details and retrieve the name and phone number
        // Implement this part to extract the name and phone number based on the selected contactUri.
        // For example:
        val name = "Dennis"
        val phoneNumber = "123-456-7890"
        return Pair(name, phoneNumber)
    }
    actual companion object {
        private const val PICK_CONTACT_REQUEST_CODE = 1001
    }
}