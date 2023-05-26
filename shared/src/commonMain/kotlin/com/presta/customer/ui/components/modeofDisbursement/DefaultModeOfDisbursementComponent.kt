package components.modeofDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent

class DefaultModeOfDisbursementComponent(

    private val onMpesaClicked: () -> Unit,
    private val onBankClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,

    componentContext: ComponentContext,

    ) : ModeOfDisbursementComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        ModeOfDisbursementComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<ModeOfDisbursementComponent.Model> = models
    override fun onMpesaSelected() {
        onMpesaClicked()
    }

    override fun onBankSelected() {
        onBankClicked()
    }

    override fun onBackNavSelected() {
      onBackNavClicked()
    }

}