package com.presta.customer.ui.components.favouriteGuarantors.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.favouriteGuarantors.FavouriteGuarantorsComponent

@Composable
fun FavouriteGaurantorsScreen(
    component: FavouriteGuarantorsComponent
) {
    val authState by component.authState.collectAsState()
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val profileState by component.signHomeState.collectAsState()
    FavouriteGuarantorContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
        signHomeState = profileState,
        onProfileEvent = component::onProfileEvent,
    )


}













