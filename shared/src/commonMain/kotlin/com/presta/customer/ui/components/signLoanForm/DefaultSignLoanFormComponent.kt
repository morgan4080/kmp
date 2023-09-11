package com.presta.customer.ui.components.signLoanForm

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.CoroutineScope
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.signLoanForm.poller.ApplicantSigningStatusPoller
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

class DefaultSignLoanFormComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    coroutinetineDispatcher: CoroutineDispatcher,
    private val onItemClicked: () -> Unit,
    private val onDocumentSignedClicked: (
        loanNumber: String,
        amount: Double
    ) -> Unit,
    private val onProductClicked: () -> Unit,
    override val loanNumber: String,
    override val amount: Double,
    override val loanRequestRefId: String,
    override var sign: Boolean,
    override val memberRefId: String,
) : SignLoanFormComponent, ComponentContext by componentContext, KoinComponent {
    override val platform by inject<Platform>()
    private val scope = coroutineScope(mainContext + SupervisorJob())
    private val longTermLoanRepository by inject<LongTermLoansRepository>()
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
                        ApplyLongTermLoansStore.Intent.GetLongTermLoansProducts(
                            token = state.cachedMemberData.accessToken,
                        )
                    )
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

    override fun onDocumentSigned(
        loanNumber: String,
        amount: Double
    ) {
        onDocumentSignedClicked(loanNumber,amount)
    }

    private val loanScope = coroutineScope(coroutinetineDispatcher + SupervisorJob())

    private val poller =
        ApplicantSigningStatusPoller(longTermLoanRepository, coroutinetineDispatcher)

    private fun refreshToken() {
        loanScope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(
                        AuthStore.Intent.RefreshToken(
                            tenantId = OrganisationModel.organisation.tenant_id,
                            refId = state.cachedMemberData.refId
                        )
                    )
                    val flow =
                        poller.poll(5_000L, state.cachedMemberData.accessToken, loanRequestRefId)

                    flow.collect {
                        it.onSuccess { response ->
                            if (response.applicantSigned!=null) {
                                if (response.applicantSigned){
                                    onDocumentSigned(loanNumber,amount)
                                }
                            }
                            println("Poll has Succeded ::::::")
                        }.onFailure { error ->
                            println("Poll has   Failed  ::::::")
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
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
    }

}