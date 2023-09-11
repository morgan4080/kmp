package com.presta.customer.ui.components.addGuarantors

import com.presta.customer.Platform
import com.presta.customer.network.longTermLoans.model.GuarantorDataListing
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import kotlinx.coroutines.flow.StateFlow

interface AddGuarantorsComponent {
    val loanRefId: String
    val loanType: String
    val desiredAmount: Double
    val loanPeriod: Int
    val requiredGuarantors: Int
    val loanCategory: String
    val loanPurposeCategory: String
    val loanPurposeCategoryCode: String
    val loanPurpose: String
    val platform: Platform
    fun onBackNavClicked()
    fun onContinueSelected(
        loanRefId: String,
        loanType: String,
        desiredAmount: Double,
        loanPeriod: Int,
        requiredGuarantors: Int,
        loanCategory: String,
        loanPurpose: String,
        loanPurposeCategory: String,
        businessType: String,
        businessLocation: String,
        kraPin: String,
        employer: String,
        employmentNumber: String,
        grossSalary: Double,
        netSalary:Double,
        memberRefId:String,
        guarantorList: Set<GuarantorDataListing>,
        loanPurposeCategoryCode: String,
        witnessRefId: String,
    )
    fun onNavigateToAddWitness(
        loanRefId: String,
        loanType: String,
        desiredAmount: Double,
        loanPeriod: Int,
        requiredGuarantors: Int,
        loanCategory: String,
        loanPurpose: String,
        loanPurposeCategory: String,
        businessType: String,
        businessLocation: String,
        kraPin: String,
        employer: String,
        employmentNumber: String,
        grossSalary: Double,
        netSalary:Double,
        memberRefId:String,
        guarantorList: Set<GuarantorDataListing>,
        loanPurposeCategoryCode: String,
        witnessRefId: String,
    )
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ApplyLongTermLoansStore.Intent)
    val applyLongTermLoansStore: ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>
    fun onProfileEvent(event: SignHomeStore.Intent)
    val sigHomeStore: SignHomeStore
    val signHomeState: StateFlow<SignHomeStore.State>

}