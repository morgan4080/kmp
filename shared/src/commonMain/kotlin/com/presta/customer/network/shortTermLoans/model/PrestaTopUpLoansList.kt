package com.presta.customer.network.shortTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class PrestaTopUpLoansList @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val available: Boolean?=null,
    @EncodeDefault val cycle: String?=null,
    @EncodeDefault val daysAvailable: String?=null,
    @EncodeDefault  val interest: Double?=null,
    @EncodeDefault val loanBalance: Double?=null,
    @EncodeDefault val loanRefId: String?=null,
    @EncodeDefault val maxAmount: Double?=null,
    @EncodeDefault  val maxPeriod: Int?=null,
    @EncodeDefault val minAmount: Double?=null,
    @EncodeDefault val minPeriod: Int?=null,
    @EncodeDefault  val name: String?=null,
    @EncodeDefault  val productRefId: String?=null,
    @EncodeDefault val termUnit: String?=null,
    @EncodeDefault val dueDate: String?=null

)