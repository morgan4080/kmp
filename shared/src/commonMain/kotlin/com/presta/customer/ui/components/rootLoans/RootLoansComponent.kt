package com.presta.customer.ui.components.rootLoans

import ApplyLoanComponent
import FailedTransactionComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.banKDisbursement.BankDisbursementComponent
import com.presta.customer.ui.components.specificLoanType.SpecificLoansComponent
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent
import com.presta.customer.ui.components.payLoan.PayLoanComponent
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionComponent
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.succesfulTransaction.SuccessfulTransactionComponent
import com.presta.customer.ui.components.topUp.LoanTopUpComponent
import com.presta.customer.ui.components.longTermLoans.LongTermLoansComponent
import com.presta.customer.ui.components.payLoanPropmpt.PayLoanPromptComponent

interface RootLoansComponent {
    val childLoansStack: Value<ChildStack<*, ChildLoans>>

    sealed class ChildLoans {
        class ApplyLoanChild(val component: ApplyLoanComponent) : ChildLoans()

        class ShortTermLoansChild(val component: ShortTermLoansComponent) : ChildLoans()

        class LongTermLoansChild(val component: LongTermLoansComponent) : ChildLoans()

        // class ProductLoansChild(val component: ProductComponent):ChildLoans()
        class EmergencyLoanChild(val component: SpecificLoansComponent) : ChildLoans()

        class ConfirmLoanChild(val component: LoanConfirmationComponent) : ChildLoans()

        class DisbursementModeChild(val component: ModeOfDisbursementComponent) : ChildLoans()

        class ProcessingTransactionChild(val component: ProcessingTransactionComponent) : ChildLoans()
        class BankDisbursementChild(val component: BankDisbursementComponent) : ChildLoans()
        class SuccessfulTransactionChild(val component: SuccessfulTransactionComponent) : ChildLoans()
        class FailedTransactionChild(val component: FailedTransactionComponent) : ChildLoans()
        class LoanTopUpChild(val component: LoanTopUpComponent) : ChildLoans()
    }

}