package com.presta.customer.ui.components.longTermLoanRequestsList

import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import kotlinx.coroutines.flow.StateFlow

interface LongTermLoanRequestsComponent {
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ApplyLongTermLoansStore.Intent)
    val applyLongTermLoansStore: ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>

    fun onProfileEvent(event: SignHomeStore.Intent)
    val sigHomeStore: SignHomeStore
    val signHomeState: StateFlow<SignHomeStore.State>
    fun onSelected(item: String)
    fun navigateToHome()
    fun navigateToReplaceGuarantor(
        loanRequestRefId: String,
        guarantorRefId: String,
        guarantorFirstname: String,
        guarantorLastName: String
    )

    fun navigateToSignLoanForm(
        loanNumber: String,
        amount: Double,
        loanRequestRefId: String,
        memberRefId: String
    )
}