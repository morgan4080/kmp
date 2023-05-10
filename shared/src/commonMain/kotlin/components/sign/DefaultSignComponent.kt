package components.sign

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSignComponent (
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
): SignComponent, ComponentContext by componentContext {
    override val model: Value<SignComponent.Model> =
        MutableValue(
            SignComponent.Model(
                // create welcome screens here
                items = List(120) { "Country $it" }
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}