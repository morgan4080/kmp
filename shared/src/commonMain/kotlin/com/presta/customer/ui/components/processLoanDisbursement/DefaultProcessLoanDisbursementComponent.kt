package com.presta.customer.ui.components.processLoanDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.data.LoanRequestRepository
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
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
    val navigateToCompleteFailure: () -> Unit,
): ProcessLoanDisbursementComponent, ComponentContext by componentContext, KoinComponent {
    private val loanRequestRepository by inject<LoanRequestRepository>()

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
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

    override fun navigate() {
        navigateToCompleteFailure()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val poller = CoroutineLoanRequestPoller(loanRequestRepository, mainContext)


    private var refreshTokenScopeJob: Job? = null
    private fun refreshToken() {
        if (refreshTokenScopeJob?.isActive == true) return

        refreshTokenScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    if (OrganisationModel.organisation.tenant_id!=null){
                        onAuthEvent(AuthStore.Intent.RefreshToken(
                            tenantId = OrganisationModel.organisation.tenant_id,
                            refId = state.cachedMemberData.refId
                        ))

                    }
                    println("::::::::::requestId:::::::::::::")
                    println(requestId)

                    val flow = poller.poll(2_000L, state.cachedMemberData.accessToken, requestId)

                    flow.collect {
                        it.onSuccess { response ->
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateApprovalStatus(response.applicationStatus))
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateDisbursementStatus(response.disbursementResult?.disbursementStatus))
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateAppraisalStatus(response.appraisalResult.appraisalStatus))
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateAccountingStatus(response.accountingResult?.accountingStatus))
                            onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateDisbursementTransactionId(response.disbursementResult?.transactionId))
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

    init {
        refreshToken()
        onProcessingLoanDisbursementEvent(ProcessingLoanDisbursementStore.Intent.UpdateLoading(true))
    }
}