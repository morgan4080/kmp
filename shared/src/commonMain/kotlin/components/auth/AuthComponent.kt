package components.auth

import com.arkivanov.decompose.value.Value

enum class Contexts {
    CREATE_PIN,
    CONFIRM_PIN,
    LOGIN
}

interface AuthComponent {
    val model: Value<Model>
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
    fun onPinSubmit()
    fun onLoginSubmit()
    fun onConfirmation()
}