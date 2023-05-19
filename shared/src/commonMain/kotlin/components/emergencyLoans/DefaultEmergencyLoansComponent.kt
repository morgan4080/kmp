package components.emergencyLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultEmergencyLoansComponent (
    componentContext: ComponentContext,
    ): EmergencyLoansComponent, ComponentContext by componentContext {
    private val models = MutableValue(
        EmergencyLoansComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<EmergencyLoansComponent.Model> =models
    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }

}