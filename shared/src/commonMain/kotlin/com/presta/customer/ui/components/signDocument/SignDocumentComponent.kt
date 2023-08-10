package com.presta.customer.ui.components.signDocument

interface SignDocumentComponent {
    val loanNumber: String
    val amount: Double
    val loanRequestRefId: String
    fun onBackNavClicked()
    fun onProductSelected()
}