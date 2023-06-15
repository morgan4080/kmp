package com.presta.customer.ui.components.loanConfirmation

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
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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

class DefaultLoanConfirmationComponent(
    componentContext: ComponentContext,

    private val onConfirmClicked: (
        correlationId: String,
        amount: Double,
        fees: Double
    ) -> Unit,
    private val onBackNavClicked: () -> Unit,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    override val refId: String,
    override val amount: Double,
    override val loanPeriod: String,
    override val loanInterest: String,
    override val loanName: String,
    override val loanPeriodUnit: String,
    override val loanOperation: String,
    override val loanType: LoanType,
    override val referencedLoanRefId: String?,
    override val currentTerm: Boolean,
) : LoanConfirmationComponent, ComponentContext by componentContext {
    override val authStore: AuthStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET
            ).create()
        }


    override val modeOfDisbursementStore =
        instanceKeeper.getStore {
            ModeOfDisbursementStoreFactory(
                storeFactory = storeFactory
            ).create()
        }


    override fun onBackNavSelected() {
        onBackNavClicked()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val shortTermloansStore: ShortTermLoansStore =
        instanceKeeper.getStore {
            ShortTermLoansStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val shortTermloansState: StateFlow<ShortTermLoansStore.State> =
        shortTermloansStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: ShortTermLoansStore.Intent) {
        shortTermloansStore.accept(event)
    }

    private var authUserScopeJob: Job? = null
    private fun checkAuthenticatedUser() {
        if (authUserScopeJob?.isActive == true) return
        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(
                        AuthStore.Intent.CheckAuthenticatedUser(
                            token = state.cachedMemberData.accessToken
                        )
                    )
                    onEvent(
                        ShortTermLoansStore.Intent.GetPrestaShortTermProductList(
                            token = state.cachedMemberData.accessToken,
                            refId = state.cachedMemberData.refId
                        )
                    )

                    onEvent(
                        ShortTermLoansStore.Intent.GetPrestaShortTermProductById(
                            token = state.cachedMemberData.accessToken,
                            loanId = refId,
                        )
                    )
                }
            }
        }
    }

    private var refreshTokenScopeJob: Job? = null
    private fun refreshToken() {
        if (refreshTokenScopeJob?.isActive == true) return
        refreshTokenScopeJob = scope.launch {
            authState.collect { state ->
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

    //Execute   payment  on confirm

    override fun onRequestLoanEvent(event: ModeOfDisbursementStore.Intent) {
        modeOfDisbursementStore.accept(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State> =
        modeOfDisbursementStore.stateFlow

    override fun onConfirmSelected() {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onRequestLoanEvent(
                        ModeOfDisbursementStore.Intent.RequestLoan(
                            token = state.cachedMemberData.accessToken,
                            amount = amount.toInt(),
                            currentTerm = currentTerm,
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
                    this.cancel()
                }
            }
        }

        scope.launch {
            modeOfDisbursementState.collect { state ->
                if (state.requestId !== null) {
                    val requestId = state.requestId
                    onConfirmClicked(requestId, amount, 0.00)
                }
            }
        }

    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
        refreshToken()
    }
}