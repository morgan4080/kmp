package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
data class RoleAction @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val role: String?=null,
    @EncodeDefault val action_id: String?=null,
    @EncodeDefault val action_type: String?=null,
    @EncodeDefault val is_embedded: String?=null,
    @EncodeDefault val recipient_name: String?=null,
    @EncodeDefault val recipient_email: String?=null,
    @EncodeDefault val verify_recipient: String?=null,
    @EncodeDefault val private_notes: String?=null,
    @EncodeDefault val recipient_phonenumber: String?=null,
)