package com.presta.customer.ui.components.shortTermLoans.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse

interface ShortTermLoansStore: Store<ShortTermLoansStore.Intent,ShortTermLoansStore.State,Nothing> {

    sealed class Intent{
        data class GetPrestaShortTermProductList(val token: String, val refId: String): Intent()
        data class GetPrestaShortTermTopUpList(val token: String, val session_id:String, val refId: String): Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaShortTermProductList: List<PrestaShortTermProductsListResponse> = emptyList(),
        val  prestaShortTermTopUpList: PrestaShortTermTopUpListResponse? = null
    )
}