package components.rootSavings

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.addSavings.AddSavingsComponent
import components.processingTransaction.ProcessingTransactionComponent
import components.savings.SavingsComponent

interface RootSavingsComponent {
    val childSavingsStack:Value<ChildStack<*, ChildSavings>>

    sealed class  ChildSavings{

        class SavingsHomeChild(val component: SavingsComponent):ChildSavings()
        class AddSavingsChild(val component: AddSavingsComponent):ChildSavings()
        class ProcessingTransactionChild(val component: ProcessingTransactionComponent):ChildSavings()

    }

}