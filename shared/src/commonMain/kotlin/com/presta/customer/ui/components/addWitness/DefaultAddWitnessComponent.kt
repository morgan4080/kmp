package com.presta.customer.ui.components.addWitness

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class DefaultAddWitnessComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit,
    private val onAddWitnessClicked: (
        loanRefId: String,
        loanType: String,
        desiredAmount: Double,
        loanPeriod: Int,
        requiredGuarantors: Int,
        loanCategory: String,
        loanPurpose: String,
        loanPurposeCategory: String,
        businessType: String,
        businessLocation: String,
        kraPin: String,
        employer: String,
        employmentNumber: String,
        grossSalary: Double,
        netSalary: Double,
        memberRefId: String,
        guarantorList:Set<GuarantorDataListing>,
        loanPurposeCategoryCode: String,
        witnessRefId: String,
        witnessName: String,
        witnessPayrollNo: String
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
    override val witnessRefId: String
): AddWitnessComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope(mainContext + SupervisorJob())
    override val platform by inject<Platform>()
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

    override val sigHomeStore: SignHomeStore =
        instanceKeeper.getStore {
            SignHomeStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val signHomeState: StateFlow<SignHomeStore.State> =
        sigHomeStore.stateFlow

    override fun onProfileEvent(event: SignHomeStore.Intent) {
        sigHomeStore.accept(event)
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
                    onProfileEvent(
                        SignHomeStore.Intent.GetPrestaTenantByPhoneNumber(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber
                        )
                    )
                    onProfileEvent(
                        SignHomeStore.Intent.GetClientSettings(
                            token = state.cachedMemberData.accessToken
                        )
                    )
                    this.cancel()
                }
            }
        }
    }
    override fun onAddWitnessSelected(
        loanRefId: String,
        loanType: String,
        desiredAmount: Double,
        loanPeriod: Int,
        requiredGuarantors: Int,
        loanCategory: String,
        loanPurpose: String,
        loanPurposeCategory: String,
        businessType: String,
        businessLocation: String,
        kraPin: String,
        employer: String,
        employmentNumber: String,
        grossSalary: Double,
        netSalary: Double,
        memberRefId: String,
        guarantorList: Set<GuarantorDataListing>,
        loanPurposeCategoryCode: String,
        witnessRefId: String,
        witnessName: String,
        witnessPayrollNo: String
    ) {
        onAddWitnessClicked(
            loanRefId,
            loanType,
            desiredAmount,
            loanPeriod,
            requiredGuarantors,
            loanCategory,
            loanPurpose,
            loanPurposeCategory,
            businessType,
            businessLocation,
            kraPin,
            employer,
            employmentNumber,
            grossSalary,
            netSalary,
            memberRefId,
            guarantorList,
            loanPurposeCategoryCode,
            witnessRefId,
            witnessName,
            witnessPayrollNo
        )
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