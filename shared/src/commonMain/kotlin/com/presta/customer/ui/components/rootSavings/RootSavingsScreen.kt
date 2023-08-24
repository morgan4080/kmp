
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.ui.components.addSavings.ui.AddSavingsScreen
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.savings.ui.SavingsScreen
import com.presta.customer.ui.components.savingsTransactionHistory.SavingsTransactionHistoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootSavingsScreen(component: RootSavingsComponent) {
    val childStackSavings by component.childSavingsStack.subscribeAsState()

    Scaffold { innerPadding ->
        Children(
            stack = component.childSavingsStack,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootSavingsComponent.ChildSavings.SavingsHomeChild -> SavingsScreen(child.component)
                is RootSavingsComponent.ChildSavings.AddSavingsChild -> AddSavingsScreen(child.component,innerPadding)
                is RootSavingsComponent.ChildSavings.TransactionHistoryChild-> SavingsTransactionHistoryScreen(child.component)
            }
        }
    }
}

