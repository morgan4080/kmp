package components.rootLoans

import ApplyLoanComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.emergencyLoans.EmergencyLoansComponent
import components.longTermLoans.LongTermLoansComponent
import components.loanProducts.ProductComponent
import components.shortTermLoans.ShortTermLoansComponent

interface RootLoansComponent {
    val childLoansStack:Value<ChildStack<*, ChildLoans>>


    sealed class  ChildLoans{
        class ApplyLoanChild(val component: ApplyLoanComponent):ChildLoans()

        class ShortTermLoansChild(val component: ShortTermLoansComponent):ChildLoans()

        class LongTermLoansChild(val component: LongTermLoansComponent):ChildLoans()
       // class ProductLoansChild(val component: ProductComponent):ChildLoans()
        class EmergencyLoanChild(val component: EmergencyLoansComponent):ChildLoans()

        // lo

    }

}