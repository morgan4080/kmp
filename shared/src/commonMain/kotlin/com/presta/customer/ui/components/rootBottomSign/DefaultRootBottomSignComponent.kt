package com.presta.customer.ui.components.rootBottomSign

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
import com.presta.customer.ui.components.auth.poller.AuthPoller
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.rootLoans.DefaultRootLoansComponent
import com.presta.customer.ui.components.rootLoans.ProcessLoanDisbursement
import com.presta.customer.ui.components.rootLoans.RootLoansComponent
import com.presta.customer.ui.components.rootSignHome.DefaultRootSignHomeComponent
import com.presta.customer.ui.components.rootSignHome.RootSignHomeComponent
import com.presta.customer.ui.components.sign.DefaultSignComponent
import com.presta.customer.ui.components.sign.SignComponent
import com.presta.customer.ui.components.signAppRequest.DefaultSignRequestComponent
import com.presta.customer.ui.components.signAppRequest.SignRequestComponent
import com.presta.customer.ui.signAppHome.DefaultSignHomeComponent
import com.presta.customer.ui.signAppHome.SignHomeComponent
import com.presta.customer.ui.signAppSettings.DefaultSignSettingsComponent
import com.presta.customer.ui.signAppSettings.SignSettingsComponent
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
class DefaultRootBottomSignComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    val mainContext: CoroutineDispatcher,
    val logoutToSplash: (state: Boolean) -> Unit = {},
    val gotoAllTransactions: () -> Unit,
    val gotToPendingApprovals: () -> Unit,
    val gotoPayLoans: () -> Unit,
    val gotoPayRegistrationFees: (correlationId: String, amount: Double) -> Unit,
    val processTransaction: (
        correlationId: String,
        amount: Double,
        mode: PaymentTypes
    ) -> Unit,
    val gotoApplyAllLoans: () -> Unit,
    var processLoanState: (state: ProcessLoanDisbursement?) -> Unit,
    backTopProfile: Boolean = false
) : RootBottomSignComponent, ComponentContext by componentContext, KoinComponent {
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

    override val childStackBottom: Value<ChildStack<*, RootBottomSignComponent.ChildBottom>> = _childStackBottom

    private fun createChildBottom(config: ConfigBottom, componentContext: ComponentContext): RootBottomSignComponent.ChildBottom =
        when (config) {
            is ConfigBottom.Profile -> RootBottomSignComponent.ChildBottom.ProfileChild(rootSignHomeComponent(componentContext))
            is ConfigBottom.RootLoans -> RootBottomSignComponent.ChildBottom.RequestChild(requestComponent(componentContext))
            is ConfigBottom.RootSavings -> RootBottomSignComponent.ChildBottom.SettingsChild(settingsComponent(componentContext))
            is ConfigBottom.Sign -> RootBottomSignComponent.ChildBottom.SignChild(signComponent(componentContext))
        }

    private fun profileComponent(componentContext: ComponentContext): SignHomeComponent =
        DefaultSignHomeComponent(
            componentContext = componentContext,
            onItemClicked = {

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
            processLoanState = {
                processLoanState(it)
            }
        )

    private fun rootSignHomeComponent(componentContext: ComponentContext): RootSignHomeComponent =
        DefaultRootSignHomeComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            pop = {
                navigationBottomStackNavigation.pop()
            },
            processTransaction = { correlationId, amount, mode ->
                processTransaction(correlationId, amount, mode)
            },
            onApplyLoanClicked = {
                //go to component outside root Bottom
                gotoApplyAllLoans()


            }
        )
    private fun signComponent(componentContext: ComponentContext): SignComponent =
        DefaultSignComponent(
            componentContext = componentContext,
            onSelected = {

            }
        )

    private fun requestComponent(componentContext: ComponentContext): SignRequestComponent=
        DefaultSignRequestComponent(
            componentContext = componentContext,
            onSelected = {

            }

        )
    private fun settingsComponent(componentContext: ComponentContext): SignSettingsComponent=
        DefaultSignSettingsComponent(
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
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Sign)
    }

    private sealed class ConfigBottom : Parcelable {
        @Parcelize
        object Profile : ConfigBottom()
        @Parcelize
        object RootLoans : ConfigBottom()
        @Parcelize
        object RootSavings : ConfigBottom()
        @Parcelize
        object Sign : ConfigBottom()

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
                if (state.cachedMemberData !== null) {
                    val flow = poller.poll(
                        state.cachedMemberData.expires_in * 100,
                        OrganisationModel.organisation.tenant_id,
                        state.cachedMemberData.refId
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