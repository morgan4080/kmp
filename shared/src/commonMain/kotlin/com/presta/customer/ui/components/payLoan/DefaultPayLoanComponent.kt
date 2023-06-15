package com.presta.customer.ui.components.payLoan

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.network.profile.model.LoanBreakDown
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.addSavings.store.AddSavingsStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.profile.store.ProfileStoreFactory
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

class DefaultPayLoanComponent(
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    componentContext: ComponentContext,
    private val onPayClicked: (desiredAmount: String, correlationId: String) -> Unit,
    private val onPop: () -> Unit
) : PayLoanComponent, ComponentContext by componentContext {
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

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET,
                onLogOut = {

                }
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val profileStore =
        instanceKeeper.getStore {
            ProfileStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profileState: StateFlow<ProfileStore.State> = profileStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onEvent(event: ProfileStore.Intent) {
        profileStore.accept(event)
    }

    private var authUserScopeJob: Job? = null

    private var payLoanScopeJob: Job? = null

    override fun onPaySelected(desiredAmount: String, loan: LoanBreakDown) {
        if (authUserScopeJob?.isActive == true) return

        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAddSavingsEvent(
                        AddSavingsStore.Intent.MakePayment(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber,
                            loanRefId = loan.refId,
                            beneficiaryPhoneNumber = null,
                            amount = desiredAmount.toDouble(),
                            paymentType = PaymentTypes.LOAN
                        )
                    )
                }
                this.cancel()
            }
        }

        payLoanScopeJob = scope.launch {
            addSavingsState.collect { state ->
                if (state.correlationId !== null) {
                    val correlationId = state.correlationId
                    onPayClicked(desiredAmount, correlationId)
                    onAddSavingsEvent(AddSavingsStore.Intent.ClearCorrelationId(null))
                    this.cancel()
                }
            }
        }
    }

    override fun onBack() {
        onPop()
    }

    private fun refreshToken() {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(
                        AuthStore.Intent.RefreshToken(
                            tenantId = OrganisationModel.organisation.tenant_id,
                            refId = state.cachedMemberData.refId
                        )
                    )
                    onEvent(ProfileStore.Intent.GetLoanBalances (
                        token = state.cachedMemberData.accessToken,
                        refId = state.cachedMemberData.refId,
                    ))
                } else {
                    onAuthEvent(AuthStore.Intent.GetCachedMemberData)
                }
            }
        }
    }

    init {
        refreshToken()
    }
}