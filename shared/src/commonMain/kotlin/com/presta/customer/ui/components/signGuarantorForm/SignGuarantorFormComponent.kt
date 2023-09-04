package com.presta.customer.ui.components.signGuarantorForm

import com.presta.customer.Platform
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.processingTransaction.poller.CoroutinePoller
import com.presta.customer.ui.components.root.CoroutineScope
import com.presta.customer.ui.components.signGuarantorForm.poller.GuarantorSigningStatusPoller
import com.presta.customer.ui.components.signGuarantorForm.poller.SignGuarantorFormPoller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface SignGuarantorFormComponent {
    val loanNumber: String
    val amount: Double
    val loanRequestRefId: String
    val memberRefId: String
    val guarantorRefId: String
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    var sign: Boolean
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: ApplyLongTermLoansStore.Intent)
    val applyLongTermLoansStore: ApplyLongTermLoansStore
    val applyLongTermLoansState: StateFlow<ApplyLongTermLoansStore.State>
    val platform: Platform
    fun onBackNavClicked()
    fun onProductSelected()
    fun onDocumentSigned()


}