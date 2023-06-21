package com.presta.customer.ui.components.pendingApprovals

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import kotlinx.coroutines.flow.StateFlow

interface LoanPendingApprovalsComponent {
    val authState: StateFlow<AuthStore.State>
    val authStore: AuthStore
    val modeOfDisbursementStore: ModeOfDisbursementStore
    val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State>
    fun onBackClicked()
    fun onRequestLoanEvent(event: ModeOfDisbursementStore.Intent)
    fun onAuthEvent(event: AuthStore.Intent)
    fun checkAuthenticatedUser()
}