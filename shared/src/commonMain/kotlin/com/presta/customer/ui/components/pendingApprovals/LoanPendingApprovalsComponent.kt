package com.presta.customer.ui.components.pendingApprovals

import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface LoanPendingApprovalsComponent {
    val authState: StateFlow<AuthStore.State>
    val authStore: AuthStore
    fun onBackClicked()
}