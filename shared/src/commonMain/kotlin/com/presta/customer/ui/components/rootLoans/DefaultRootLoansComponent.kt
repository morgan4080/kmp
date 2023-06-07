package com.presta.customer.ui.components.rootLoans

import ApplyLoanComponent
import FailedTransactionComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.network.payments.model.PaymentStatuses
import com.presta.customer.ui.components.applyLoan.DefaultApplyLoanComponent
import com.presta.customer.ui.components.banKDisbursement.BankDisbursementComponent
import com.presta.customer.ui.components.banKDisbursement.DefaultBankDisbursementComponent
import com.presta.customer.ui.components.failedTransaction.DefaultFailedTransactionComponent
import com.presta.customer.ui.components.loanConfirmation.DefaultLoanConfirmationComponent
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent
import com.presta.customer.ui.components.longTermLoans.DefaultLongTermComponent
import com.presta.customer.ui.components.longTermLoans.LongTermLoansComponent
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent
import com.presta.customer.ui.components.processingTransaction.DefaultProcessingTransactionComponent
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionComponent
import com.presta.customer.ui.components.shortTermLoans.DefaultShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.specificLoanType.DefaultSpecificLoansComponent
import com.presta.customer.ui.components.specificLoanType.SpecificLoansComponent
import com.presta.customer.ui.components.succesfulTransaction.DefaultSuccessfulTransactionComponent
import com.presta.customer.ui.components.succesfulTransaction.SuccessfulTransactionComponent
import com.presta.customer.ui.components.topUp.DefaultLoanTopUpComponent
import com.presta.customer.ui.components.topUp.LoanTopUpComponent
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.modeofDisbursement.DefaultModeOfDisbursementComponent

class DefaultRootLoansComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    val pop: () -> Unit = {}
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

        is ConfigLoans.SpecificLoan -> RootLoansComponent.ChildLoans.EmergencyLoanChild(
            specificLoanComponent(componentContext, config)
        )

        is ConfigLoans.LoanConfirmation -> RootLoansComponent.ChildLoans.ConfirmLoanChild(
            loanConfirmationComponent(componentContext, config)
        )

        is ConfigLoans.DisbursementMethod -> RootLoansComponent.ChildLoans.DisbursementModeChild(
            modeOfDisbursementComponent(componentContext,config)
        )

        is ConfigLoans.ProcessingTransaction -> RootLoansComponent.ChildLoans.ProcessingTransactionChild(
            processingTransactionComponent(componentContext, config)
        )

        is ConfigLoans.BankDisbursement -> RootLoansComponent.ChildLoans.BankDisbursementChild(
            bankDisbursementComponent(componentContext)
        )

        is ConfigLoans.SuccessfulTransaction -> RootLoansComponent.ChildLoans.SuccessfulTransactionChild(
            successfulTransactionComponent(componentContext)
        )

        is ConfigLoans.FailedTransaction -> RootLoansComponent.ChildLoans.FailedTransactionChild(
            failedTransactionComponent(componentContext)
        )

        is ConfigLoans.LoanTopUp -> RootLoansComponent.ChildLoans.LoanTopUpChild(
            loanTopUpComponent(componentContext,config)
        )

    }

    private fun applyLoanComponent(componentContext: ComponentContext): ApplyLoanComponent =
        DefaultApplyLoanComponent(
            componentContext = componentContext,
            onShortTermClicked = {
                loansNavigation.push(ConfigLoans.ShortTermLoans)
            }, onLongTermClicked = {
                loansNavigation.push(ConfigLoans.LongTermLoans)
            },
            onBackNavClicked = {
                pop()


            },
            onPop = {
                pop()
            }
        )
    //Pass the RefId
    private fun shortTermComponent(componentContext: ComponentContext): ShortTermLoansComponent =
        DefaultShortTermLoansComponent(
            componentContext = componentContext,

            onConfirmClicked = {
                //Navigation to TopUp Screen
                loansNavigation.push(ConfigLoans.LoanTopUp(refId = ""))
            },
            onBackNavClicked = {
                loansNavigation.pop()

            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            onProductClicked = { refId ->
                loansNavigation.push(ConfigLoans.SpecificLoan(refId))
            }
        )
    private fun longTermComponent(componentContext: ComponentContext): LongTermLoansComponent =
        DefaultLongTermComponent(componentContext = componentContext,
            onProductSelected = { refId ->
                // loansNavigation.push(ConfigLoans.LoanProduct(refId = refId))
                //Get data on the specif  RefId
                // loansNavigation.push(ConfigLoans.EmergencyLoan(refId = refId))
            })
    private fun specificLoanComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.SpecificLoan
    ): SpecificLoansComponent =
        DefaultSpecificLoansComponent(
            componentContext = componentContext,
            refId = config.refId,
            onConfirmClicked = { refId, amount,loanPeriod,loanType ->
                println("Ref id")
                println(refId)
                println("Amount")
                println(amount)
                loansNavigation.push(ConfigLoans.LoanConfirmation(refId = refId, amount = amount, loanPeriod =loanPeriod, loanType =loanType ))
            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory
        )

    private fun loanConfirmationComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.LoanConfirmation
    ): LoanConfirmationComponent =
        DefaultLoanConfirmationComponent(
            componentContext = componentContext,
            refId = config.refId,
            onConfirmClicked = { refId, amount,loanPeriod,loantype ->
                //navigate to mode of Disbursement
                loansNavigation.push(ConfigLoans.DisbursementMethod(refId = refId, amount = amount,loanPeriod=loanPeriod, loanType = loantype))
            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            amount = config.amount,
            loanPeriod =config.loanPeriod
        )

    private fun modeOfDisbursementComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.DisbursementMethod): ModeOfDisbursementComponent =
        DefaultModeOfDisbursementComponent(
            componentContext = componentContext,
            onMpesaClicked = { amount, fees ->

            },
            onBankClicked = {
                //navigate to  BankDisbursement Screen
                loansNavigation.push(ConfigLoans.BankDisbursement)

            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            TransactionSuccessful = {
                loansNavigation.push(ConfigLoans.SuccessfulTransaction)
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            refId = config.refId,
            amount = config.amount,
            loanPeriod = config.loanPeriod,
            loanType = config.loanType
        )

    private fun processingTransactionComponent(componentContext: ComponentContext, config: ConfigLoans.ProcessingTransaction): ProcessingTransactionComponent =
        DefaultProcessingTransactionComponent(
            storeFactory = storeFactory,
            componentContext = componentContext,
            correlationId = config.correlationId,
            amount = config.amount,
            mainContext = prestaDispatchers.main,
            onPop = {
                loansNavigation.pop()
            },
            navigateToCompleteFailure = { paymentStatus ->
                if (paymentStatus == PaymentStatuses.COMPLETED) {
                    loansNavigation.push(ConfigLoans.SuccessfulTransaction)
                }

                if (paymentStatus == PaymentStatuses.FAILURE
                    || paymentStatus == PaymentStatuses.CANCELLED) {
                    loansNavigation.push(ConfigLoans.FailedTransaction)
                }
            }
        )

    private fun bankDisbursementComponent(componentContext: ComponentContext): BankDisbursementComponent =
        DefaultBankDisbursementComponent(
            componentContext = componentContext,
            onConfirmClicked = {
                loansNavigation.push(ConfigLoans.SuccessfulTransaction)
            },
            onBackNavClicked = {
                loansNavigation.pop()
            })

    private fun successfulTransactionComponent(componentContext: ComponentContext): SuccessfulTransactionComponent =
        DefaultSuccessfulTransactionComponent(
            componentContext = componentContext,
        )

    private fun failedTransactionComponent(componentContext: ComponentContext): FailedTransactionComponent =
        DefaultFailedTransactionComponent(
            componentContext = componentContext,
            onRetryClicked = {
            }
        )

    private fun loanTopUpComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.LoanTopUp): LoanTopUpComponent =
        DefaultLoanTopUpComponent(
            componentContext = componentContext,
            onConfirmClicked = {
                //push  to confirm Loan Details Screen
                loansNavigation.push(ConfigLoans.LoanConfirmation(refId = "", amount = 0.0,"",""))

            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            refId = config.refId

        )

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
        data class SpecificLoan(val refId: String) : ConfigLoans()

        @Parcelize
        data class LoanConfirmation(val refId: String, val amount: Double,val loanPeriod: String,val loanType :String) : ConfigLoans()
        @Parcelize
        data class DisbursementMethod(val refId: String, val amount: Double,val loanPeriod: String,val loanType: String) : ConfigLoans()

        @Parcelize
        data class ProcessingTransaction(val correlationId: String, val amount: Double) : ConfigLoans()

        @Parcelize
        object BankDisbursement : ConfigLoans()

        @Parcelize
        object SuccessfulTransaction : ConfigLoans()

        @Parcelize
        object FailedTransaction : ConfigLoans()

        @Parcelize
        data class LoanTopUp(val refId: String) : ConfigLoans()

    }
}