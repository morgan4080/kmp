package components.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultOtpComponent(
    componentContext: ComponentContext
): OtpComponent, ComponentContext by componentContext {
    override val model: Value<OtpComponent.Model> =
        MutableValue(
            OtpComponent.Model(
                inputs = listOf(
                    OtpComponent.InputMethod(
                        fieldType = OtpComponent.InputFields.OTPCHAR1,
                        valueType = Int
                    ),
                    OtpComponent.InputMethod(
                        fieldType = OtpComponent.InputFields.OTPCHAR2,
                        valueType = Int
                    ),
                    OtpComponent.InputMethod(
                        fieldType = OtpComponent.InputFields.OTPCHAR3,
                        valueType = Int
                    ),
                    OtpComponent.InputMethod(
                        fieldType = OtpComponent.InputFields.OTPCHAR4,
                        valueType = Int
                    )
                ),
                label = "Verify you account details",
                title = "Enter OTP verification code",
                phone_number = null,
                email = null,
                tenant_id = null
            )
        )
}