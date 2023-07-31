package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class Response @OptIn(ExperimentalSerializationApi::class) constructor(
    val organizationName: String,
    val requireWitness: Boolean,
    val allowZeroGuarantors: Boolean,
    val allowSelfGuarantee: Boolean,
    val isGuaranteedAmountShared: Boolean,
    val useEmbeddedURL: Boolean,
    val containsAttachments: Boolean,
    val organizationAlias: String,
    val organizationEmail: String,
    val supportEmail: String,
    val coreBankingIntegration: String,
    val notificationProvider: String,
    val identifierType: String,
    val parallelLoans: Boolean,
    @EncodeDefault val details: Details? = null,
    )