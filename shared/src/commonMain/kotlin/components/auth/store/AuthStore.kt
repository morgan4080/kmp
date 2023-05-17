package components.auth.store

import com.arkivanov.mvikotlin.core.store.Store

interface AuthStore: Store<AuthStore.Intent, AuthStore.State, Nothing> {
    sealed class Intent {
        data class AuthenticateClient(val client_secret: String): Intent()
        data class GetMemberDetails(val phone_number: String): Intent()
        data class UpdateMember(val memberRefId: String, val access_token: String, val pinConfirmation: String): Intent()
        data class LoginUser(val phoneNumber: String, val pin: String, val tenant: String, val clientSecret: String): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val access_token: String = ""
    )
}