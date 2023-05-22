package components.onBoarding

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import components.auth.store.AuthStore
import components.auth.store.AuthStoreFactory
import components.onBoarding.store.OnBoardingStore
import components.onBoarding.store.OnBoardingStoreFactory
import components.root.DefaultRootComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import organisation.OrganisationModel

class DefaultOnboardingComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onPush: (phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean) -> Unit,
): OnBoardingComponent, ComponentContext by componentContext {

    // after getting member data do onPush

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val onBoardingStore =
        instanceKeeper.getStore {
            OnBoardingStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun navigate(phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean) {
        onPush(
            phoneNumber,
            isTermsAccepted,
            isActive
        )
    }

    init {
        onAuthEvent(AuthStore.Intent.AuthenticateClient(
            client_secret = OrganisationModel.organisation.client_secret
        ))
    }
}