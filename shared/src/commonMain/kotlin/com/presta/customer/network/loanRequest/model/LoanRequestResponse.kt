package com.presta.customer.network.loanRequest.model

import com.presta.customer.network.registration.model.CreditData
import com.presta.customer.network.registration.model.CustomerMetadata
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

enum class LoanApplicationStatus{
    NEWAPPLICATION, INITIATED, INPROGRESS, COMPLETED, FAILED
}

enum class AccountingStatuses {
    NOTACCOUNTED, ACCOUNTED
}
enum class DisbursementStatus {
    INITIATED, INPROGRESS, DISBURSED, NOTDISBURSED, FAILED
}
enum class AppraisalStatus {
    PENDING, APPROVED, DENIED, FAILED
}
@Serializable
data class AccountingResult @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val accountingStatus: AccountingStatuses? = null
)
@Serializable
data class DisbursementResult @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val disbursementStatus: DisbursementStatus? = null,
    val transactionId: String
)
@Serializable
data class AppraisalResult @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val appraisalStatus: AppraisalStatus? = null
)

@Serializable
data class PrestaLoanPollingResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val applicationStatus: LoanApplicationStatus? = null,
    val appraisalResult: AppraisalResult,
    @EncodeDefault val disbursementResult: DisbursementResult? = null,
    @EncodeDefault val accountingResult: AccountingResult? = null
)

@Serializable
enum class DisbursementMethod {
    MOBILEMONEY,
    BANK
}


@Serializable
enum class LoanType {
    _NORMAL_LOAN,
    _TOP_UP
}

@Serializable
data class LoanRequestResponse (
    val requestId: String,
    val sessionId: String,
    val loanType: LoanType,
    val customerRefId: String,
    val productRefId: String,
    val amount: Int,
    val loanPeriod: Int,
    val currentTerm: Boolean,
    val disbursementMethod: String,
    val disbursementAccountReference: String
)

@Serializable
data class Offer @OptIn(ExperimentalSerializationApi::class) constructor(
    val quotationRefId: String,
    @EncodeDefault val principal: Int? = null,
    @EncodeDefault val interestRate: Int? = null,
    @EncodeDefault val interestAmount: Int? = null,
    @EncodeDefault val deductedInterest: Int? = null,
    @EncodeDefault val totalAmount: Int? = null,
    @EncodeDefault val deductedFees: Int? = null,
    @EncodeDefault val installmentFees: Int? = null,
    @EncodeDefault val totalFees: Int? = null,
    @EncodeDefault val disbursementAmount: Int? = null,
    @EncodeDefault val maturityDate: String? = null,
    @EncodeDefault val installmentCount: Int? = null,
    @EncodeDefault val monthlyInstallment: Int? = null,
    @EncodeDefault val firstInstallment: String? = null,
    @EncodeDefault val lastInstallment: String? = null,
    @EncodeDefault val loanFees: List<LoanFee>? = null
)

@Serializable
data class Customer(
    val uuid: String,
    val refId: String,
    val fullName: String,
    val phoneNumber: String,
    val idNumber: String,
    val creditData: CreditData,
    val customerMetadata: CustomerMetadata,
    val appraisalStatus: AppraisalStatus
)
@Serializable
data class LoanProduct(
    val refId: String,
    val name: String,
    val interestRate: Int,
    val interestPeriodCycle: String,
    val isInterestGraduated: Boolean,
    val daysAvailable: String,
    val loanPeriodUnit: String,
    val minTerm: Int,
    val maxTerm: Int,
    val minAmount: Int,
    val maxAmount: Int
)

@Serializable
data class AppraisalRequest(
    val refId: String,
    val tenantId: String,
    val accountName: String,
    val userRefId: String,
    val userUUID: String,
    val fullName: String,
    val applicantPhoneNumber: String,
    val branchRefId: String,
    val branch: String,
    val regionRefId: String,
    val region: String,
    val loanDesc: String,
    val loanDuration: String,
    val loanTerm: String,
    val requestedAmount: Int,
    val loanOffer: Offer,
    val approvedLimit: Int,
    val savings: Int,
    val loanDate: String,
    val productName: String,
    val productRefId: String
)

@Serializable
data class PrestaLoanApplicationStatusResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val tenantId: String? = null,
    @EncodeDefault val id: String? = null,
    @EncodeDefault val loanRefId: String? = null,
    @EncodeDefault val loanDesc: String? = null,
    @EncodeDefault val loanDate: String? = null,
    @EncodeDefault val loanRequest: LoanRequestResponse? = null,
    @EncodeDefault val offer: Offer? = null,
    @EncodeDefault val customer: Customer? = null,
    @EncodeDefault val loanProduct: LoanProduct? = null,
    @EncodeDefault val appraisalRequest: AppraisalRequest? = null,
    @EncodeDefault val applicationStatus: LoanApplicationStatus? = null,
    val appraisalResult: AppraisalResult,
    @EncodeDefault val disbursementResult: DisbursementResult? = null,
    @EncodeDefault val accountingResult: AccountingResult? = null
)