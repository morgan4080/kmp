package com.presta.customer.ui.components.signGuarantorForm

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStoreFactory
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.CoroutineScope
import com.presta.customer.ui.components.profile.coroutineScope
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

class DefaultSignGuarantorFormComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onItemClicked: () -> Unit,
    private val onDocumentSignedClicked: (sign: Boolean) -> Unit,
    private val onProductClicked: () -> Unit,
    override val loanNumber: String,
    override val amount: Double,
    override val loanRequestRefId: String,
    override var sign: Boolean,
    override val memberRefId: String,
): SignGuarantorFormComponent, ComponentContext by componentContext, KoinComponent {
    override val platform by inject<Platform>()
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
        sign: Boolean) {
    onDocumentSignedClicked(sign)
    }
    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
        checkAuthenticatedUser()
//        lifecycle.subscribe(
//            object : Lifecycle.Callbacks {
//                override fun onResume() {
//                    super.onResume()
//                        onDocumentSigned(sign = sign)
//
//                }
//            }
//        )
    }

}