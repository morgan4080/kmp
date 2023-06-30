package com.presta.customer.ui.components.banKDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.model.PrestaBanksResponse
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class DefaultBankDisbursementComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onConfirmClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,

) : BankDisbursementComponent,ComponentContext by componentContext{

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override val authStore: AuthStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow
    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override val modeOfDisbursementStore =
        instanceKeeper.getStore {
            ModeOfDisbursementStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State> =
        modeOfDisbursementStore.stateFlow

    override fun onModeOfDIsbursementEvent(event: ModeOfDisbursementStore.Intent) {
        modeOfDisbursementStore.accept(event)
    }

    override fun onConfirmSelected(selectedBank: PrestaBanksResponse, accountName: String, accountNumber: String) {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onModeOfDIsbursementEvent(ModeOfDisbursementStore.Intent.CreateCustomerBanks(
                        token = state.cachedMemberData.accessToken,
                        customerRefId = state.cachedMemberData.refId,
                        accountNumber = accountNumber,
                        accountName = accountName,
                        paybillName = selectedBank.name,
                        paybillNumber = selectedBank.payBillNumber
                    ))
                }
            }
        }

        scope.launch {
            modeOfDisbursementState.collect { state ->
                if (state.customerBankCreatedResponse !== null) {
                    onModeOfDIsbursementEvent(ModeOfDisbursementStore.Intent.ClearCreatedResponse)
                    onConfirmClicked()
                }
            }
        }
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)

        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onModeOfDIsbursementEvent(ModeOfDisbursementStore.Intent.GetAllBanks(
                        token = state.cachedMemberData.accessToken
                    ))
                }
            }
        }
    }
}