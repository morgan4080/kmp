package com.presta.customer.ui.components.profile

import com.arkivanov.decompose.ComponentContext
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
import kotlinx.coroutines.CoroutineScope
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.profile.store.ProfileStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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
    private val onProfileClicked: () -> Unit
) : ProfileComponent, ComponentContext by componentContext {

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
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

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onEvent(event: ProfileStore.Intent) {
        profileStore.accept(event)
    }

    private var profileStateScopeJob: Job? = null

    private fun checkProfileTransactions (access_token: String, refId: String) {
        if (profileStateScopeJob?.isActive == true) return

        profileStateScopeJob = scope.launch {
            profileState.collect { state ->
                if (state.transactionMapping !== null) {
                    onEvent(ProfileStore.Intent.GetTransactionHistory (
                        token = access_token,
                        refId = refId,
                        purposeIds = state.transactionMapping.keys.toList()
                    ))
                }
            }
        }
    }

    private var authUserScopeJob: Job? = null

    private fun checkAuthenticatedUser() {
        if (authUserScopeJob?.isActive == true) return

        authUserScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.CheckAuthenticatedUser(
                        token = state.cachedMemberData.accessToken
                    ))
                    onEvent(ProfileStore.Intent.GetBalances (
                        token = state.cachedMemberData.accessToken,
                        refId = state.cachedMemberData.refId,
                    ))
                    onEvent(ProfileStore.Intent.GetTransactionMapping (
                        token = state.cachedMemberData.accessToken
                    ))

                    checkProfileTransactions(state.cachedMemberData.accessToken, state.cachedMemberData.refId)
                }
            }
        }
    }

    private var refreshTokenScopeJob: Job? = null

    private fun refreshToken() {
        if (refreshTokenScopeJob?.isActive == true) return

        refreshTokenScopeJob = scope.launch {
            authState.collect { state ->
                if (state.cachedMemberData !== null) {
                    onAuthEvent(AuthStore.Intent.RefreshToken(
                        tenantId = OrganisationModel.organisation.tenant_id,
                        refId = state.cachedMemberData.refId
                    ))
                }
                this.cancel()
            }
        }
    }

    init {
        onAuthEvent(AuthStore.Intent.GetCachedMemberData)

        checkAuthenticatedUser()

        refreshToken()
    }
}