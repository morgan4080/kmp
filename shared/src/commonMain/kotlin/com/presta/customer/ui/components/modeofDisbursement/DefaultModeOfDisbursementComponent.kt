package components.modeofDisbursement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent

class DefaultModeOfDisbursementComponent(

    private val onMpesaClicked: (correlationId: String) -> Unit,
    private val onBankClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
    private  val TransactionSuccessful: () -> Unit,

    componentContext: ComponentContext,

    ) : ModeOfDisbursementComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        ModeOfDisbursementComponent.Model(
            items = listOf()
        )
    )
    //Test --Add  state to Determine which screen is called
    // Add a navigate  method controlled By the  state of the  Transaction
    val Transact: Boolean=false

    override val model: Value<ModeOfDisbursementComponent.Model> = models
    override fun onMpesaSelected() {
        if (Transact) {
            // state flow collection
            onMpesaClicked("")
        } else{
            TransactionSuccessful()
        }

    }

    override fun onBankSelected() {
        onBankClicked()
    }

    override fun onBackNavSelected() {
      onBackNavClicked()
    }

    override fun successFulTransaction() {
     TransactionSuccessful
    }

}