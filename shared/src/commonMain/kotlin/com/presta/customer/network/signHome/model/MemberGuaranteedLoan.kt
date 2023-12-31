package com.presta.customer.network.signHome.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberGuaranteedLoan(
    val applicantCommittedShares: Int,
    val committedAmount: Int,
    val dateCreated: String,
    val fullName: String,
    val guarantorMemberNo: List<String>,
    val guarantorRequired: Int,
    val guarantorsShares: Int,
    val lastUpdated: String,
    val loanAccountNum: String,
    val loanAmount: Int,
    val loanBalance: Int,
    val loanDate: String,
    val loanGuarantorMembers: List<LoanGuarantorMember>,
    val loanNumber: String,
    val loanPerformingStatus: String,
    val loanProductName: String,
    val loanProductRefId: String,
    val loanStatus: String,
    val loanTypeCode: String,
    val loanTypeName: String,
    val memberFirstName: String,
    val memberLastName: String,
    val memberNumber: String,
    val memberRefId: String,
    val outstandingAmount: Int,
    val phoneNumber: String,
    val refId: String,
    val sharesPerGuarantor: Int,
    val term: Int,
    val termUnit: String
)