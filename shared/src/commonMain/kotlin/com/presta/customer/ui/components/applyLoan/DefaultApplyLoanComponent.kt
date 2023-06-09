package com.presta.customer.ui.components.applyLoan

import ApplyLoanComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultApplyLoanComponent(
    componentContext: ComponentContext,
    private val onShortTermClicked: () -> Unit,
    private val onLongTermClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
<<<<<<< HEAD:shared/src/commonMain/kotlin/components/applyLoan/DefaultApplyLoanComponent.kt
=======
    private val onPop: () -> Unit
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/components/applyLoan/DefaultApplyLoanComponent.kt
): ApplyLoanComponent , ComponentContext by componentContext {
    override val model: Value<ApplyLoanComponent.Model> =
        MutableValue(
            ApplyLoanComponent.Model(
                items = List(120) { "Profile $it" }
            )
        )

    override fun onShortTermSelected() {
        onShortTermClicked()
    }

    override fun onLongTermSelected() {
        onLongTermClicked()
    }

    override fun onBackNavSelected() {
     onBackNavClicked()
    }

<<<<<<< HEAD:shared/src/commonMain/kotlin/components/applyLoan/DefaultApplyLoanComponent.kt
=======
    override fun onBack() {
       onPop()
    }

>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/components/applyLoan/DefaultApplyLoanComponent.kt

}