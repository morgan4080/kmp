package com.presta.customer

expect interface ContactPicker {
    actual fun pickContact(onContactPicked: (name: String?, phoneNumber: String?) -> Unit)
}