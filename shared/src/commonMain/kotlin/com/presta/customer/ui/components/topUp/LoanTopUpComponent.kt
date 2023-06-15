package com.presta.customer.ui.components.topUp

import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import kotlinx.coroutines.flow.StateFlow

interface LoanTopUpComponent {

    val maxAmount: Double
    val minAmount: String
    val loanRefId: String
    val loanName:  String
    val interestRate: Double
    val loanPeriod: String
    val loanPeriodUnit: String
    val loanOperation: String
    val referencedLoanRefId: String
    fun onProceedSelected(
        refId: String,
        amount: Double,
        loanPeriod: String,
        loanType:LoanType,
        loanName: String,
        interest: Double,
        loanPeriodUnit: String,
        referencedLoanRefId:String,
        currentTerm:Boolean
    )

    fun onBackNavSelected()
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val shortTermloansStore: ShortTermLoansStore

    val shortTermloansState: StateFlow<ShortTermLoansStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ShortTermLoansStore.Intent)
}