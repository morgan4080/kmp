package com.presta.customer.ui.components.rootBottomStack

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.authDevice.data.AuthRepository
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.auth.poller.AuthPoller
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.DefaultProfileComponent
import com.presta.customer.ui.components.profile.ProfileComponent
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.rootLoans.DefaultRootLoansComponent
import com.presta.customer.ui.components.rootLoans.ProcessLoanDisbursement
import com.presta.customer.ui.components.rootLoans.RootLoansComponent
import com.presta.customer.ui.components.rootSavings.DefaultRootSavingsComponent
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.sign.DefaultSignComponent
import com.presta.customer.ui.components.sign.SignComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)
class DefaultRootBottomComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    val mainContext: CoroutineDispatcher,
    val logoutToSplash: (state: Boolean) -> Unit = {_ -> },
    val gotoAllTransactions: () -> Unit,
    val gotToPendingApprovals: () -> Unit,
    val gotoPayLoans: () -> Unit,
    val gotoPayRegistrationFees: (correlationId: String, amount: Double) -> Unit,
    val processTransaction: (
        correlationId: String,
        amount: Double,
        mode: PaymentTypes
    ) -> Unit,
    override val gotoSignApp: () -> Unit,
    var processLoanState: (state: ProcessLoanDisbursement?) -> Unit,
    backTopProfile: Boolean = false
) : RootBottomComponent, ComponentContext by componentContext, KoinComponent {
    private val authRepository by inject<AuthRepository>()
    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()

    private val _childStackBottom =
        childStack(
            source = navigationBottomStackNavigation,
            initialConfiguration = ConfigBottom.Profile,
            handleBackButton = true,
            childFactory = ::createChildBottom,
            key = "authStack"
        )

    override val childStackBottom: Value<ChildStack<*, RootBottomComponent.ChildBottom>> = _childStackBottom
    
    private fun createChildBottom(config: ConfigBottom, componentContext: ComponentContext): RootBottomComponent.ChildBottom =
        when (config) {
            is ConfigBottom.Profile -> RootBottomComponent.ChildBottom.ProfileChild(profileComponent(componentContext))
            is ConfigBottom.RootLoans -> RootBottomComponent.ChildBottom.RootLoansChild(rootLoansComponent(componentContext))
            is ConfigBottom.RootSavings -> RootBottomComponent.ChildBottom.RootSavingsChild(rootSavingsComponent(componentContext))
            is ConfigBottom.Sign -> RootBottomComponent.ChildBottom.SignChild(signComponent(componentContext))
        }

    private fun profileComponent(componentContext: ComponentContext): ProfileComponent =
        DefaultProfileComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            gotoAllTransactions = {
                gotoAllTransactions()
            },
            logoutToSplash = {
                logoutToSplash(true)
            },
            gotoSavings = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootSavings)
            },
            gotoLoans = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)
            },
            gotoPayLoans = {
                gotoPayLoans()
            },
            goToPendingApproval = {
                gotToPendingApprovals()
            },
            onConfirmClicked = { correlationId, amount ->
                gotoPayRegistrationFees(correlationId, amount)
            }
        )

    private fun rootLoansComponent(componentContext: ComponentContext): RootLoansComponent =
        DefaultRootLoansComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            pop = {
                navigationBottomStackNavigation.pop()
            },
            navigateToProfile = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
            },
            navigateToSign = {
                // TODO NAV TO SIGN
                gotoSignApp()
            },
            processLoanState = {
                processLoanState(it)
            }
        )

    private fun rootSavingsComponent(componentContext: ComponentContext): RootSavingsComponent =
        DefaultRootSavingsComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            pop = {
                navigationBottomStackNavigation.pop()
            },
            processTransaction = { correlationId, amount, mode ->
                processTransaction(correlationId, amount, mode)
            }
        )
    private fun signComponent(componentContext: ComponentContext): SignComponent =
        DefaultSignComponent(
            componentContext = componentContext,
            onSelected = {

            }
        )

    override fun onProfileTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
    }

    override fun onLoanTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)
    }

    override fun onSavingsTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.RootSavings)
    }

    override fun onSignTabClicked() {
        //navigate To sign app
        gotoSignApp()
       // navigationBottomStackNavigation.bringToFront(ConfigBottom.Sign)
    }

    sealed class ConfigBottom : Parcelable {
        @Parcelize
        data object Profile : ConfigBottom()
        @Parcelize
        data object RootLoans : ConfigBottom()
        @Parcelize
        data object RootSavings : ConfigBottom()
        @Parcelize
        data object Sign : ConfigBottom()
    }

    override val authStore =
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

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val poller = AuthPoller(authRepository = authRepository, mainContext)

    init {

        if (backTopProfile) {
            navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
        }

        scope.launch {
            authState.collect { state ->
                state.cachedMemberData?.let { cachedMemberData ->
                    onAuthEvent(
                        AuthStore.Intent.CheckServices(
                            token = cachedMemberData.accessToken,
                            tenantId = cachedMemberData.tenantId
                        )
                    )

                    onAuthEvent(
                        AuthStore.Intent.CheckServiceConfigs(
                            token = cachedMemberData.accessToken,
                            tenantId = cachedMemberData.tenantId
                        )
                    )

                    val flow = poller.poll(
                        cachedMemberData.expires_in * 100,
                        OrganisationModel.organisation.tenant_id,
                        cachedMemberData.refId
                    )

                    flow.collect {
                        it.onSuccess { response ->
                            onAuthEvent(AuthStore.Intent.UpdateRefreshToken(response))
                        }.onFailure { error ->
                            onAuthEvent(AuthStore.Intent.UpdateError(error.message))
                        }
                    }
                }
            }
        }

        onAuthEvent(AuthStore.Intent.GetCachedMemberData)
    }
}