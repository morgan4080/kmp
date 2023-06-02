package com.presta.customer.ui.components.savings.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse

interface SavingsStore: Store<SavingsStore.Intent, SavingsStore.State, Nothing> {
    sealed class Intent {
        data class GetSavingsBalances(val token: String, val refId: String): Intent()
        data class GetSavingsTransactions(val token: String, val refId: String, val purposeIds: List<String>, val searchTerm: String?): Intent()
        data class GetTransactionsMapping(val token: String): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val savingsTransactions: List<PrestaTransactionHistoryResponse>? = null,
        val transactionMapping: Map<String, String>? = null,
        val savingsBalances: PrestaSavingsBalancesResponse? = null
    )
}