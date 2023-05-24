package components.addSavings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultAddSavingsComponent (

    componentContext: ComponentContext,
    private val onConfirmClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
        ):AddSavingsComponent,ComponentContext by componentContext{

    private val models = MutableValue(
        AddSavingsComponent.Model(
            items = listOf()
        )
    )
    override val model: Value<AddSavingsComponent.Model> = models

    override fun onConfirmSelected() {
       onConfirmClicked()
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }


}