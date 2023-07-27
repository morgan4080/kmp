package com.presta.customer.ui.components.addGuarantors

import com.presta.customer.ContactsUtils
import com.presta.customer.Platform
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import kotlinx.coroutines.flow.StateFlow

interface AddGuarantorsComponent {
    val platform: Platform
    val contactlist: ContactsUtils
    fun onBackNavClicked()
    fun onContinueSelected()
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ApplyLongTermLoansStore.Intent)
    val  applyLongTermLoansStore : ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>
    fun onProfileEvent(event: SignHomeStore.Intent)
    val  sigHomeStore: SignHomeStore
    val signHomeState: StateFlow<SignHomeStore.State>

}