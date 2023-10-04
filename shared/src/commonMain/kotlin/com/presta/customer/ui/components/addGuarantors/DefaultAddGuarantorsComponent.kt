package com.presta.customer.ui.components.addGuarantors

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

class DefaultAddGuarantorsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onItemClicked: () -> Unit,
    private val onContinueClicked: (
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
    ) -> Unit,
    private val onNavigateToAddWitnessClicked: (
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
    ) -> Unit,
    override val loanRefId: String,
    override val loanType: String,
    override val desiredAmount: Double,
    override val loanPeriod: Int,
    override val requiredGuarantors: Int,
    override val loanCategory: String,
    override val loanPurpose: String,
    override val loanPurposeCategory: String,
    override val loanPurposeCategoryCode: String,
) : AddGuarantorsComponent, ComponentContext by componentContext, KoinComponent {
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
                storeFactory = storeFactory,
                loanRefId = ""
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
                    onEvent(
                        ApplyLongTermLoansStore.Intent.GetClientSettings(
                            token = state.cachedMemberData.accessToken,
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

    override fun onContinueSelected(
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
    ) {
        onContinueClicked(
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
            witnessRefId
        )
    }

    override fun onNavigateToAddWitness(
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
        witnessRefId: String
    ) {
        onNavigateToAddWitnessClicked(
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
            witnessRefId
        )

    }

    override fun reloadModels() {
       checkAuthenticatedUser()
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }
}