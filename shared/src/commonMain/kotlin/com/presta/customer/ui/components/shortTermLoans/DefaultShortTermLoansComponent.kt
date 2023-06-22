package com.presta.customer.ui.components.shortTermLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
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

class DefaultShortTermLoansComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    storeFactory: StoreFactory,
    private var onProductClicked: (refId: String, loanName: String,referencedLoanRefId: String?) -> Unit,
    private val onProceedClicked: (
        referencedLoanRefId: String,
        loanRefId: String,
        maxAmount: Double,
        minAmount: Double,
        loanName: String,
        interestRate: Double,
        maxLoanPeriod: Int,
        loanPeriodUnit: String,
        minLoanPeriod:Int,
        loanRefIds:String
    ) -> Unit,
    private val onBackNavClicked: () -> Unit,
    override val referencedLoanRefId: String?,

    ) : ShortTermLoansComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(mainContext + SupervisorJob())

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

    override fun onProductSelected(refId: String, loanName: String,referencedLoanRefId: String?) {
        onProductClicked(refId, loanName,referencedLoanRefId)
    }

    override fun onProceedSelected(
        referencedLoanRefId: String,
        loanRefId: String,
        minAmount: Double,
        maxAmount: Double,
        loanName: String,
        InterestRate: Double,
        maxLoanPeriod: Int,
        loanPeriodUnit: String,
        minLoanPeriod: Int,
        loanRefIds: String
    ) {
        onProceedClicked(
            referencedLoanRefId,
            loanRefId,
            minAmount,
            maxAmount,
            loanName,
            InterestRate,
            maxLoanPeriod,
            loanPeriodUnit,
            minLoanPeriod,
            loanRefIds
        )
    }

    override fun onBackNav() {
        onBackNavClicked()
    }

    override fun reloadModels() {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }

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
                }
            }
        }
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)

        checkAuthenticatedUser()
    }

}