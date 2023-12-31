package com.presta.customer.ui.components.topUp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.profile.store.ProfileStoreFactory
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStoreFactory
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

class DefaultLoanTopUpComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onProceedClicked: (
        referencedLoanRefId: String,
        refId: String,
        amount: Double,
        maxLoanPeriod: Int,
        loanType: LoanType,
        loanName: String,
        interest: Double,
        loanPeriodUnit: String,
        currentTerm: Boolean,
        minLoanPeriod: Int,
        loanOperation:String
    ) -> Unit,
    private val onBackNavClicked: () -> Unit,
    override val maxAmount: Double,
    override val minAmount: String,
    override val loanRefId: String,
    override val loanName: String,
    override val interestRate: Double,
    override val maxLoanPeriod: Int,
    override val loanPeriodUnit: String,
    override val loanOperation: String,
    override val referencedLoanRefId: String,
    override val minLoanPeriod: Int,
    override val loanRefIds: String,
) : LoanTopUpComponent, ComponentContext by componentContext {
    override fun onProceedSelected(
        referencedLoanRefId: String,
        loanRefId: String,
        amount: Double,
        maxLoanPeriod: Int,
        loanType: LoanType,
        loanName: String,
        interest: Double,
        loanPeriodUnit: String,
        currentTerm: Boolean,
        minLoanPeriod: Int,
        loanOperation:String
    ) {
        onProceedClicked(
            referencedLoanRefId,
            loanRefId,
            amount,
            maxLoanPeriod,
            loanType,
            loanName,
            interest,
            loanPeriodUnit,
            currentTerm,
            minLoanPeriod,
            loanOperation
        )
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }

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

    override val profileStore =
        instanceKeeper.getStore {
            ProfileStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profileState: StateFlow<ProfileStore.State> = profileStore.stateFlow


    override fun onProfileEvent(event: ProfileStore.Intent) {
        profileStore.accept(event)
    }

    private var authUserScopeJob: Job? = null
    private fun checkAuthenticatedUser() {
        if (authUserScopeJob?.isActive == true) return
        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(
                        AuthStore.Intent.CheckAuthenticatedUser(
                            token = state.cachedMemberData.accessToken,
                            state.cachedMemberData.refId
                        )
                    )

                    onEvent(
                        ShortTermLoansStore.Intent.GetPrestaShortTermProductById(
                            token = state.cachedMemberData.accessToken,
                            loanId = loanRefIds
                        )
                    )

                    onEvent(
                        ShortTermLoansStore.Intent.GetPrestaShortTermTopUpList(
                            token = state.cachedMemberData.accessToken,
                            session_id = state.cachedMemberData.session_id,
                            refId = state.cachedMemberData.refId
                        )
                    )

                    onEvent(
                        ShortTermLoansStore.Intent.GetPrestaLoanEligibilityStatus(
                            token = state.cachedMemberData.accessToken,
                            session_id = state.cachedMemberData.session_id,
                            customerRefId = state.cachedMemberData.refId
                        )
                    )

                    onEvent(
                        ShortTermLoansStore.Intent.GetLoanProductById(
                            token = state.cachedMemberData.accessToken,
                            loanRefId = loanRefIds
                        )
                    )

                    this.cancel()
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

    init {

        onAuthEvent(AuthStore.Intent.GetCachedMemberData)

        checkAuthenticatedUser()

        refreshToken()
    }

    init {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onProfileEvent(
                        ProfileStore.Intent.GetLoanBalances(
                            token = state.cachedMemberData.accessToken,
                            refId = state.cachedMemberData.refId,
                        )
                    )
                    this.cancel()
                }
            }
        }

    }

}