package com.presta.customer.ui.components.profile.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.profile.ProfileComponent


@Composable
fun ProfileScreen(
    component: ProfileComponent,
    innerPadding: PaddingValues
) {
    val authState by component.authState.collectAsState()
    val profileState by component.profileState.collectAsState()
    val addSavingsState by component.addSavingsState.collectAsState()

    ProfileContent(
        authState= authState,
        state = profileState,
        addSavingsState = addSavingsState,
        innerPadding = innerPadding,
        seeAllTransactions = component::seeAllTransactions,
        goToSavings = component::goToSavings,
        goToLoans = component::goToLoans,
        goToPayLoans = component::goToPayLoans,
        goToStatement = component::goToStatement,
        activateAccount = component::activateAccount,
    )
}


