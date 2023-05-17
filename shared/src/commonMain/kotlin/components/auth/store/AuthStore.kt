package components.auth.store

import com.arkivanov.mvikotlin.core.store.Store

interface AuthStore: Store<AuthStore.Intent, AuthStore.State, Nothing> {
    sealed class Intent {

    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val access_token: String = ""
    )
}