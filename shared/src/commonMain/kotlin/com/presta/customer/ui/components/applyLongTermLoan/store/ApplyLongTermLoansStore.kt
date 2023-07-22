package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse

interface ApplyLongTermLoansStore : Store<ApplyLongTermLoansStore.Intent, ApplyLongTermLoansStore.State, Nothing> {
    sealed class Intent {
        data class GetLongTermLoansProducts(val token: String): Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaLongTermLoanProducts: PrestaLongTermLoansProductResponse? = null
    )
}