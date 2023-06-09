package com.presta.customer.ui.components.transactionHistory.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse

interface TransactionHistoryStore: Store<TransactionHistoryStore.Intent, TransactionHistoryStore.State, Nothing> {
    sealed class Intent {
        data class GetTransactionHistory(val token: String, val refId: String, val purposeIds: List<String>, val searchTerm: String?): Intent()
        data class GetTransactionMapping(val token: String): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val transactionHistory: List<PrestaTransactionHistoryResponse>? = null,
        val transactionMapping: Map<String, String>? = null
    )
}