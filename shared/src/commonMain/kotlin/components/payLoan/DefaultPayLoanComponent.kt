package components.payLoan

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultPayLoanComponent(

    componentContext: ComponentContext,
) :PayLoanComponent,ComponentContext by componentContext{

    private val models = MutableValue(
        PayLoanComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<PayLoanComponent.Model> =models

    override fun onSelected(refId: String) {
        TODO("Not yet implemented")
    }
}