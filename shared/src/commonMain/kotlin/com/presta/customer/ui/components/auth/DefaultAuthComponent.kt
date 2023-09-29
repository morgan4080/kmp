package com.presta.customer.ui.components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.MR
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.Organisation
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.auth.store.Contexts
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStoreFactory
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import com.presta.customer.ui.components.tenant.store.TenantStoreFactory
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
) : AuthComponent, ComponentContext by componentContext, KoinComponent {
    override val platform by inject<Platform>()
    val phone = phoneNumber
    override val authStore =
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

    override val tenantStore: TenantStore =
        instanceKeeper.getStore {
            TenantStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tenantState: StateFlow<TenantStore.State> = tenantStore.stateFlow

    override fun onTenantEvent(event: TenantStore.Intent) {
        tenantStore.accept(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override fun navigateToLMS() {
        onLoginLMS()
    }

    override fun navigateToSign() {
        onLoginSIGN()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun reloadModels() {
        onEvent(AuthStore.Intent.GetCachedMemberData)
        scope.launch {
            state.collect {
                if (it.error !== null) {
                    platform.showToast(it.error)
                    onEvent(AuthStore.Intent.UpdateError(null))
                }
                if (it.cachedMemberData?.tenantId !== null) {
                    onTenantEvent(
                        TenantStore.Intent.GetClientById(
                            searchTerm = it.cachedMemberData.tenantId
                        )
                    )

                    if (it.phoneNumber !== null) {
                        onOnBoardingEvent(
                            OnBoardingStore.Intent.GetMemberDetails(
                                token = "",
                                memberIdentifier = it.phoneNumber,
                                identifierType = IdentifierTypes.PHONE_NUMBER,
                                tenantId = it.cachedMemberData.tenantId
                            )
                        )
                    } else {
                        onOnBoardingEvent(
                            OnBoardingStore.Intent.GetMemberDetails(
                                token = "",
                                memberIdentifier = phone,
                                identifierType = IdentifierTypes.PHONE_NUMBER,
                                tenantId = it.cachedMemberData.tenantId
                            )
                        )
                    }

                    onBoardingState.collect { onBoard ->
                        if (onBoard.member !== null && onBoard.member.authenticationInfo.pinStatus == PinStatus.SET || it.pinStatus == PinStatus.SET) {
                            val title: String =
                                if (it.cachedMemberData.firstName !== null && it.cachedMemberData.lastName !== null) {
                                    "${it.cachedMemberData.firstName} ${it.cachedMemberData.lastName}"
                                } else {
                                    "log in"
                                }

                            val label: String =
                                if (onBoard.member !== null && onBoard.member.accountInfo.accountName != "") {
                                    onBoard.member.accountInfo.accountName
                                } else {
                                    ""
                                }
                            println(":::::onBoard.label::::::::")
                            println(title)
                            println(label)
                            onEvent(
                                AuthStore.Intent.UpdateContext(
                                    context = Contexts.LOGIN,
                                    title = title,
                                    label = label,
                                    pinCreated = true,
                                    pinConfirmed = true,
                                    error = null
                                )
                            )

                        } else if (it.pinStatus == null && onBoard.member !== null && onBoard.member.authenticationInfo.pinStatus == null) {
                            AuthStore.Intent.UpdateError(
                                error = "Your Account Is Not Synchronised: Kindly Contact: +254 711 082 442"
                            )
                        } else {
                            AuthStore.Intent.UpdateContext(
                                context = Contexts.CREATE_PIN,
                                title = "Create pin code",
                                label = if (OrganisationModel.organisation.tenant_name != "") "You'll be able to login to " + OrganisationModel.organisation.tenant_name + "using the following pin code" else "",
                                pinCreated = false,
                                pinConfirmed = false,
                                error = null
                            )
                        }
                    }
                }
            }

        }
        scope.launch {
            tenantState.collect {
                if (it.tenantData !== null) {
                    OrganisationModel.loadOrganisation(
                        Organisation(
                            it.tenantData.alias,
                            it.tenantData.tenantId,
                            MR.images.logo,
                            MR.images.logodark,
                            true
                        )
                    )
                }
            }
        }
    }

    init {
        reloadModels()
    }
}