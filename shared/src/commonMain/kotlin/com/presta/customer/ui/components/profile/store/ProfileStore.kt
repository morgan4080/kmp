package com.presta.customer.ui.components.profile.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.profile.model.PrestaLoansBalancesResponse
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse

interface ProfileStore: Store<ProfileStore.Intent, ProfileStore.State, Nothing> {

    sealed class Intent{
        data class GetSavingsBalances(val token: String, val refId: String): Intent()
        data class GetLoanBalances(val token: String, val refId: String): Intent()
        data class GetTransactionHistory(val token: String, val refId: String, val purposeIds: List<String>): Intent()
        data class GetTransactionMapping(val token: String): Intent()

    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val savingsBalances: PrestaSavingsBalancesResponse? = null,
        val loansBalances: PrestaLoansBalancesResponse? = null,
        val transactionHistory: List<PrestaTransactionHistoryResponse>? = null,
        val transactionMapping: Map<String, String>? = null
    )
}