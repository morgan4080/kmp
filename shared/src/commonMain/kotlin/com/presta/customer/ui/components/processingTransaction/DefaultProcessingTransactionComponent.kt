package com.presta.customer.ui.components.processingTransaction

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentsRepository
import com.presta.customer.network.payments.model.PaymentStatuses
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.processingTransaction.poller.CoroutinePoller
import com.presta.customer.ui.components.processingTransaction.store.ProcessingTransactionStore
import com.presta.customer.ui.components.processingTransaction.store.ProcessingTransactionStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

class DefaultProcessingTransactionComponent(
    storeFactory: StoreFactory,
    componentContext: ComponentContext,
    mainContext: CoroutineDispatcher,
    val onPop: () -> Unit,
    val navigateToCompleteFailure: (paymentStatus: PaymentStatuses) -> Unit,
    override val correlationId: String,
): ProcessingTransactionComponent, ComponentContext by componentContext, KoinComponent {

    private val paymentsRepository by inject<PaymentsRepository>()

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

    override val processingTransactionStore =
        instanceKeeper.getStore {
            ProcessingTransactionStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val processingTransactionState: StateFlow<ProcessingTransactionStore.State> = processingTransactionStore.stateFlow
    override fun onBackNavSelected() {
        onPop()
    }

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onProcessingTransactionEvent(event: ProcessingTransactionStore.Intent) {
        processingTransactionStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val poller = CoroutinePoller(paymentsRepository, mainContext)

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

                    val flow = poller.poll(1_000, state.cachedMemberData.accessToken, correlationId)

                    flow.collect {
                        it.onSuccess { response ->
                            println("Poller Response")
                            println(response)
                            onProcessingTransactionEvent(ProcessingTransactionStore.Intent.UpdatePaymentStatus(response))
                        }.onFailure { error ->
                            println("Poller error")
                            println(error)
                            onProcessingTransactionEvent(ProcessingTransactionStore.Intent.UpdateError(error.message))
                        }
                    }

                    poller.close()
                }
                this.cancel()
            }
        }
    }

    private var processingTransactionStateScopeJob: Job? = null
    private fun processTransactionState() {
        processingTransactionStateScopeJob = scope.launch {
            processingTransactionState.collect {  state ->
                if (state.paymentStatus !== null) {
                    if (state.paymentStatus == PaymentStatuses.COMPLETED || state.paymentStatus == PaymentStatuses.FAILURE) {
                        navigateToCompleteFailure(state.paymentStatus)
                    }
                }
            }
        }
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        refreshToken()
        processTransactionState()
    }
}