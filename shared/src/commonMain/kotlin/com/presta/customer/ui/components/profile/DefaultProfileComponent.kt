package com.presta.customer.ui.components.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.addSavings.store.AddSavingsStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStoreFactory
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.profile.store.ProfileStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

class DefaultProfileComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    storeFactory: StoreFactory,
    private val logoutToSplash: () -> Unit,
    private val gotoAllTransactions: () -> Unit,
    private val gotoSavings: () -> Unit,
    private val gotoLoans: () -> Unit,
    private val gotoPayLoans: () -> Unit,
    private val goToPendingApproval: () -> Unit,
    private val onConfirmClicked: (correlationId: String, amount: Double) -> Unit
) : ProfileComponent, ComponentContext by componentContext {

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = PinStatus.SET,
                onLogOut = logoutToSplash
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val profileStore =
        instanceKeeper.getStore {
            ProfileStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profileState: StateFlow<ProfileStore.State> = profileStore.stateFlow

    override val modeOfDisbursementStore =
        instanceKeeper.getStore {
            ModeOfDisbursementStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val modeOfDisbursementState: StateFlow<ModeOfDisbursementStore.State> = modeOfDisbursementStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onEvent(event: ProfileStore.Intent) {
        profileStore.accept(event)
    }

    override fun onModeOfDisbursementEvent(event: ModeOfDisbursementStore.Intent) {
        modeOfDisbursementStore.accept(event)
    }

    override fun seeAllTransactions() {
        gotoAllTransactions()
    }

    override fun goToSavings() {
        gotoSavings()
    }

    override fun goToLoans() {
        gotoLoans()
    }

    override fun goToPayLoans() {
        gotoPayLoans()
    }

    override fun goToLoansPendingApproval() {
        goToPendingApproval()
    }

    override val addSavingsStore =
        instanceKeeper.getStore {
            AddSavingsStoreFactory(
                storeFactory = storeFactory
            ).create()
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val addSavingsState: StateFlow<AddSavingsStore.State> = addSavingsStore.stateFlow

    override fun onAddSavingsEvent(event: AddSavingsStore.Intent) {
        addSavingsStore.accept(event)
    }

    override fun activateAccount(amount: Double) {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAddSavingsEvent(
                        AddSavingsStore.Intent.MakePayment(
                            token = state.cachedMemberData.accessToken,
                            phoneNumber = state.cachedMemberData.phoneNumber,
                            loanRefId = null,
                            beneficiaryPhoneNumber = null,
                            amount = amount,
                            paymentType = PaymentTypes.MEMBERSHIPFEES
                        )
                    )
                }
                this.cancel()
            }
        }

        scope.launch {
            addSavingsState.collect { state ->
                if (state.correlationId !== null) {
                    val correlationId = state.correlationId
                    onConfirmClicked(correlationId, amount)
                    onAddSavingsEvent(AddSavingsStore.Intent.ClearCorrelationId(null))
                    this.cancel()
                }
            }
        }
    }

    private fun checkProfileTransactions (access_token: String, refId: String) {
        scope.launch {
            profileState.collect { state ->
                if (state.transactionMapping !== null) {
                    onEvent(ProfileStore.Intent.GetTransactionHistory (
                        token = access_token,
                        refId = refId,
                        purposeIds = state.transactionMapping.keys.toList()
                    ))

                    this.cancel()
                }
            }
        }
    }

    private fun checkAuthenticatedUser() {

        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
                        token = state.cachedMemberData.accessToken
                    ))
                    onEvent(ProfileStore.Intent.GetSavingsBalances (
                        token = state.cachedMemberData.accessToken,
                        refId = state.cachedMemberData.refId,
                    ))
                    onEvent(ProfileStore.Intent.GetLoanBalances (
                        token = state.cachedMemberData.accessToken,
                        refId = state.cachedMemberData.refId,
                    ))
                    onEvent(ProfileStore.Intent.GetTransactionMapping (
                        token = state.cachedMemberData.accessToken
                    ))

                    onModeOfDisbursementEvent(ModeOfDisbursementStore.Intent.GetPendingApprovals(
                        token = state.cachedMemberData.accessToken,
                        customerRefId = state.cachedMemberData.refId,
                        applicationStatus = listOf(LoanApplicationStatus.NEWAPPLICATION)
                    ))

                    checkProfileTransactions(state.cachedMemberData.accessToken, state.cachedMemberData.refId)
                }
            }
        }
    }

    override fun logout() {
        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.LogOutUser)
                }
            }
        }
    }

    override fun reloadModels() {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }
}