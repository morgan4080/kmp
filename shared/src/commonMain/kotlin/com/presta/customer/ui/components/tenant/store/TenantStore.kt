package com.presta.customer.ui.components.tenant.store

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.tenant.model.PrestaTenantResponse

enum class InputFields {
    TENANT_ID
}
data class InputMethod(val inputLabel: String, val fieldType: InputFields, val required: Boolean, var value: TextFieldValue, val errorMessage: String)

interface TenantStore:
    Store<TenantStore.Intent, TenantStore.State, Nothing> {

    sealed class Intent{
        data  class  GetClientById(val searchTerm: String): Intent()
        data  class  UpdateField(
            val value: TextFieldValue
        ): Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val tenantField: InputMethod = InputMethod(
            inputLabel = "Enter Tenant ID*",
            fieldType = InputFields.TENANT_ID,
            required = true,
            value = TextFieldValue(),
            errorMessage = ""
        ),
        val tenantData: PrestaTenantResponse?=null
    )
}
