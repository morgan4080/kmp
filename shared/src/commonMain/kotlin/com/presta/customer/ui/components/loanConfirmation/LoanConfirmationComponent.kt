package com.presta.customer.ui.components.loanConfirmation

import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import kotlinx.coroutines.flow.StateFlow

interface LoanConfirmationComponent {
    val refId: String
    val amount: Double
    val loanPeriod: Int
    val loanInterest: String
    val loanName: String
    val loanPeriodUnit: String
    val loanOperation: String
    val loanType: LoanType
    val referencedLoanRefId: String?
    val currentTerm: Boolean

    val modeOfDisbursement: DisbursementMethod
    val accountRefId: String?

    fun onRequestLoanEvent(event: ModeOfDisbursementStore.Intent)
    val modeOfDisbursementStore: ModeOfDisbursementStore
    val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State>
    fun onConfirmSelected()

    fun onBackNavSelected()
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val shortTermloansStore: ShortTermLoansStore

    val shortTermloansState: StateFlow<ShortTermLoansStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ShortTermLoansStore.Intent)

}