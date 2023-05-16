package components.otp

import com.arkivanov.decompose.value.Value

interface OtpComponent {
    val model: Value<Model>
    fun onValid()
    fun sendOTP()
    data class Model(
        val loading: Boolean,
        val inputs: List<InputMethod>,
        val label: String,
        val title: String,
        val phone_number: String?,
        val email: String?,
        val tenant_id: String?,
    )

    data class InputMethod(val value: String)
}