package com.presta.customer.ui.components.modeofDisbursement

import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import kotlinx.coroutines.flow.StateFlow

interface ModeOfDisbursementComponent {
    val refId: String
    val amount: Double
    val fees:Double
    val loanPeriod : String
    val loanType:LoanType
    val referencedLoanRefId: String
     val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    val modeOfDisbursementStore: ModeOfDisbursementStore
    val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State>
     fun onAuthEvent(event: AuthStore.Intent)
     fun onRequestLoanEvent(event: ModeOfDisbursementStore.Intent)
    fun onMpesaSelected()
    fun onBankSelected()
    fun onBackNavSelected()
    fun successFulTransaction()
    data class Model(
        val items: List<String>,
    )

}