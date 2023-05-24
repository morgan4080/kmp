package components.applyLoan

import ApplyLoanComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.ktor.utils.io.core.Output

class DefaultApplyLoanComponent(
    componentContext: ComponentContext,
    private val onShortTermClicked: () -> Unit,
    private val onLongTermClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
): ApplyLoanComponent , ComponentContext by componentContext {
    override val model: Value<ApplyLoanComponent.Model> =
        MutableValue(
            ApplyLoanComponent.Model(
                items = List(120) { "Profile $it" }
            )
        )

    override fun onShortTermSelected() {
        onShortTermClicked()
    }

    override fun onLongTermSelected() {
        onLongTermClicked()
    }

    override fun onBackNavSelected() {
     onBackNavClicked()
    }


}