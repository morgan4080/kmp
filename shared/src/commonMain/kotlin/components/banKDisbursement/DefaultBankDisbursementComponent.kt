package components.banKDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultBankDisbursementComponent(
    componentContext: ComponentContext,
    private val onConfirmClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,

) : BankDisbursementComponent,ComponentContext by componentContext{

    private val models = MutableValue(
        BankDisbursementComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<BankDisbursementComponent.Model> =models
    override fun onConfirmSelected() {
     onConfirmClicked()
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }


}