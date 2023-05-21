package components.rootLoans

import ApplyLoanComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.banKDisbursement.BankDisbursementComponent
import components.emergencyLoans.EmergencyLoansComponent
import components.loanConfirmation.LoanConfirmationComponent
import components.longTermLoans.LongTermLoansComponent
import components.modeofDisbursement.ModeOfDisbursementComponent
import components.processingTransaction.ProcessingTransactionComponent
import components.shortTermLoans.ShortTermLoansComponent

interface RootLoansComponent {
    val childLoansStack: Value<ChildStack<*, ChildLoans>>

    sealed class ChildLoans {
        class ApplyLoanChild(val component: ApplyLoanComponent) : ChildLoans()

        class ShortTermLoansChild(val component: ShortTermLoansComponent) : ChildLoans()

        class LongTermLoansChild(val component: LongTermLoansComponent) : ChildLoans()

        // class ProductLoansChild(val component: ProductComponent):ChildLoans()
        class EmergencyLoanChild(val component: EmergencyLoansComponent) : ChildLoans()

        class ConfirmLoanChild(val component: LoanConfirmationComponent) : ChildLoans()

        class DisbursementModeChild(val component: ModeOfDisbursementComponent) : ChildLoans()

        class ProcessingTransactionChild(val component: ProcessingTransactionComponent) : ChildLoans()
        class BankDisbursementChild(val component: BankDisbursementComponent) : ChildLoans()

    }

}