package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse

interface ApplyLongTermLoansStore :
    Store<ApplyLongTermLoansStore.Intent, ApplyLongTermLoansStore.State, Nothing> {
    sealed class Intent {
        data class GetLongTermLoansProducts(val token: String) : Intent()
        data class GetPrestaLongTermProductById(val token: String, val loanRefId: String) : Intent()
        data class GetLongTermLoansProductsCategories(val token: String) : Intent()
        data class GetLongTermLoansProductsSubCategories(val token: String, val parent: String) :
            Intent()

        data class GetLongTermLoansProductsSubCategoriesChildren(
            val token: String,
            val parent: String,
            val child: String
        ) : Intent()

        data class GetClientSettings(val token: String) : Intent()
        data class RequestLongTermLoan(
            val token: String,
            val details: DetailsData,
            val loanProductName: String,
            val loanProductRefId: String,
            val selfCommitment: Double,
            val loanAmount: Double,
            val memberRefId: String,
            val memberNumber: String,
            val witnessRefId: String?,
            val guarantorList: ArrayList<Guarantor>,
        ) : Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaLongTermLoanProducts: PrestaLongTermLoansProductResponse? = null,
        val prestaLongTermLoanProductById: LongTermLoanResponse? = null,
        val prestaLongTermLoanProductsCategories: List<PrestaLongTermLoanCategoriesResponse> = emptyList(),
        val prestaLongTermLoanProductsSubCategories: List<PrestaLongTermLoanSubCategories> = emptyList(),
        val prestaLongTermLoanProductsSubCategoriesChildren: List<PrestaLongTermLoanSubCategoriesChildren> = emptyList(),
        val prestaClientSettings: ClientSettingsResponse? = null,
        val prestaLongTermLoanRequestData: LongTermLoanRequestResponse? = null,
        val memberNo: String = "By Member No",
        val phoneNo: String = "By Phone No",
        val selfGuarantee: String = "Self Guarantee",
    )
}