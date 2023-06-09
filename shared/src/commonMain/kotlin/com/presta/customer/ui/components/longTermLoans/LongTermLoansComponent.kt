package com.presta.customer.ui.components.longTermLoans

import com.arkivanov.decompose.value.Value

interface LongTermLoansComponent {

    val model: Value<Model>

<<<<<<< HEAD:shared/src/commonMain/kotlin/components/profile/ProfileComponent.kt
    fun onProfileSelected()
    fun onApplyLoanSelected()
    fun onAddSavingsSelected()
    fun onPayLoanSelected()
    fun onViewFullStatementSelected()
=======
    fun onSelected(refId: String)
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/components/longTermLoans/LongTermLoansComponent.kt

    data class Model(
        val items: List<String>,
    )

}