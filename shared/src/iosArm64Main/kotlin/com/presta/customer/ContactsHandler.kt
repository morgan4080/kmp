package com.presta.customer

actual interface ContactsHandler {
    actual fun getContacts(): List<Contact>
}