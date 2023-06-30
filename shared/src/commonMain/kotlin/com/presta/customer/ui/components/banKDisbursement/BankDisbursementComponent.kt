package com.presta.customer.ui.components.banKDisbursement

import com.arkivanov.decompose.value.Value
import com.presta.customer.network.loanRequest.model.PrestaBanksResponse
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import kotlinx.coroutines.flow.StateFlow

interface BankDisbursementComponent {

    fun onConfirmSelected(selectedBank: PrestaBanksResponse, accountName: String, accountNumber: String)
    fun onBackNavSelected()

    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onModeOfDIsbursementEvent(event: ModeOfDisbursementStore.Intent)
    val modeOfDisbursementStore: ModeOfDisbursementStore
    val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State>
}