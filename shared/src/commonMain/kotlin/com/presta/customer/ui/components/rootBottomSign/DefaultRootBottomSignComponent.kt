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
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.auth.poller.AuthPoller
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.rootSignHome.DefaultRootSignHomeComponent
import com.presta.customer.ui.components.rootSignHome.RootSignHomeComponent
import com.presta.customer.ui.components.longTermLoanRequestsList.DefaultLongTermLoansRequestsComponent
import com.presta.customer.ui.components.longTermLoanRequestsList.LongTermLoanRequestsComponent
import com.presta.customer.ui.components.signAppSettings.DefaultSignSettingsComponent
import com.presta.customer.ui.components.signAppSettings.SignSettingsComponent
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
    val gotoApplyAllLoans: () -> Unit,
    val gotoGuarantorshipRequests: () -> Unit,
    val gotoFavouriteGuarantors: () -> Unit,
    val gotoWitnessRequests: () -> Unit,
    val goBackToLMs: () -> Unit,
    val gotoReplaceGuarantor: () -> Unit,
    val gotoSignLoanForm: (
        loanNumber: String,
        amount: Double,
        loanRequestRefId: String,
        memberRefId: String
    ) -> Unit,
    backTopProfile: Boolean = false
) : RootBottomSignComponent, ComponentContext by componentContext, KoinComponent {
    private val authRepository by inject<AuthRepository>()

    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()

    private val _childStackBottom =
        childStack(
            source = navigationBottomStackNavigation,
            initialConfiguration = ConfigBottom.SignProfile,
            handleBackButton = true,
            childFactory = ::createChildBottom,
            key = "authStack"
        )

    override val childStackBottom: Value<ChildStack<*, RootBottomSignComponent.ChildBottom>> =
        _childStackBottom

    private fun createChildBottom(
        config: ConfigBottom,
        componentContext: ComponentContext
    ): RootBottomSignComponent.ChildBottom =
        when (config) {
            is ConfigBottom.SignProfile -> RootBottomSignComponent.ChildBottom.ProfileChild(
                rootSignHomeComponent(componentContext)
            )

            is ConfigBottom.Request -> RootBottomSignComponent.ChildBottom.RequestChild(
                longTermLoansrequestsListComponent(componentContext)
            )

            is ConfigBottom.Settings -> RootBottomSignComponent.ChildBottom.SettingsChild(
                settingsComponent(componentContext)
            )
        }

    private fun rootSignHomeComponent(componentContext: ComponentContext): RootSignHomeComponent =
        DefaultRootSignHomeComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            pop = {
                navigationBottomStackNavigation.pop()
            },
            onApplyLoanClicked = {
                //go to component outside root Bottom
                gotoApplyAllLoans()
            },
            onGuarantorshipRequestsClicked = {
                gotoGuarantorshipRequests()
            },
            onFavouriteGuarantorsClicked = {
                gotoFavouriteGuarantors()
            },
            onWitnessRequestClicked = {
                gotoWitnessRequests()
            },
            onLoanRequestsListClicked = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.Request)

            }
        )

    private fun longTermLoansrequestsListComponent(componentContext: ComponentContext): LongTermLoanRequestsComponent =
        DefaultLongTermLoansRequestsComponent(
            componentContext = componentContext,
            onSelected = {

            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onNavigateBackCLicked = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.SignProfile)
            },
            onReplaceGuarantorCLicked = {
                gotoReplaceGuarantor()

            },
//            loanNumber: String,
//            amount: Double,
//            loanRequestRefId: String,
//            memberRefId: String
            navigateToSignLoanFormCLicked = { loanNumber, amount, loanRequestRefId, memberRefId ->
                //create a data class transfer
                gotoSignLoanForm(
                    loanNumber,
                    amount,
                    loanRequestRefId,
                    memberRefId
                )
            }

        )

    private fun settingsComponent(componentContext: ComponentContext): SignSettingsComponent =
        DefaultSignSettingsComponent(
            componentContext = componentContext,
            onSelected = {

            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
        )

    override fun onProfileTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.SignProfile)
    }

    override fun onRequestTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Request)
    }

    override fun onSettingsTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Settings)
    }

    override fun onLmsTabClicked() {
        goBackToLMs()
    }

    private sealed class ConfigBottom : Parcelable {
        @Parcelize
        object SignProfile : ConfigBottom()

        @Parcelize
        object Request : ConfigBottom()

        @Parcelize
        object Settings : ConfigBottom()

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
            navigationBottomStackNavigation.bringToFront(ConfigBottom.SignProfile)
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