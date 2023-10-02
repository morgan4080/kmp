package com.presta.customer.ui.components.longTermLoanConfirmation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.longTermLoans.model.GuarantorDataListing
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DefaultLongTermLoanConfirmationComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit,
    private val navigateToSignLoanFormCLicked: (
        loanNumber: String,
        amount: Double,
        loanRequestRefId: String,
        memberRefId: String
    ) -> Unit,
    override val loanRefId: String,
    override val loanType: String,
    override val desiredAmount: Double,
    override val loanPeriod: Int,
    override val requiredGuarantors: Int,
    override val loanCategory: String,
    override val loanPurpose: String,
    override val loanPurposeCategory: String,
    override val businessType: String,
    override val businessLocation: String,
    override val kraPin: String,
    override val employer: String,
    override val employmentNumber: String,
    override val grossSalary: Double,
    override val netSalary: Double,
    override val memberRefId: String,
    override val guarantorList: Set<GuarantorDataListing>,
    override val loanPurposeCategoryCode: String,
    override val witnessRefId: String,
    override val witnessName: String
) : LongTermLoanConfirmationComponent, ComponentContext by componentContext {
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

    override val sigHomeStore: SignHomeStore =
        instanceKeeper.getStore {
            SignHomeStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val signHomeState: StateFlow<SignHomeStore.State> =
        sigHomeStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }


    override fun onSignProfileEvent(event: SignHomeStore.Intent) {
        sigHomeStore.accept(event)
    }

    override val applyLongTermLoansStore: ApplyLongTermLoansStore =
        instanceKeeper.getStore {
            ApplyLongTermLoansStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State> =
        applyLongTermLoansStore.stateFlow

    override fun navigateToSignLoanForm(
        loanNumber: String,
        amount: Double,
        loanRequestRefId: String,
        memberRefId: String
    ) {
        navigateToSignLoanFormCLicked(
            loanNumber,
            amount,
            loanRequestRefId,
            memberRefId
        )
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
                            token = state.cachedMemberData.accessToken,
                            state.cachedMemberData.refId
                        )
                    )
                    onSignProfileEvent(
                        SignHomeStore.Intent.GetPrestaTenantByPhoneNumber(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber
                        )
                    )

                    this.cancel()
                }
            }
        }
    }

    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
        onProductClicked()
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }


}