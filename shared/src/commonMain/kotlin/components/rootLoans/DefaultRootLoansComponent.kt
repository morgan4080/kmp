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
import components.banKDisbursement.BankDisbursementComponent
import components.banKDisbursement.DefaultBankDisbursementComponent
import components.emergencyLoans.DefaultEmergencyLoansComponent
import components.emergencyLoans.EmergencyLoansComponent
import components.loanConfirmation.DefaultLoanConfirmationComponent
import components.loanConfirmation.LoanConfirmationComponent
import components.longTermLoans.DefaultLongTermComponent
import components.longTermLoans.LongTermLoansComponent
import components.modeofDisbursement.DefaultModeOfDisbursementComponent
import components.modeofDisbursement.ModeOfDisbursementComponent
import components.processingTransaction.DefaultProcessingTransactionComponent
import components.processingTransaction.ProcessingTransactionComponent
import components.shortTermLoans.DefaultShortTernLoansComponent
import components.shortTermLoans.ShortTermLoansComponent

class DefaultRootLoansComponent(
    componentContext: ComponentContext,
) : RootLoansComponent, ComponentContext by componentContext {
    private val loansNavigation = StackNavigation<ConfigLoans>()

    private val _childLoansStack = childStack(
        source = loansNavigation,
        initialConfiguration = ConfigLoans.ApplyLoan,
        handleBackButton = true,
        childFactory = ::createLoansChild,
        key = "applyLoansStack"
    )

    override val childLoansStack: Value<ChildStack<*, RootLoansComponent.ChildLoans>> =
        _childLoansStack

    private fun createLoansChild(
        config: ConfigLoans, componentContext: ComponentContext
    ): RootLoansComponent.ChildLoans = when (config) {
        is ConfigLoans.ApplyLoan -> RootLoansComponent.ChildLoans.ApplyLoanChild(
            applyLoanComponent(componentContext)
        )

        is ConfigLoans.LongTermLoans -> RootLoansComponent.ChildLoans.LongTermLoansChild(
            longTermComponent(componentContext)
        )

        is ConfigLoans.ShortTermLoans -> RootLoansComponent.ChildLoans.ShortTermLoansChild(
            shortTermComponent(componentContext)
        )

        is ConfigLoans.EmergencyLoan -> RootLoansComponent.ChildLoans.EmergencyLoanChild(
            emergencyLoanComponent(componentContext)
        )

        is ConfigLoans.LoanConfirmation -> RootLoansComponent.ChildLoans.ConfirmLoanChild(
            loanConfirmationComponent(componentContext)
        )

        is ConfigLoans.DisbursementMethod -> RootLoansComponent.ChildLoans.DisbursementModeChild(
            modeOfDisbursementComponent(componentContext)
        )

        is ConfigLoans.ProcessingTransaction -> RootLoansComponent.ChildLoans.ProcessingTransactionChild(
            processingTransactionComponent(componentContext)
        )

        is ConfigLoans.BankDisbursement -> RootLoansComponent.ChildLoans.BankDisbursementChild(
            bankDisbursementComponent(componentContext)
        )
    }

    private fun applyLoanComponent(componentContext: ComponentContext): ApplyLoanComponent =
        DefaultApplyLoanComponent(componentContext = componentContext, onShortTermClicked = {
            loansNavigation.push(ConfigLoans.ShortTermLoans)
        }, onLongTermClicked = {
            loansNavigation.push(ConfigLoans.LongTermLoans)
        })

    private fun shortTermComponent(componentContext: ComponentContext): ShortTermLoansComponent =
        DefaultShortTernLoansComponent(
            componentContext = componentContext,
            onProductSelected = { refId ->
                loansNavigation.push(ConfigLoans.EmergencyLoan(refId = "em"))
            })

    private fun longTermComponent(componentContext: ComponentContext): LongTermLoansComponent =
        DefaultLongTermComponent(componentContext = componentContext, onProductSelected = { refId ->
            // loansNavigation.push(ConfigLoans.LoanProduct(refId = refId))
            loansNavigation.push(ConfigLoans.EmergencyLoan(refId = "em"))

        })

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
        DefaultEmergencyLoansComponent(componentContext = componentContext, onConfirmClicked = {
            //Navigate to  confirm Screen- a child under loans
            loansNavigation.push(ConfigLoans.LoanConfirmation)
        })

    private fun loanConfirmationComponent(componentContext: ComponentContext): LoanConfirmationComponent =
        DefaultLoanConfirmationComponent(componentContext = componentContext, onConfirmClicked = {
            //navigate to mode of Disbursement
            loansNavigation.push(ConfigLoans.DisbursementMethod)
        })

    private fun modeOfDisbursementComponent(componentContext: ComponentContext): ModeOfDisbursementComponent =
        DefaultModeOfDisbursementComponent(componentContext = componentContext, onMpesaClicked = {
            //Navigate to processing payment screen
            // loansNavigation.push()
            loansNavigation.push(ConfigLoans.ProcessingTransaction)

        }, onBankClicked = {
            //navigate to  BankDisbursement Screen
            loansNavigation.push(ConfigLoans.BankDisbursement)

        })

    private fun processingTransactionComponent(componentContext: ComponentContext): ProcessingTransactionComponent =
        DefaultProcessingTransactionComponent(
            componentContext = componentContext
        )

    private fun bankDisbursementComponent(componentContext: ComponentContext): BankDisbursementComponent =
        DefaultBankDisbursementComponent(componentContext = componentContext,
            onConfirmClicked = {
            //Proceed  to show processing,--show failed or successful based on state
                loansNavigation.push(ConfigLoans.ProcessingTransaction)

        })

    private sealed class ConfigLoans : Parcelable {
        @Parcelize
        object ApplyLoan : ConfigLoans()

        @Parcelize
        object LongTermLoans : ConfigLoans()

        @Parcelize
        object ShortTermLoans : ConfigLoans()

//        @Parcelize
//        data class LoanProduct(val  refId: String): ConfigLoans()

        @Parcelize
        data class EmergencyLoan(val refId: String) : ConfigLoans()

        @Parcelize
        object LoanConfirmation : ConfigLoans()

        @Parcelize
        object DisbursementMethod : ConfigLoans()

        @Parcelize
        object ProcessingTransaction : ConfigLoans()

        @Parcelize
        object BankDisbursement : ConfigLoans()

    }
}