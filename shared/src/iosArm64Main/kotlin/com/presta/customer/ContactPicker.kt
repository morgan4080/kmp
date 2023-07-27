package com.presta.customer

actual interface ContactPicker {
    actual fun pickContact(onContactPicked: (name: String?, phoneNumber: String?) -> Unit)
}