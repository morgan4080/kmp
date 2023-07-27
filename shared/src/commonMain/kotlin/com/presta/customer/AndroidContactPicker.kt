package com.presta.customer

expect class AndroidContactPicker : ContactPicker {
    override fun pickContact(onContactPicked: (name: String?, phoneNumber: String?) -> Unit)

    companion object {
    }
}