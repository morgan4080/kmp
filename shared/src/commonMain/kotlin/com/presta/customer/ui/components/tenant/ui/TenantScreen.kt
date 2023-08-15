package com.presta.customer.ui.components.tenant.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.AppContext
import com.presta.customer.SharedStatus
import com.presta.customer.ui.components.tenant.TenantComponent

@Composable
fun TenantScreen(
    component: TenantComponent
) {
    val  tenantState by component.tenantState.collectAsState()
    TenantContent(
        component = component,
        onTenantEvent = component::onEvent,
        state = tenantState
    )
}