package components.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update

class DefaultOtpComponent(
    componentContext: ComponentContext,
    private val onValidOTP: () -> Unit
): OtpComponent, ComponentContext by componentContext {
    private val models = MutableValue(
        OtpComponent.Model(
            loading = true,
            inputs = listOf(
                OtpComponent.InputMethod(
                    value = ""
                ),
                OtpComponent.InputMethod(
                    value = ""
                ),
                OtpComponent.InputMethod(
                    value = ""
                ),
                OtpComponent.InputMethod(
                    value = ""
                )
            ),
            label = "Verify you account details",
            title = "Enter OTP verification code",
            phone_number = null,
            email = null,
            tenant_id = null
        )
    )
    override val model: Value<OtpComponent.Model> = models

    override fun onValid() {
        onValidOTP()
    }
    override fun sendOTP() {
        models.update {
            it.copy(loading = false)
        }
    }
}