package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class LoanQuotationResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val refId: String? = null,
    @EncodeDefault val quotationDate: String? = null,
    @EncodeDefault val fullName: String? = null,
    @EncodeDefault val phoneNumber: String? = null,
    @EncodeDefault val productName: String? = null,
    @EncodeDefault val loanPeriod: Double? = null,
    @EncodeDefault val principal: Double? = null,
    @EncodeDefault val interestRate: Double? = null,
    @EncodeDefault val interestAmount: Double? = null,
    @EncodeDefault val deductedInterest: Double? = null,
    @EncodeDefault val upfrontFees: Double? = null,//
    @EncodeDefault val deductedFees: Double? = null, //
    @EncodeDefault val installmentFees: Double? = null,//
    @EncodeDefault val totalFees: Double? = null,
    @EncodeDefault val totalAmount: Double? = null,
    @EncodeDefault val disbursementAmount: Double? = null,
    @EncodeDefault val maturityDate: String? = null,
    val loanFeeList: List<LoanFee>? = null,
    @EncodeDefault val installmentCount: Int? = null,
    @EncodeDefault val monthlyInstallment: Double? = null,
    @EncodeDefault val firstInstallment: String? = null,
    @EncodeDefault val lastInstallment: String? = null,
    @EncodeDefault val balanceBroughtForward: Double? = null,
    @EncodeDefault val expiredQuotationDate: String? = null,
    @EncodeDefault val loanFeeRefIds: String? = null
)