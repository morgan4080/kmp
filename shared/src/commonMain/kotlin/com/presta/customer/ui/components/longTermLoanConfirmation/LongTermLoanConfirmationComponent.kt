package com.presta.customer.ui.components.longTermLoanConfirmation

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import kotlinx.coroutines.flow.StateFlow

interface LongTermLoanConfirmationComponent {
    val loanRefId: String
    val loanType: String
    val desiredAmount: Double
    val loanPeriod: Int
    val requiredGuarantors: Int
    val loanCategory: String
    val loanPurpose: String
    val loanPurposeCategory: String
    fun onBackNavClicked()
    fun onProductSelected(

    )
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onSignProfileEvent(event: SignHomeStore.Intent)
    val sigHomeStore: SignHomeStore
    val signHomeState: StateFlow<SignHomeStore.State>

}