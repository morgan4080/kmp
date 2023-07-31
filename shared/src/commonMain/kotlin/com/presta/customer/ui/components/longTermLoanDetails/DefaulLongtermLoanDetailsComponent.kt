package com.presta.customer.ui.components.longTermLoanDetails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DefaultLongTermLoanDetailsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onItemClicked: () -> Unit,
    private val onConfirmClicked: (
        loanRefId: String,
        loanType: String,
        desiredAmount: Double,
        loanPeriod: Int,
        requiredGuarantors: Int
    ) -> Unit,
    override val loanRefId: String

) : LongTermLoanDetailsComponent, ComponentContext by componentContext {

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

    override val applyLongTermLoansStore: ApplyLongTermLoansStore =
        instanceKeeper.getStore {
            ApplyLongTermLoansStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State> =
        applyLongTermLoansStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: ApplyLongTermLoansStore.Intent) {
        applyLongTermLoansStore.accept(event)
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
                        ApplyLongTermLoansStore.Intent.GetPrestaLongTermProductById(
                            token = state.cachedMemberData.accessToken,
                            loanRefId = loanRefId
                        )
                    )
                }
            }
        }
    }

    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onConfirmSelected(
        loanRefId: String,
        loanType: String,
        desiredAmount: Double,
        loanPeriod: Int,
        requiredGuarantors: Int
    ) {
        onConfirmClicked(
            loanRefId,
            loanType,
            desiredAmount,
            loanPeriod,
            requiredGuarantors
        )
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }
}