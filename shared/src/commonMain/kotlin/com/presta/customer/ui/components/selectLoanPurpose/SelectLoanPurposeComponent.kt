package com.presta.customer.ui.components.selectLoanPurpose

import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface SelectLoanPurposeComponent {
    val loanRefId: String
    val loanType: String
    val desiredAmount: Double
    val loanPeriod: Int
    val requiredGuarantors: Int
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
        loanPurposeCategoryCode: String,
    )
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ApplyLongTermLoansStore.Intent)
    val applyLongTermLoansStore: ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>
    fun reloadModels()
}