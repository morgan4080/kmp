package com.presta.customer.ui.components.signLoanForm

import com.presta.customer.Platform
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface SignLoanFormComponent {
    val loanNumber: String
    val amount: Double
    val loanRequestRefId: String
    val memberRefId: String
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    var sign: Boolean
    fun onAuthEvent(event: AuthStore.Intent)
    fun replaceGuarantor(loanRequestRefId: String)
    fun onEvent(event: ApplyLongTermLoansStore.Intent)
    val applyLongTermLoansStore: ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>
    val platform: Platform
    fun onBackNavClicked()
    fun onProductSelected()
    fun onDocumentSigned(
        loanNumber: String,
        amount: Double
    )

}