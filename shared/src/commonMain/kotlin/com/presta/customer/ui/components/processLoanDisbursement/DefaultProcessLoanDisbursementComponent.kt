package com.presta.customer.ui.components.processLoanDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.data.LoanRequestRepository
import com.presta.customer.network.loanRequest.model.LoanRequestStatus
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.processLoanDisbursement.poller.CoroutineLoanRequestPoller
import com.presta.customer.ui.components.processLoanDisbursement.store.ProcessLoanDisbursementStoreFactory
import com.presta.customer.ui.components.processLoanDisbursement.store.ProcessingLoanDisbursementStore
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
class DefaultProcessLoanDisbursementComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineDispatcher,
    override val amount: Double,
    override val fees: Double,
    override val requestId: String,
    val navigateToCompleteFailure: (loanRequestStatus: LoanRequestStatus) -> Unit,
): ProcessLoanDisbursementComponent, ComponentContext by componentContext,KoinComponent {
    private val loanRequestRepository by inject<LoanRequestRepository>()

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET,
                onLogOut = {  }
            ).create()
        }

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val processingLoanDisbursementStore=
        instanceKeeper.getStore {
            ProcessLoanDisbursementStoreFactory(
                storeFactory=storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val processingTransactionState: StateFlow<ProcessingLoanDisbursementStore.State> =processingLoanDisbursementStore.stateFlow
    override fun onProcessingLoanDisbursementEvent(event: ProcessingLoanDisbursementStore.Intent) {
        processingLoanDisbursementStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val poller = CoroutineLoanRequestPoller(loanRequestRepository, mainContext)


    private var refreshTokenScopeJob: Job? = null
    private fun refreshToken() {
        if (refreshTokenScopeJob?.isActive == true) return

        refreshTokenScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.RefreshToken(
                        tenantId = OrganisationModel.organisation.tenant_id,
                        refId = state.cachedMemberData.refId
                    ))

                    val flow = poller.poll(2_000L, state.cachedMemberData.accessToken, requestId)

                    flow.collect {
                        it.onSuccess { response ->
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateLoanRequestStatus(response))
                        }.onFailure { error ->
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateError(error.message))
                        }
                    }
                    poller.close()

                } else {
                    onAuthEvent(AuthStore.Intent.GetCachedMemberData)
                }
            }
        }
    }

    private var processingTransactionStateScopeJob: Job? = null
    private fun processTransactionState() {
        processingTransactionStateScopeJob = scope.launch {
            processingTransactionState.collect {  state ->
                if (state.loanDisburseMentStatus !== null) {
                    if (state.loanDisburseMentStatus.applicationStatus == LoanRequestStatus.COMPLETED || state.loanDisburseMentStatus.applicationStatus == LoanRequestStatus.FAILED|| state.loanDisburseMentStatus.applicationStatus == LoanRequestStatus.FAILED) {
                        onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateLoading(false))
                        if (!state.isLoading) {
                            navigateToCompleteFailure(state.loanDisburseMentStatus.applicationStatus)
                        }
                    }
                }
            }
        }
    }

    init {
        refreshToken()
        processTransactionState()
        onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateLoading(true))
    }
}