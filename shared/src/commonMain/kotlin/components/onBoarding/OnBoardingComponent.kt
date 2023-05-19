package components.onBoarding

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import components.auth.store.AuthStore
import components.onBoarding.store.OnBoardingStore
import kotlinx.coroutines.flow.StateFlow
import organisation.Organisation


interface OnBoardingComponent {

    val authStore: AuthStore

    val onBoardingStore: OnBoardingStore

    val authState: StateFlow<AuthStore.State>

    val state: StateFlow<OnBoardingStore.State>

    fun onSubmit(
        organisation: Organisation,
        phone_number: String,
        email: String?
    )

    fun onSelectCountry()

    fun onSelectOrganisation()

    fun onCountrySelected(country: String)

    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: OnBoardingStore.Intent)
}