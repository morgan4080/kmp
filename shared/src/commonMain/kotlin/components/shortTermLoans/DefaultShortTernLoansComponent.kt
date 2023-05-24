package components.shortTermLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultShortTernLoansComponent(
    componentContext: ComponentContext,
    private val onProductSelected: (refId: String) -> Unit,
    private val onProduct2Selected: (refId: String) -> Unit,
    private val onConfirmClicked: (refId: String) -> Unit,
    private val onBackNavClicked: () -> Unit,
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

    override fun onSelecte2(refId: String) {
       onProduct2Selected(refId)
    }

    override fun onConfirmSelected(refId: String) {
       onConfirmClicked(refId)
    }

    override fun onBackNav() {
       onBackNavClicked()
    }

}