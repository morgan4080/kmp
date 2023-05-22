package components.auth.store

import com.arkivanov.mvikotlin.core.store.Store

interface AuthStore: Store<AuthStore.Intent, AuthStore.State, Nothing> {
    sealed class Intent {
        data class AuthenticateClient(val client_secret: String): Intent()
        data class LoginUser(val phoneNumber: String, val pin: String, val tenant: String, val clientSecret: String): Intent()
        data class UpdateError(val error: String?): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val access_token: String = "",
        val user_access_token: String = ""
    )
}