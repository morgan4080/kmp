package com.presta.customer

 expect interface ContactsHandler {
     fun requestReadContactsPermission(callback: (Boolean) -> Unit)
     fun getContacts(callback: (List<Contact>) -> Unit)

}

actual interface ContactsHandler {
    actual fun requestReadContactsPermission(callback: (Boolean) -> Unit)
    actual fun getContacts(callback: (List<Contact>) -> Unit)

}