package com.presta.customer.ui.components.addWitness

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.favouriteGuarantors.FavouriteGuarantorsComponent

@Composable
fun AddwitnessScreen(
    component: AddWitnessComponent
) {
    val authState by component.authState.collectAsState()
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val profileState by component.signHomeState.collectAsState()
    AddWitnessContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
        signHomeState = profileState,
        onProfileEvent = component::onProfileEvent,
    )

}













