package com.presta.customer.ui.components.rootLoans

import ApplyLoanComponent
import FailedTransactionComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.payments.model.PaymentStatuses
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.applyLoan.DefaultApplyLoanComponent
import com.presta.customer.ui.components.banKDisbursement.BankDisbursementComponent
import com.presta.customer.ui.components.banKDisbursement.DefaultBankDisbursementComponent
import com.presta.customer.ui.components.customerBanks.CustomerBanksComponent
import com.presta.customer.ui.components.customerBanks.DefaultCustomerBanksComponent
import com.presta.customer.ui.components.failedTransaction.DefaultFailedTransactionComponent
import com.presta.customer.ui.components.loanConfirmation.DefaultLoanConfirmationComponent
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent
import com.presta.customer.ui.components.longTermLoans.DefaultLongTermComponent
import com.presta.customer.ui.components.longTermLoans.LongTermLoansComponent
import com.presta.customer.ui.components.modeofDisbursement.DefaultModeOfDisbursementComponent
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

data class ProcessLoanDisbursement(
    val correlationId: String,
    val amount: Double,
    val fees: Double
)

class DefaultRootLoansComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    val pop: () -> Unit = {},
    val navigateToProfile: () -> Unit = {},
    var processLoanState: (state: ProcessLoanDisbursement?) -> Unit = {}
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
            shortTermComponent(componentContext,config)
        )

        is ConfigLoans.SpecificLoan -> RootLoansComponent.ChildLoans.EmergencyLoanChild(
            specificLoanComponent(componentContext, config)
        )

        is ConfigLoans.LoanConfirmation -> RootLoansComponent.ChildLoans.ConfirmLoanChild(
            loanConfirmationComponent(componentContext, config)
        )

        is ConfigLoans.DisbursementMethod -> RootLoansComponent.ChildLoans.DisbursementModeChild(
            modeOfDisbursementComponent(componentContext, config)
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
            loanTopUpComponent(componentContext, config)
        )

        is ConfigLoans.CustomerBanks -> RootLoansComponent.ChildLoans.CustomerBanksChild(
            customerBanks(componentContext, config)
        )

    }

    private fun customerBanks(componentContext: ComponentContext, config: ConfigLoans.CustomerBanks): CustomerBanksComponent =
        DefaultCustomerBanksComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            proceed = {

            },
            pop = {
                loansNavigation.pop()
            }
        )

    private fun applyLoanComponent(componentContext: ComponentContext): ApplyLoanComponent =
        DefaultApplyLoanComponent(
            componentContext = componentContext,
            onShortTermClicked = {
                loansNavigation.push(ConfigLoans.ShortTermLoans(
                    referencedLoanRefId = null
                ))
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
    private fun shortTermComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.ShortTermLoans
    ): ShortTermLoansComponent =
        DefaultShortTermLoansComponent(
            componentContext = componentContext,

            onProceedClicked = { loanRefId, referencedLoanRefId, maxAmount, minAmount, loanName, interestRate, loanPeriod, loanPeriodUnit, minLoanPeriod, loanRefIds ->
                //Navigation to TopUp Screen
                loansNavigation.push(
                    ConfigLoans.LoanTopUp(
                        referencedLoanRefId = referencedLoanRefId,
                        LoanRefId = loanRefId,
                        maxAmount = maxAmount,
                        minAmount = minAmount,
                        loanName = loanName,
                        InterestRate = interestRate,
                        enteredAmount = 0.0,
                        maxLoanPeriod = loanPeriod,
                        loanPeriodUnit = loanPeriodUnit,
                        minLoanPeriod = minLoanPeriod,
                        loanRefIds = loanRefIds,
                        loanOperation =""
                    )
                )
            },
            onBackNavClicked = {
                loansNavigation.pop()

            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            onProductClicked = { refId, loanName, referencedLoanRefId ->
                loansNavigation.push(
                    ConfigLoans.SpecificLoan(refId, loanName, referencedLoanRefId)
                )
            },
            referencedLoanRefId = config.referencedLoanRefId
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
            onConfirmClicked = { refId, amount, loanPeriod, loanType, LoanName, interest, loanPeriodUnit, referencedLoanRefId, currentTerm ->
                loansNavigation.push(
                    ConfigLoans.DisbursementMethod(
                        refId = refId,
                        amount = amount,
                        loanPeriod = loanPeriod,
                        loanType = loanType,
                        interestRate = interest,
                        enteredAmount = 0.00,
                        loanName = LoanName,
                        loanPeriodUnit = loanPeriodUnit,
                        loanOperation = "loan",
                        referencedLoanRefId = referencedLoanRefId,
                        currentTerm =currentTerm,
                        fees = 0.0,
                        correlationId = "",
                    )
                )
            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            loanName = config.loanName,
            loanOperation = "loan",
            referencedLoanRefId = config.referencedLoanRefId
        )

    private fun loanConfirmationComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.LoanConfirmation
    ): LoanConfirmationComponent =
        DefaultLoanConfirmationComponent(
            componentContext = componentContext,
            refId = config.refId,
            onConfirmClicked = { correlationId, amount, fees ->
                processLoanState(ProcessLoanDisbursement(correlationId, amount, fees))
            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            amount = config.amount,
            loanPeriod = config.loanPeriod,
            loanInterest = config.interestRate.toString(),
            loanName = config.loanName,
            loanPeriodUnit = config.loanPeriodUnit,
            loanOperation = config.loanOperation,
            loanType = config.loanType,
            referencedLoanRefId = config.referencedLoanRefId,
            currentTerm = config.currentTerm
        )

    private fun modeOfDisbursementComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.DisbursementMethod
    ): ModeOfDisbursementComponent =
        DefaultModeOfDisbursementComponent(
            componentContext = componentContext,
            onMpesaClicked = { correlationId, refId, amount, fees, loanPeriod, loanType, interestRate, LoanName, loanPeriodUnit, referencedLoanRefId, currentTerm,loanOperation ->
                loansNavigation.push(
                    ConfigLoans.LoanConfirmation(
                        correlationId = correlationId,
                        refId = refId,
                        amount = amount,
                        fees = fees,
                        loanPeriod = loanPeriod,
                        loanType = loanType,
                        interestRate = interestRate,
                        loanName = LoanName,
                        loanPeriodUnit = loanPeriodUnit,
                        loanOperation = loanOperation,
                        referencedLoanRefId = referencedLoanRefId,
                        currentTerm = currentTerm
                    )
                )
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
            loanType = config.loanType,
            fees = config.fees,
            referencedLoanRefId = config.referencedLoanRefId,
            currentTerm = config.currentTerm,
            correlationId = config.correlationId,
            interestRate = config.interestRate,
            loanName =config.loanName ,
            loanPeriodUnit = config.loanPeriodUnit,
            loanOperation = config.loanOperation
        )

    private fun processingTransactionComponent(
        componentContext: ComponentContext,
        config: ConfigLoans.ProcessingTransaction
    ): ProcessingTransactionComponent =
        DefaultProcessingTransactionComponent(
            storeFactory = storeFactory,
            componentContext = componentContext,
            correlationId = config.correlationId,
            amount = config.amount,
            mainContext = prestaDispatchers.main,
            navigateToCompleteFailure = { paymentStatus ->
                if (paymentStatus == PaymentStatuses.COMPLETED) {
                    loansNavigation.push(ConfigLoans.SuccessfulTransaction)
                }

                /*if (paymentStatus == PaymentStatuses.FAILURE
                    || paymentStatus == PaymentStatuses.CANCELLED
                ) {
                    loansNavigation.push(ConfigLoans.FailedTransaction)
                }*/
            },
            onPop = {
                loansNavigation.pop()
            },
            navigateToProfile = {
                navigateToProfile()
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
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory
        )

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
        config: ConfigLoans.LoanTopUp
    ): LoanTopUpComponent =
        DefaultLoanTopUpComponent(
            componentContext = componentContext,
            onProceedClicked = {referencedLoanRefId, refId, amount, maxLoanPeriod, loanType, LoanName, interest, loanPeriodUnit, currentTerm, minLoanPeriod,loanOperation ->
                loansNavigation.push(
                    ConfigLoans.DisbursementMethod(
                        referencedLoanRefId = referencedLoanRefId,
                        refId = refId,
                        amount = amount,
                        loanPeriod = maxLoanPeriod,
                        loanType = loanType,
                        interestRate = interest,
                        enteredAmount = 0.00,
                        loanName = LoanName,
                        loanPeriodUnit = loanPeriodUnit,
                        loanOperation = loanOperation,
                        currentTerm = currentTerm,
                        fees = 0.0,
                        correlationId = "",

                    )
                )
            },
            onBackNavClicked = {
                loansNavigation.pop()
            },
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            maxAmount = config.maxAmount,
            minAmount = config.minAmount.toString(),
            loanRefId = config.LoanRefId,
            loanName = config.loanName,
            interestRate = config.InterestRate,
            maxLoanPeriod = config.maxLoanPeriod,
            loanPeriodUnit = config.loanPeriodUnit,
            loanOperation = config.loanOperation,
            referencedLoanRefId = config.referencedLoanRefId,
            minLoanPeriod = config.minLoanPeriod,
            loanRefIds = config.loanRefIds

        )

    private sealed class ConfigLoans : Parcelable {
        @Parcelize
        object ApplyLoan : ConfigLoans()

        @Parcelize
        object LongTermLoans : ConfigLoans()
        @Parcelize
        data class ShortTermLoans(  val referencedLoanRefId: String?) :ConfigLoans()

        @Parcelize
        data class SpecificLoan(
            val refId: String,
            val loanName: String,
            val referencedLoanRefId: String?
        ) : ConfigLoans()

        @Parcelize
        data class LoanConfirmation(
            val correlationId: String,
            val refId: String,
            val amount: Double,
            val fees: Double,
            val loanPeriod: Int,
            val loanType: LoanType,
            val interestRate: Double,
            val loanName: String,
            val loanPeriodUnit: String,
            val loanOperation: String,
            val referencedLoanRefId: String?,
            val currentTerm:Boolean
        ) : ConfigLoans()

        @Parcelize
        data class DisbursementMethod(
            val referencedLoanRefId: String?,
            val refId: String,
            val amount: Double,
            val loanPeriod: Int,
            val loanType: LoanType,
            val interestRate: Double,
            val enteredAmount: Double,
            val loanPeriodUnit: String,
            val loanOperation: String,
            val currentTerm: Boolean,
            val fees: Double,
            val correlationId: String,
            val loanName: String,

            ) : ConfigLoans()

        @Parcelize
        data class ProcessingTransaction(val correlationId: String, val amount: Double) :
            ConfigLoans()
        @Parcelize
        object BankDisbursement : ConfigLoans()

        @Parcelize
        object SuccessfulTransaction : ConfigLoans()
        @Parcelize
        object FailedTransaction : ConfigLoans()
        @Parcelize
        data class LoanTopUp(
            val referencedLoanRefId: String,
            val LoanRefId: String,
            val maxAmount: Double,
            val minAmount: Double,
            val loanName: String,
            val InterestRate: Double,
            val enteredAmount: Double,
            val maxLoanPeriod: Int,
            val loanPeriodUnit: String,
            val minLoanPeriod: Int,
            val loanRefIds: String,
            val loanOperation: String,

        ) : ConfigLoans()

        @Parcelize
        object CustomerBanks: ConfigLoans()
    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    super.onResume()
                    loansNavigation.replaceAll(ConfigLoans.ApplyLoan)
                }
            }
        )
    }
}