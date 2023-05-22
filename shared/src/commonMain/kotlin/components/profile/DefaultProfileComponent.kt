package components.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val onProfileClicked: () -> Unit,
    private val onApplyLoanClicked: () -> Unit,
    private val onAddSavingsClicked: () -> Unit,
    private val onPayLoanClicked: () -> Unit,
    private val onViewFullStatementClicked: () -> Unit

) : ProfileComponent, ComponentContext by componentContext {
    override val model: Value<ProfileComponent.Model> =
        MutableValue(
            ProfileComponent.Model(
                items = List(120) { "Profile $it" }
            )
        )

    override fun onProfileSelected() {
        onProfileClicked()
    }

    override fun onApplyLoanSelected() {
       onApplyLoanClicked()
    }

    override fun onAddSavingsSelected() {
        onAddSavingsClicked()
    }

    override fun onPayLoanSelected() {
     onPayLoanClicked()
    }
    override fun onViewFullStatementSelected() {
       onViewFullStatementClicked()
    }
}