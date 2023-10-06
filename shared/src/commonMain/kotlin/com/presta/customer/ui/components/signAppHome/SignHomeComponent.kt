package com.presta.customer.ui.components.signAppHome

import com.presta.customer.Platform
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import kotlinx.coroutines.flow.StateFlow

interface SignHomeComponent {
    fun onApplyLoanSelected()
    fun guarantorshipRequestsSelected()
    fun favouriteGuarantorsSelected()
    fun witnessRequestSelected()
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: SignHomeStore.Intent)
    val  sigHomeStore: SignHomeStore
    val signHomeState: StateFlow<SignHomeStore.State>
    fun onApplyLongTermLoanEvent(event: ApplyLongTermLoansStore.Intent)
    val applyLongTermLoansStore: ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>
    fun goToLoanRequests(refId: String = "")
    val platform: Platform
    fun logout()

}
