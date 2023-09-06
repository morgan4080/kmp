package com.presta.customer

actual class ContactsUtils actual constructor(context: AndroidContext) {
    actual companion object {
        actual val READ_CONTACTS_PERMISSION_REQUEST_CODE: Int
            get() = TODO("Not yet implemented")
    }

    actual fun getContactList(): List<Contact> {
        TODO("Not yet implemented")
    }

}