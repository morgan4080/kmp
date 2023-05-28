import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.ui.components.banKDisbursement.BankDisbursementScreen
import com.presta.customer.ui.components.emergencyLoans.EmergencyLoansScreen
import com.presta.customer.ui.components.failedTransaction.FailedTransactionScreen
import com.presta.customer.ui.components.loanConfirmation.LoansConfirmationScreen
import com.presta.customer.ui.components.longTermLoans.LongTermLoansScreen
import com.presta.customer.ui.components.payLoan.PayLoanScreen
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansScreen
import com.presta.customer.ui.components.succesfulTransaction.SuccessfulTransactionScreen
import com.presta.customer.ui.components.topUp.LoanTopUpScreen
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.components.applyLoan.ui.ApplyLoanScreen
import com.presta.customer.ui.components.payLoanPropmpt.PayLoanPromptScreen
import components.modeofDisbursement.SelectModeOfDisbursementScreen
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionScreen
import components.rootLoans.RootLoansComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootLoansScreen(component: RootLoansComponent) {
    val childStackLoans by component.childLoansStack.subscribeAsState()
    Scaffold (
        modifier = Modifier.padding(LocalSafeArea.current),
    ) { innerPadding ->
        Children(
            stack = component.childLoansStack,
            animation = stackAnimation(fade() + scale()),
        ) {
            when(val child = it.instance) {
                is RootLoansComponent.ChildLoans.ApplyLoanChild -> ApplyLoanScreen(child.component, innerPadding)
                is RootLoansComponent.ChildLoans.ShortTermLoansChild -> ShortTermLoansScreen(child.component, innerPadding)
                is RootLoansComponent.ChildLoans.LongTermLoansChild -> LongTermLoansScreen(child.component, innerPadding)
                is RootLoansComponent.ChildLoans.EmergencyLoanChild-> EmergencyLoansScreen(child.component)
                is RootLoansComponent.ChildLoans.ConfirmLoanChild-> LoansConfirmationScreen(child.component)
                is RootLoansComponent.ChildLoans.DisbursementModeChild-> SelectModeOfDisbursementScreen(child.component)
                is RootLoansComponent.ChildLoans.ProcessingTransactionChild-> ProcessingTransactionScreen(child.component)
                is RootLoansComponent.ChildLoans.BankDisbursementChild-> BankDisbursementScreen(child.component)
                is RootLoansComponent.ChildLoans.SuccessfulTransactionChild-> SuccessfulTransactionScreen(child.component)
                is RootLoansComponent.ChildLoans.FailedTransactionChild-> FailedTransactionScreen(child.component)
                is RootLoansComponent.ChildLoans.LoanTopUpChild-> LoanTopUpScreen(child.component)
                is RootLoansComponent.ChildLoans.PayLoanChild-> PayLoanScreen(child.component)
                is RootLoansComponent.ChildLoans.PayLoanPromptChild-> PayLoanPromptScreen(child.component)

            }
        }
    }
}