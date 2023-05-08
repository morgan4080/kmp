package components.welcome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultWelcomeComponent (
    componentContext: ComponentContext,
    private val onGetStartedSelected: () -> Unit,
) : WelcomeComponent, ComponentContext by componentContext {
    override val model: Value<WelcomeComponent.Model> =
        MutableValue(WelcomeComponent.Model(
            // create welcome screens here
            items = List(3) { "Item $it" }
            )
        )

    override fun onGetStarted() {
        onGetStartedSelected()
    }
}

