package components.auth

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

enum class Contexts {
    CREATE_PIN,
    CONFIRM_PIN,
    LOGIN
}

interface AuthComponent {
    val models: MutableValue<Model>

    val model: Value<Model>

    val authStore:  AuthStore

    val state: StateFlow<AuthStore.State>

    data class Model(
        val loading: Boolean,
        val inputs: List<InputMethod>,
        val label: String,
        val title: String,
        val phone_number: String?,
        val email: String?,
        val tenant_id: String?,
        var termsAccepted: Boolean,
        var pinConfirmed: Boolean,
        var pinCreated: Boolean,
        val context: Contexts,
    )
    data class InputMethod(val value: String)
    fun onEvent(event: AuthStore.Intent)
}