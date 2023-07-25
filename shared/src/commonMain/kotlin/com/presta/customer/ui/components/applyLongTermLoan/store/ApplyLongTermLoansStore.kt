package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse

interface ApplyLongTermLoansStore :
    Store<ApplyLongTermLoansStore.Intent, ApplyLongTermLoansStore.State, Nothing> {
    sealed class Intent {
        data class GetLongTermLoansProducts(val token: String) : Intent()
        data class GetPrestaLongTermProductById(val token: String, val loanRefId: String) : Intent()
        data class GetLongTermLoansProductsCategories(val token: String) : Intent()
        data class GetLongTermLoansProductsSubCategories(val token: String, val parent: String) :
            Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaLongTermLoanProducts: PrestaLongTermLoansProductResponse? = null,
        val prestaLongTermLoanProductById: LongTermLoanResponse? = null,
        val prestaLongTermLoanProductsCategories: List<PrestaLongTermLoanCategoriesResponse> = emptyList(),
        val prestaLongTermLoanProductsSubCategories: List<PrestaLongTermLoanSubCategories> = emptyList(),
    )
}