package com.presta.customer.ui.components.modeofDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

class DefaultModeOfDisbursementComponent(
    private val onMpesaClicked: (correlationId: String ,amount: Double, fees: Double,) -> Unit,
    private val onBankClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
    private val TransactionSuccessful: () -> Unit,
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    override val refId: String,
    override val amount: Double,
    override val loanPeriod: String,
    override val loanType: LoanType,
    override val fees: Double,
    override val referencedLoanRefId: String?,
    override val currentTerm: Boolean,
) : ModeOfDisbursementComponent, ComponentContext by componentContext {
    override val authStore: AuthStore =
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
    override val modeOfDisbursementStore =
        instanceKeeper.getStore {
            ModeOfDisbursementStoreFactory(
                storeFactory = storeFactory
            ).create()
        }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State> =
        modeOfDisbursementStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow
    override fun onRequestLoanEvent(event: ModeOfDisbursementStore.Intent) {
        modeOfDisbursementStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private var refreshTokenJob: Job? = null
    private fun refreshToken() {
        if (refreshTokenJob?.isActive == true) return

        refreshTokenJob = scope.launch {
            authState.collect { state ->
                println(state.cachedMemberData)
                if (state.cachedMemberData !== null) {
                    onAuthEvent(
                        AuthStore.Intent.RefreshToken(
                            tenantId = OrganisationModel.organisation.tenant_id,
                            refId = state.cachedMemberData.refId
                        )
                    )
                }
                this.cancel()
            }
        }
    }

    private var authUserScopeJob: Job? = null

    private var loanRequestScopeJob: Job? = null
    override fun onMpesaSelected() {
        //business logic send   request and call the  appropriate screen  based on  the response
        if (authUserScopeJob?.isActive == true) return
        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onRequestLoanEvent(
                        ModeOfDisbursementStore.Intent.RequestLoan(
                            token = state.cachedMemberData.accessToken,
                            amount = amount.toInt(),
                            currentTerm =currentTerm,
                            customerRefId = state.cachedMemberData.refId,
                            disbursementAccountReference = state.cachedMemberData.phoneNumber,
                            disbursementMethod = DisbursementMethod.MOBILEMONEY,
                            loanPeriod = loanPeriod.toInt(),
                            loanType = loanType,
                            productRefId = refId,
                            referencedLoanRefId = referencedLoanRefId,
                            requestId = null,
                            sessionId = state.cachedMemberData.session_id
                        )
                    )
                }
                this.cancel()
            }
        }
        loanRequestScopeJob = scope.launch {
            modeOfDisbursementState.collect { state ->
                if (state.requestId!==null) {
                    val requestId = state.requestId
                    onMpesaClicked(requestId,amount,0.00)
                    this.cancel()
                }
            }
        }
    }

    override fun onBankSelected() {
        onBankClicked()
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }

    override fun successFulTransaction() {
        TransactionSuccessful
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        refreshToken()
    }

}