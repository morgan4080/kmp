package com.presta.customer.ui.components.shortTermLoans

import com.arkivanov.decompose.value.Value
import com.presta.customer.Platform
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import kotlinx.coroutines.flow.StateFlow

interface ShortTermLoansComponent {
    val platform: Platform
    val referencedLoanRefId: String?
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val shortTermloansStore: ShortTermLoansStore

    val shortTermloansState: StateFlow<ShortTermLoansStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ShortTermLoansStore.Intent)
    fun onProductSelected(refId: String,loanName:String,referencedLoanRefId: String?)
    fun onProceedSelected(
        referencedLoanRefId: String,
        loanRefId: String,
        minAmount: Double,
        maxAmount: Double,
        loanName: String,
        InterestRate: Double,
        maxLoanPeriod: Int,
        loanPeriodUnit: String,
        minLoanPeriod:Int,
        loanRefIds:String,
    )
    fun onBackNav()
    fun reloadModels()

}