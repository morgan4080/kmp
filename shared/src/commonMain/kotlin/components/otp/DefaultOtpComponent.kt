package components.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultOtpComponent(
    componentContext: ComponentContext,
    private val onValidOTP: () -> Unit
): OtpComponent, ComponentContext by componentContext {
    override val model: Value<OtpComponent.Model> =
        MutableValue(
            OtpComponent.Model(
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

    override fun onValid() {
        onValidOTP()
    }
}