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
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanScreen
import com.presta.customer.ui.components.longTermLoanDetails.LongTermLoanDetailsScreen
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.rootSignHome.RootSignHomeComponent
import com.presta.customer.ui.components.savingsTransactionHistory.SavingsTransactionHistoryScreen
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.signAppHome.SignHomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootSignHomeScreen(component: RootSignHomeComponent) {
    val childSignHomeStack by component.childSignHomeStack.subscribeAsState()
    Scaffold (
        modifier = Modifier.padding(LocalSafeArea.current),
    ) { innerPadding ->
        Children(
            stack = component.childSignHomeStack,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootSignHomeComponent.ChildHomeSign.SignHomeChild -> SignHomeScreen(child.component)
//                is RootSignHomeComponent.ChildHomeSign.ApplyLongTermLoansChild-> ApplyLongTermLoanScreen(child.component)
//                is RootSignHomeComponent.ChildHomeSign.LongTermLoanDetailsChild -> LongTermLoanDetailsScreen(child.component)
                is RootSignHomeComponent.ChildHomeSign.TransactionHistoryChild-> SavingsTransactionHistoryScreen(child.component)
            }
        }
    }
}

