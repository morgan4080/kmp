package components.shortTermLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultShortTernLoansComponent(
    componentContext: ComponentContext,
    private val onProductSelected: (refId: String) -> Unit,
): ShortTermLoansComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        ShortTermLoansComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<ShortTermLoansComponent.Model> = models

    override fun onSelected(refId: String) {
        onProductSelected(refId)
    }

}