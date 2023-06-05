package com.presta.customer.ui.components.addSavings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.addSavings.store.AddSavingsStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

class DefaultAddSavingsComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onConfirmClicked: (correlationId: String, amount: Double, mode: PaymentTypes) -> Unit,
    private val onBackNavClicked: () -> Unit,
): AddSavingsComponent,ComponentContext by componentContext {
    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = true,
                isActive = true,
                pinStatus = PinStatus.SET,
                onLogOut = {

                }
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override val addSavingsStore =
        instanceKeeper.getStore {
            AddSavingsStoreFactory(
                storeFactory = storeFactory
            ).create()
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val addSavingsState: StateFlow<AddSavingsStore.State> = addSavingsStore.stateFlow

    override fun onAddSavingsEvent(event: AddSavingsStore.Intent) {
        addSavingsStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private var refreshTokenJob: Job? = null

    private fun refreshToken() {
        if (refreshTokenJob?.isActive == true) return

        refreshTokenJob = scope.launch {
            authState.collect { state ->
                println(state.cachedMemberData)
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.RefreshToken(
                        tenantId = OrganisationModel.organisation.tenant_id,
                        refId = state.cachedMemberData.refId
                    ))
                }
                this.cancel()
            }
        }
    }

    private var authUserScopeJob: Job? = null

    private var addSavingsScopeJob: Job? = null

    override fun onConfirmSelected(mode: PaymentTypes, amount: Double) {
        if (authUserScopeJob?.isActive == true) return

        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAddSavingsEvent(
                        AddSavingsStore.Intent.MakePayment(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber,
                            loanRefId = null,
                            beneficiaryPhoneNumber = null,
                            amount = amount.toInt(),
                            paymentType = mode
                        )
                    )
                }
                this.cancel()
            }
        }

        addSavingsScopeJob = scope.launch {
            addSavingsState.collect { state ->
                if (state.correlationId !== null) {
                    val correlationId = state.correlationId
                    onConfirmClicked(correlationId, amount, mode)
                    onAddSavingsEvent(AddSavingsStore.Intent.ClearCorrelationId(null))
                    this.cancel()
                }
            }
        }
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        refreshToken()
    }
}