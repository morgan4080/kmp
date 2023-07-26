package com.presta.customer

expect class AndroidContactsHandler {
    fun requestReadContactsPermission(callback: (Boolean) -> Unit)
    fun getContacts(callback: (List<Contact>) -> Unit)

}