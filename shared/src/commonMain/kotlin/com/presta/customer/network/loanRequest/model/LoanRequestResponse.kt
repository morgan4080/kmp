package com.presta.customer.network.loanRequest.model

import com.presta.customer.network.registration.model.ApprovalStatus
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
data class LoanRequestResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val requestId: String? = null,
    @EncodeDefault val sessionId: String? = null,
    @EncodeDefault val loanType: LoanType? = null,
    @EncodeDefault val customerRefId: String? = null,
    @EncodeDefault val productRefId: String? = null,
    @EncodeDefault val amount: Int? = null,
    @EncodeDefault val loanPeriod: Int? = null,
    @EncodeDefault val currentTerm: Boolean? = null,
    @EncodeDefault val disbursementMethod: DisbursementMethod? = null,
    @EncodeDefault val disbursementAccountReference: String? = null
)

@Serializable
data class Offer @OptIn(ExperimentalSerializationApi::class) constructor(
    val quotationRefId: String,
    @EncodeDefault val principal: Double? = null,
    @EncodeDefault val interestRate: Double? = null,
    @EncodeDefault val interestAmount: Double? = null,
    @EncodeDefault val deductedInterest: Double? = null,
    @EncodeDefault val totalAmount: Double? = null,
    @EncodeDefault val upfrontFees: Double? = null,
    @EncodeDefault val deductedFees: Double? = null,
    @EncodeDefault val installmentFees: Double? = null,
    @EncodeDefault val totalFees: Double? = null,
    @EncodeDefault val disbursementAmount: Double? = null,
    @EncodeDefault val maturityDate: String? = null,
    @EncodeDefault val installmentCount: Int? = null,
    @EncodeDefault val monthlyInstallment: Double? = null,
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
    val refId: String,
    val productName: String,
    val applicationDate: String,
    val amount: Double,
    @EncodeDefault val approvalStatus: ApprovalStatus? = null,
    val applicationStatus: LoanApplicationStatus,
    val interestAmount: Double,
    val interestRate: Double,
    val totalFees: Double,
    val loanPeriod: String,
    val balanceBF: Double,
    val repaymentAmount: Double
)

@Serializable
data class PrestaBanksResponse(
    val refId: String,
    val name: String,
    val payBillNumber: String
)
@Serializable
data class AllBanksContent(
    val content: List<PrestaBanksResponse>
)
@Serializable
data class PrestaCustomerBanksResponse(
    val refId: String,
    val accountName: String,
    val accountNumber: String,
    val paybillName: String,
    val payBillNumber: String,
)

@Serializable
data class PrestaCustomerBankCreatedResponse(
    val accountName: String,
    val accountNumber: String,
    val paybillName: String,
    val payBillNumber: String,
    val userRefId: String
)

@Serializable
data class PrestaCustomerBankDeletedResponse(
    val accountName: String,
    val accountNumber: String,
    val paybillName: String,
    val payBillNumber: String,
    val userRefId: String
)


