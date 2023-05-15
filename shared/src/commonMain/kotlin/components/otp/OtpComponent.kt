package components.otp

import com.arkivanov.decompose.value.Value

interface OtpComponent {
    val model: Value<Model>

    data class Model(
        val inputs: List<InputMethod>,
        val label: String,
        val title: String,
        val phone_number: String?,
        val email: String?,
        val tenant_id: String?,
    )

    enum class InputFields {
        OTPCHAR1,
        OTPCHAR2,
        OTPCHAR3,
        OTPCHAR4,
    }

    data class InputMethod(val fieldType: InputFields, val valueType: Any)
}