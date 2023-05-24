package components.topUp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultLoanTopUpComponent(
    componentContext: ComponentContext,
    private val onConfirmClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
) : LoanTopUpComponent, ComponentContext by componentContext{

    private val models = MutableValue(
        LoanTopUpComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<LoanTopUpComponent.Model> = models
    override fun onConfirmSelected() {
        onConfirmClicked()
    }

    override fun onBackNavSelected() {
       onBackNavClicked()
    }

}