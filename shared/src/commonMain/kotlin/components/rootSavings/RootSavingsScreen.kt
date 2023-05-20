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
import components.addSavings.AddSavingsScreen
import components.processingTransaction.ProcessingTransactionScreen
import components.rootSavings.RootSavingsComponent
import components.savings.SavingsScreen
import components.transactionHistoryScreen.TransactionHistoryScreen
import helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootSavingsScreen(component: RootSavingsComponent) {
    val childStackSavings by component.childSavingsStack.subscribeAsState()

    Scaffold (
        modifier = Modifier.padding(LocalSafeArea.current),
    ) { innerPadding ->
        Children(
            stack = component.childSavingsStack,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootSavingsComponent.ChildSavings.SavingsHomeChild -> SavingsScreen(child.component)
                is RootSavingsComponent.ChildSavings.AddSavingsChild -> AddSavingsScreen(child.component,innerPadding)
                is RootSavingsComponent.ChildSavings.ProcessingTransactionChild-> ProcessingTransactionScreen(child.component)
                is RootSavingsComponent.ChildSavings.TransactionHistoryChild-> TransactionHistoryScreen(child.component)

            }
        }
    }
}

