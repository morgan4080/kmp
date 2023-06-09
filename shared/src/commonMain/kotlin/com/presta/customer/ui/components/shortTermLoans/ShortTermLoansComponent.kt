package com.presta.customer.ui.components.shortTermLoans

import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import kotlinx.coroutines.flow.StateFlow

interface ShortTermLoansComponent {
     val referencedLoanRefId: String
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val shortTermloansStore: ShortTermLoansStore

    val shortTermloansState: StateFlow<ShortTermLoansStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ShortTermLoansStore.Intent)
    val model: Value<Model>
    fun onProductSelected(refId: String,loanName:String,referencedLoanRefId: String)
    fun onConfirmSelected(
        refId: String,
        minAmount: Double,
        maxAmount: Double,
        loanName: String,
        InterestRate: Double,
        loanPeriod: String,
        loanPeriodUnit: String,
        referencedLoanRefId: String
    )

    fun onBackNav()
    data class Model(
        val items: List<String>,
    )

}