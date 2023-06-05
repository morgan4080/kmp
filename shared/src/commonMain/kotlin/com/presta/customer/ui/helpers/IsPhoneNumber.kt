package com.presta.customer.ui.helpers


internal val pattern = Regex("^[^0]\\d{3}?\\d{3}?\\d{2}$")

fun isPhoneNumber(phoneNumber: String) : Boolean {
    return phoneNumber.matches(pattern)
}