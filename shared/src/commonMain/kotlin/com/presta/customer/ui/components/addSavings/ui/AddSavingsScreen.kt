package com.presta.customer.ui.components.addSavings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.addSavings.AddSavingsComponent

@Composable
fun AddSavingsScreen (component: AddSavingsComponent, innerPadding: PaddingValues) {
    val authState by component.authState.collectAsState()
    val state by component.addSavingsState.collectAsState()

    AddSavingsContent(
        component::onConfirmSelected,
        component::onBackNavSelected,
        component::onAuthEvent,
        component::onAddSavingsEvent,
        authState,
        state,
        innerPadding,
        sharePrice = component.sharePrice
    )
}