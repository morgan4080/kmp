package components.onBoarding.store

import com.arkivanov.mvikotlin.core.store.Store

interface OnBoardingStore: Store<OnBoardingStore.Intent, OnBoardingStore.State, Nothing> {
    sealed class Intent {

    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val phoneNumber: String = "",
    )
}