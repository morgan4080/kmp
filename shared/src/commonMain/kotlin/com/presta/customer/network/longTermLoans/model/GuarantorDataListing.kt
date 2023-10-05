package com.presta.customer.network.longTermLoans.model

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi

@Parcelize
data class GuarantorDataListing @OptIn(ExperimentalSerializationApi::class) constructor(
    val guarantorFirstName: String,
    val guarantorLastName: String,
    val phoneNumber: String,
    val memberNumber: String,
    val amount: String,
    val guarantorRefId: String,
    @EncodeDefault val guarantor1_fosa_account: String = "",
    @EncodeDefault val guarantor2_fosa_account: String = "",
): Parcelable
