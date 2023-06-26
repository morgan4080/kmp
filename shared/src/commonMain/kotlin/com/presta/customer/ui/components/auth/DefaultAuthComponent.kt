package com.presta.customer.ui.components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.auth.store.Contexts
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.root.DefaultRootComponent
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

class DefaultAuthComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: CoroutineContext,
    phoneNumber: String,
    isTermsAccepted: Boolean,
    isActive: Boolean,
    pinStatus: PinStatus?,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onLogin: () -> Unit,
): AuthComponent, ComponentContext by componentContext, KoinComponent {

    override val platform by inject<Platform>()

    override var authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = phoneNumber,
                isTermsAccepted = isTermsAccepted,
                isActive = isActive,
                pinStatus = pinStatus,
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<AuthStore.State> = authStore.stateFlow

    override val onBoardingStore =
        instanceKeeper.getStore {
            OnBoardingStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val onBoardingState: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    override fun onEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onOnBoardingEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun navigate() {
        onLogin()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    init {
        scope.launch {
            state.collect {
                if (it.phoneNumber !== null) {
                    onOnBoardingEvent(OnBoardingStore.Intent.GetMemberDetails(
                        token = "",
                        memberIdentifier = it.phoneNumber,
                        identifierType = IdentifierTypes.PHONE_NUMBER,
                        tenantId = OrganisationModel.organisation.tenant_id
                    ))
                }
            }
        }

        scope.launch {
            onBoardingState.collect {
                if (it.member?.authenticationInfo?.pinStatus == PinStatus.SET) {
                    onEvent(AuthStore.Intent.UpdateContext(
                        context = Contexts.LOGIN,
                        title = "Enter pin code to login",
                        label = "Login to Presta Customer using the following pin code",
                        pinCreated = true,
                        pinConfirmed = true,
                        error = null
                    ))
                } else {
                    AuthStore.Intent.UpdateContext(
                        context = Contexts.CREATE_PIN,
                        title = "Create pin code",
                        label = "You'll be able to login to Presta Customer using the following pin code",
                        pinCreated = false,
                        pinConfirmed = false,
                        error = null
                    )
                }
            }
        }
    }
}