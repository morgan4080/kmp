package components.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import components.auth.store.AuthStore
import components.auth.store.AuthStoreFactory
import components.onBoarding.store.OnBoardingStore
import components.onBoarding.store.OnBoardingStoreFactory
import components.otp.store.OtpStore
import components.otp.store.OtpStoreFactory
import components.root.DefaultRootComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultOtpComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onValidOTP: () -> Unit
): OtpComponent, ComponentContext by componentContext {
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
    override val onBoardingState: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    override val otpStore =
        instanceKeeper.getStore {
            OtpStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<OtpStore.State> = otpStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onOnBoardingEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun onEvent(event: OtpStore.Intent) {
        otpStore.accept(event)
    }

    override fun navigate() {
        TODO("Not yet implemented")
    }

}