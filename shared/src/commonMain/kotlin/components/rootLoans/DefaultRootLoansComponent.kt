package components.rootLoans

import ApplyLoanComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import components.applyLoan.DefaultApplyLoanComponent
import components.emergencyLoans.DefaultEmergencyLoansComponent
import components.emergencyLoans.EmergencyLoansComponent
import components.longTermLoans.DefaultLongTermComponent
import components.longTermLoans.LongTermLoansComponent
import components.shortTermLoans.DefaultShortTernLoansComponent
import components.shortTermLoans.ShortTermLoansComponent

class DefaultRootLoansComponent(
    componentContext: ComponentContext,
): RootLoansComponent, ComponentContext by componentContext {
    private val loansNavigation = StackNavigation<ConfigLoans>()

    private val _childLoansStack =
        childStack(
            source = loansNavigation,
            initialConfiguration = ConfigLoans.ApplyLoan,
            handleBackButton = true,
            childFactory = ::createLoansChild,
            key = "applyLoansStack"
        )

    override val childLoansStack: Value<ChildStack<*, RootLoansComponent.ChildLoans>> = _childLoansStack

    private fun createLoansChild(config: ConfigLoans, componentContext: ComponentContext): RootLoansComponent.ChildLoans =
        when (config) {
            is ConfigLoans.ApplyLoan -> RootLoansComponent.ChildLoans.ApplyLoanChild(applyLoanComponent(componentContext))
            is ConfigLoans.LongTermLoans -> RootLoansComponent.ChildLoans.LongTermLoansChild(longTermComponent(componentContext))
            is ConfigLoans.ShortTermLoans -> RootLoansComponent.ChildLoans.ShortTermLoansChild(shortTermComponent(componentContext))
           // is ConfigLoans.LoanProduct->RootLoansComponent.ChildLoans.ProductLoansChild(loanProductComponent(componentContext, config))
            is ConfigLoans.EmergencyLoan->RootLoansComponent.ChildLoans.EmergencyLoanChild(emergencyLoanComponent(componentContext))
        }

    private fun applyLoanComponent(componentContext: ComponentContext): ApplyLoanComponent =
        DefaultApplyLoanComponent(
            componentContext = componentContext,
            onShortTermClicked = {
                loansNavigation.push(ConfigLoans.ShortTermLoans)
            },
            onLongTermClicked = {
                loansNavigation.push(ConfigLoans.LongTermLoans)
            }
        )

    private fun shortTermComponent(componentContext: ComponentContext): ShortTermLoansComponent =
        DefaultShortTernLoansComponent(
            componentContext = componentContext
        )

    private fun longTermComponent(componentContext: ComponentContext): LongTermLoansComponent =
        DefaultLongTermComponent(
            componentContext = componentContext,
            onProductSelected = { refId ->
               // loansNavigation.push(ConfigLoans.LoanProduct(refId = refId))
            }
        )

//    private fun loanProductComponent(componentContext: ComponentContext, config: ConfigLoans.LoanProduct): ProductComponent =
//        DefaultProductComponent(
//            componentContext = componentContext,
//            refId = config.refId,
//            onEmergencyLoanClicked = {
//                //Navigate to Emergency Loan Screen
//                loansNavigation.push(ConfigLoans.EmergencyLoan)
//
//            }
//        )

    private fun emergencyLoanComponent(componentContext: ComponentContext): EmergencyLoansComponent =
        DefaultEmergencyLoansComponent(
            componentContext = componentContext
        )


    private sealed class ConfigLoans : Parcelable {
        @Parcelize
        object ApplyLoan: ConfigLoans()

        @Parcelize
        object LongTermLoans: ConfigLoans()

        @Parcelize
        object ShortTermLoans: ConfigLoans()

//        @Parcelize
//        data class LoanProduct(val  refId: String): ConfigLoans()

        @Parcelize
        object EmergencyLoan: ConfigLoans()

    }
}