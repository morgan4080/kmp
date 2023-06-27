package com.presta.customer.ui.components.tenant.store

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.tenant.data.TenantRepository
import com.presta.customer.network.tenant.model.PrestaTenantResponse
import com.presta.customer.prestaDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TenantStoreFactory(
    private val storeFactory: StoreFactory,
    private val componentContext: ComponentContext,
) : KoinComponent {

    private val tenantsRepository by inject<TenantRepository>()

    fun create(): TenantStore =
        object : TenantStore,
            Store<TenantStore.Intent, TenantStore.State, Nothing> by storeFactory.create(
                name = "TenantsStore",
                initialState = TenantStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class TenantsLoading(val isLoading: Boolean = true) : Msg()
        data class UpdateInputValue(val value: TextFieldValue) : Msg()

        data class TenantsByIdLoaded(val tenantData: PrestaTenantResponse) :
            Msg()

        data class TenantsFailed(val error: String?) : Msg()
    }


    private inner class ExecutorImpl :
        CoroutineExecutor<TenantStore.Intent, Unit, TenantStore.State, Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> TenantStore.State) {

        }

        override fun executeIntent(
            intent: TenantStore.Intent, getState: () -> TenantStore.State
        ): Unit =
            when (intent) {
                is TenantStore.Intent.GetClientById -> getCustomerById(
                    searchTerm = intent.searchTerm
                )
                is TenantStore.Intent.UpdateField -> dispatch(Msg.UpdateInputValue(intent.value))
            }
        private var getCustomerBYIdJob: Job? = null

        private fun getCustomerById(
            searchTerm: String
        ) {
            if (getCustomerBYIdJob?.isActive == true) return

            dispatch(Msg.TenantsLoading())

            getCustomerBYIdJob = scope.launch {
                tenantsRepository.getClientById(
                    searchTerm = searchTerm
                ).onSuccess { response ->
                    println(":::::::::TenantByIdByIdLoaded")
                    println(response)
                    dispatch(Msg.TenantsByIdLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.TenantsFailed(e.message))
                }
                dispatch(Msg.TenantsLoading(false))
            }
        }
    }
    private object ReducerImpl : Reducer<TenantStore.State, Msg> {
        override fun TenantStore.State.reduce(msg: Msg): TenantStore.State =
            when (msg) {
                is Msg.TenantsLoading -> copy(isLoading = msg.isLoading)
                is Msg.TenantsFailed -> copy(
                    error = msg.error,
                    tenantField = tenantField.copy(
                        errorMessage = "Invalid Tenant ID"
                    )
                )
                is Msg.UpdateInputValue -> copy(
                    tenantField = tenantField.copy(
                        value = msg.value
                    )
                )
                is Msg.TenantsByIdLoaded -> copy(tenantData = msg.tenantData)
            }
    }
}