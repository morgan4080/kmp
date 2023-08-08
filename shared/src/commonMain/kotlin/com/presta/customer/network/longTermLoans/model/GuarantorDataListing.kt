package com.presta.customer.network.longTermLoans.model

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class GuarantorDataListing(
    val guarantorName: String,
    val phoneNumber: String,
    val memberNumber: String,
    val amount: String,
    val guarantorRefId: String,
): Parcelable
