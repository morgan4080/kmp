package components.applyLoan

import ApplyLoanComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import components.profile.ProfileComponent

class DefaultApplyLoanComponent(
    componentContext: ComponentContext,
    private val onLoanClicked: () -> Unit
): ApplyLoanComponent , ComponentContext by componentContext {
    override val model: Value<ApplyLoanComponent.Model> =
        MutableValue(
            ApplyLoanComponent.Model(
                items = List(120) { "Profile $it" }
            )
        )

    override fun onLoanSelected() {
        onLoanClicked()
    }
}