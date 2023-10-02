package com.presta.customer.ui.components.splash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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

class DefaultSplashComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    private val onSignUp: () -> Unit,
    private val onSignIn: () -> Unit,
    private val navigateToAuth: ( memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?) -> Unit,
): SplashComponent, ComponentContext by componentContext {

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

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val model: Value<SplashComponent.Model> =
        MutableValue(SplashComponent.Model(
            organisation = OrganisationModel.organisation
        ))

    override fun onEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onSignUpClicked() {
        onSignUp()
    }
    override fun onSignInClicked() {
        onSignIn()
    }

    init {
        onEvent(AuthStore.Intent.GetCachedMemberData)

        scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null && state.cachedMemberData.accessToken !== "") {
                    if (
                        state.authUserResponse == null &&
                        state.cachedMemberData.accessToken !== "" &&
                        state.cachedMemberData.refId !== "" &&
                        state.cachedMemberData.phoneNumber !== ""
                    ) {
                        if (state.isOnline) {
                            navigateToAuth(
                                state.cachedMemberData.refId,
                                state.cachedMemberData.phoneNumber,
                                true,
                                true,
                                DefaultRootComponent.OnBoardingContext.LOGIN,
                                PinStatus.SET
                            )
                            this.cancel()
                        }
                    }
                }
            }
        }
    }
}