package com.presta.customer.ui.components.customerBanks

import com.presta.customer.network.loanRequest.model.PrestaCustomerBanksResponse
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import kotlinx.coroutines.flow.StateFlow

interface CustomerBanksComponent {
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    val modeOfDisbursementStore: ModeOfDisbursementStore
    val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State>

    fun onAuthEvent(event: AuthStore.Intent)
    fun onModeOfDisbursementEvent(event: ModeOfDisbursementStore.Intent)
    fun onProceed(account: PrestaCustomerBanksResponse)
    fun onBackNavSelected()
    fun addBankSelected()
}