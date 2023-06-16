package com.presta.customer.ui.components.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import com.presta.customer.network.authDevice.model.RefreshTokenResponse
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.onBoarding.model.RegistrationFeeStatus

data class InputMethod(val value: String)
enum class Contexts {
    CREATE_PIN,
    CONFIRM_PIN,
    LOGIN
}
data class CachedMemberData(
    val accessToken: String,
    val refreshToken: String,
    val session_id:String,
    val refId: String,
    val registrationFees: Double,
    val registrationFeeStatus: String,
    val phoneNumber: String,
)
interface AuthStore: Store<AuthStore.Intent, AuthStore.State, Nothing> {
    sealed class Intent {
        data class LoginUser(val phoneNumber: String, val pin: String, val tenantId: String, val refId: String, val registrationFees: Double, val registrationFeeStatus: String): Intent()
        data class  RefreshToken(val tenantId: String, val refId: String): Intent()
        object GetCachedMemberData: Intent()
        data class CheckAuthenticatedUser(val token: String): Intent()
        data class UpdateError(val error: String?): Intent()
        data class UpdateContext(val context: Contexts, val title: String, val label: String, val pinCreated: Boolean, val pinConfirmed: Boolean, val error: String?): Intent()
        object LogOutUser: Intent()
        data class UpdateOnlineState(val isOnline: Boolean): Intent()
    }

    data class State(
        val isOnline: Boolean = false,
        val isLoading: Boolean = false,
        val isTermsAccepted: Boolean = false,
        val isActive: Boolean = false,
        val error: String? = null,
        val pinStatus: PinStatus? = null,
        val cachedMemberData: CachedMemberData? = null,
        val loginResponse: PrestaLogInResponse? = null,
        val refreshTokenResponse: RefreshTokenResponse? = null,
        val authUserResponse: PrestaCheckAuthUserResponse? = null,
        val phoneNumber: String? = null,
        val pinConfirmed: Boolean = false,
        val pinCreated: Boolean = false,
        val context: Contexts = Contexts.CREATE_PIN,
        val label: String = "You'll be able to login to Presta Customer using the following pin code",
        val title: String = "Create pin code",
        val inputs: List<InputMethod> = listOf(
            InputMethod(
                value = ""
            ),
            InputMethod(
                value = ""
            ),
            InputMethod(
                value = ""
            ),
            InputMethod(
                value = ""
            )
        ),
    )
}