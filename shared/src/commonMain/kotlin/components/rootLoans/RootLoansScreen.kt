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
import components.topUp.LoanTopUpScreen
import components.applyLoan.ui.ApplyLoanScreen
import components.banKDisbursement.BankDisbursementScreen
import components.emergencyLoans.EmergencyLoansScreen
import components.failedTransaction.FailedTransactionScreen
import components.loanConfirmation.LoansConfirmationScreen
import components.longTermLoans.LongTermLoansScreen
import components.modeofDisbursement.SelectModeOfDisbursementScreen
import components.payLoan.PayLoanScreen
import components.processingTransaction.ProcessingTransactionScreen
import components.rootLoans.RootLoansComponent
import components.shortTermLoans.ShortTermLoansScreen
import components.succesfulTransaction.SuccessfulTransactionScreen
import helpers.LocalSafeArea

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

            }
        }
    }
}