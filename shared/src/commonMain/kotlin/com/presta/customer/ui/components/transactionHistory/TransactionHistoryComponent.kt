package com.presta.customer.ui.components.transactionHistory

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.transactionHistory.store.TransactionHistoryStore
import kotlinx.coroutines.flow.StateFlow

interface TransactionHistoryComponent {
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val transactionHistoryStore: TransactionHistoryStore

    val transactionHistoryState: StateFlow<TransactionHistoryStore.State>

    fun onAuthEvent(event: AuthStore.Intent)

    fun onEvent(event: TransactionHistoryStore.Intent)

    fun onMappingChange(mapping: List<String>)

    fun onBack()
    fun checkAuthenticatedUser()
}