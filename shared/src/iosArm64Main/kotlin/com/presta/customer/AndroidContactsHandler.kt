package com.presta.customer

actual class AndroidContactsHandler {
    actual fun requestReadContactsPermission(callback: (Boolean) -> Unit) {
    }

    actual fun getContacts(callback: (List<Contact>) -> Unit) {
    }

}