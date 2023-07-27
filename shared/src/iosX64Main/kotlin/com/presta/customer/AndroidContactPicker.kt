package com.presta.customer

actual class AndroidContactPicker : ContactPicker {
    actual override fun pickContact(onContactPicked: (name: String?, phoneNumber: String?) -> Unit) {
    }

    actual companion object {
    }

}