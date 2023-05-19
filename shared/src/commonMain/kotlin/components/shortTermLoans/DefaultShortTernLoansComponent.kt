package components.shortTermLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultShortTernLoansComponent(
    componentContext: ComponentContext,
): ShortTermLoansComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        ShortTermLoansComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<ShortTermLoansComponent.Model> = models


    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }

}