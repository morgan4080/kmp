package components.loanConfirmation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultLoanConfirmationComponent(
    componentContext: ComponentContext,
    private val onConfirmClicked: () -> Unit,
) : LoanConfirmationComponent, ComponentContext by componentContext{

    private val models = MutableValue(
        LoanConfirmationComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<LoanConfirmationComponent.Model> = models
    override fun onConfirmSelected() {
        onConfirmClicked()
    }

}