package com.presta.customer.ui.components.signAppHome

import com.arkivanov.decompose.ComponentContext

class DefaultSignHomeComponent (
    componentContext: ComponentContext,
    private val onApplyLoanClicked: () -> Unit,
    private val onGuarantorshipRequestsClicked: () -> Unit,
    private val onFavouriteGuarantorsClicked: () -> Unit,
    private val witnessRequestClicked: () -> Unit,
): SignHomeComponent, ComponentContext by componentContext {
    override fun onApplyLoanSelected() {
        onApplyLoanClicked()
    }

    override fun guarantorshipRequestsSelected() {
      onGuarantorshipRequestsClicked()
    }

    override fun favouriteGuarantorsSelected() {
       onFavouriteGuarantorsClicked()
    }

    override fun witnessRequestSelected() {
        witnessRequestClicked()
    }
}