package com.presta.customer.ui.components.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.profile.ProfileComponent


@Composable
fun ProfileScreen(
    component: ProfileComponent,
    callback: () -> Unit,
) {
    val authState by component.authState.collectAsState()
    val profileState by component.profileState.collectAsState()
    val addSavingsState by component.addSavingsState.collectAsState()
    val modeOfDisbursementState by component.modeOfDisbursementState.collectAsState()
    ProfileContent(
        authState = authState,
        state = profileState,
        addSavingsState = addSavingsState,
        modeOfDisbursementState = modeOfDisbursementState,
        seeAllTransactions = component::seeAllTransactions,
        goToSavings = component::goToSavings,
        goToLoans = component::goToLoans,
        goToPayLoans = component::goToPayLoans,
        goToLoansPendingApproval = component::goToLoansPendingApproval,
        activateAccount = component::activateAccount,
        logout = component::logout,
        reloadModels = component::reloadModels,
        callback = callback
    )
}


