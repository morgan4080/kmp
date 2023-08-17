package com.presta.customer.ui.components.signDocument.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signDocument.SignDocumentComponent
import org.koin.core.KoinApplication.Companion.init

@Composable
fun SignDocumentScreen(component: SignDocumentComponent) {
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val authState by component.authState.collectAsState()

    SignDocumentContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
    )


}













