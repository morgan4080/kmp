package com.presta.customer.network.longTermLoans.tst

data class RoleAction(
    val action_id: String,
    val action_type: String,
    val is_embedded: String,
    val private_notes: String,
    val recipient_email: String,
    val recipient_name: String,
    val recipient_phonenumber: String,
    val role: String,
    val verify_recipient: String
)