package com.presta.customer.ui.components.topUp

import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import kotlinx.coroutines.flow.StateFlow

interface LoanTopUpComponent {

    val maxAmount: Double
    val minAmount: String
    val loanRefId: String
    val loanName:  String
    val interestRate: Double
     val maxLoanPeriod: Int
    val loanPeriodUnit: String
    val loanOperation: String
    val referencedLoanRefId: String
    val minLoanPeriod:Int
    val loanRefIds:String

    fun onProceedSelected(
        referencedLoanRefId:String,
        loanRefId: String,
        amount: Double,
        maxLoanPeriod: Int,
        loanType:LoanType,
        loanName: String,
        interest: Double,
        loanPeriodUnit: String,
        currentTerm:Boolean,
        minLoanPeriod:Int
    )

    fun onBackNavSelected()
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    val shortTermloansStore: ShortTermLoansStore

    val shortTermloansState: StateFlow<ShortTermLoansStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ShortTermLoansStore.Intent)
    fun onProfileEvent(event: ProfileStore.Intent)
    val profileStore: ProfileStore
    val profileState: StateFlow<ProfileStore.State>

}