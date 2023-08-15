package com.presta.customer.ui.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.network.payments.model.PaymentStatuses
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.auth.AuthComponent
import com.presta.customer.ui.components.auth.DefaultAuthComponent
import com.presta.customer.ui.components.onBoarding.DefaultOnboardingComponent
import com.presta.customer.ui.components.onBoarding.OnBoardingComponent
import com.presta.customer.ui.components.otp.DefaultOtpComponent
import com.presta.customer.ui.components.otp.OtpComponent
import com.presta.customer.ui.components.payLoan.DefaultPayLoanComponent
import com.presta.customer.ui.components.payLoan.PayLoanComponent
import com.presta.customer.ui.components.payLoanPropmpt.DefaultPayLoanPromptComponent
import com.presta.customer.ui.components.payLoanPropmpt.PayLoanPromptComponent
import com.presta.customer.ui.components.payRegistrationFeePrompt.DefaultPayRegistrationFeeComponent
import com.presta.customer.ui.components.payRegistrationFeePrompt.PayRegistrationFeeComponent
import com.presta.customer.ui.components.pendingApprovals.DefaultLoanPendingApprovalsComponent
import com.presta.customer.ui.components.pendingApprovals.LoanPendingApprovalsComponent
import com.presta.customer.ui.components.processLoanDisbursement.DefaultProcessLoanDisbursementComponent
import com.presta.customer.ui.components.processLoanDisbursement.ProcessLoanDisbursementComponent
import com.presta.customer.ui.components.processingTransaction.DefaultProcessingTransactionComponent
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionComponent
import com.presta.customer.ui.components.profile.coroutineScope
import com.presta.customer.ui.components.registration.DefaultRegistrationComponent
import com.presta.customer.ui.components.registration.RegistrationComponent
import com.presta.customer.ui.components.rootBottomStack.DefaultRootBottomComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomComponent
import com.presta.customer.ui.components.splash.DefaultSplashComponent
import com.presta.customer.ui.components.splash.SplashComponent
import com.presta.customer.ui.components.tenant.DefaultTenantComponent
import com.presta.customer.ui.components.tenant.TenantComponent
import com.presta.customer.ui.components.transactionHistory.DefaultTransactionHistoryComponent
import com.presta.customer.ui.components.transactionHistory.TransactionHistoryComponent
import com.presta.customer.ui.components.welcome.DefaultWelcomeComponent
import com.presta.customer.ui.components.welcome.WelcomeComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)
class DefaultRootComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val _childStack =
        childStack(
            source = navigation,
            initialConfiguration = Config.Splash,
            handleBackButton = true,
            childFactory = ::createChild,
            key = "onboardingStack"
        )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = _childStack

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Tenant -> RootComponent.Child.TenantChild(tenantComponent(componentContext, config))
            is Config.Splash -> RootComponent.Child.SplashChild(splashComponent(componentContext))
            is Config.Welcome -> RootComponent.Child.WelcomeChild(welcomeComponent(componentContext, config))
            is Config.OnBoarding -> RootComponent.Child.OnboardingChild(onBoardingComponent(componentContext, config))
            is Config.OTP -> RootComponent.Child.OTPChild(otpComponent(componentContext, config))
            is Config.Register -> RootComponent.Child.RegisterChild(registerComponent(componentContext, config))
            is Config.Auth -> RootComponent.Child.AuthChild(authComponent(componentContext, config))
            is Config.RootBottom -> RootComponent.Child.RootBottomChild(rootBottomComponent(componentContext, config))
            is Config.AllTransactions -> RootComponent.Child.AllTransactionsChild(allTransactionHistory(componentContext))
            is Config.PayLoanPrompt->RootComponent.Child.PayLoanPromptChild(payLoanPromptComponent(componentContext, config))
            is Config.PayRegistrationFee->RootComponent.Child.PayRegistrationFeeChild(payRegistrationFeeComponent(componentContext, config))
            is Config.PayLoan->RootComponent.Child.PayLoanChild(payLoanComponent(componentContext))
            is Config.ProcessingTransaction->RootComponent.Child.ProcessingTransactionChild(processingTransactionComponent(componentContext, config))
            is Config.ProcessingLoanLoanDisbursement -> RootComponent.Child.ProcessingLoanDisbursementChild(
                processingLoanLoanDisbursementComponent(componentContext, config)
            )
            is Config.LoanPendingApprovals ->  RootComponent.Child.LoanPendingApprovalsChild(
                loansPendingApprovalComponent(componentContext)
            )
        }

    private fun loansPendingApprovalComponent(componentContext: ComponentContext): LoanPendingApprovalsComponent =
        DefaultLoanPendingApprovalsComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onBack = {
                navigation.pop()
            }
        )

    private fun tenantComponent(componentContext: ComponentContext, config: Config.Tenant): TenantComponent =
        DefaultTenantComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onBoardingContext = config.onBoardingContext,
            onSubmit = { onBoardingContext, tenantId ->
                navigation.push(Config.OnBoarding(
                    onBoardingContext = onBoardingContext,
                    tenantId = tenantId
                ))
            },

        )

    private fun splashComponent(componentContext: ComponentContext): SplashComponent =
        DefaultSplashComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onSignUp = {
                navigation.push(Config.Welcome(context = OnBoardingContext.REGISTRATION))
            },
            onSignIn = {
                navigation.push(Config.Welcome(context = OnBoardingContext.LOGIN))
            },
            navigateToAuth = { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext, pinStatus ->
                navigation.replaceAll(Config.Auth(
                    memberRefId = memberRefId,
                    phoneNumber = phoneNumber,
                    isTermsAccepted = isTermsAccepted,
                    isActive = isActive,
                    onBoardingContext = onBoardingContext,
                    pinStatus = pinStatus
                ))
            }
        )

    private fun welcomeComponent(componentContext: ComponentContext, config: Config.Welcome): WelcomeComponent =
        DefaultWelcomeComponent(
            componentContext = componentContext,
            onBoardingContext = config.context,
            onGetStartedSelected = {
                println("::::::OrganisationModel.organisation.sandbox:::::::")
                println(OrganisationModel.organisation.sandbox)
                if (OrganisationModel.organisation.sandbox) {
                    navigation.push(
                        Config.Tenant(
                            onBoardingContext = it
                        )
                    )
                } else {
                    navigation.push(
                        Config.OnBoarding(
                            onBoardingContext = it
                        )
                    )
                }
            },
        )

    private fun onBoardingComponent(componentContext: ComponentContext, config: Config.OnBoarding): OnBoardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            onBoardingContext = config.onBoardingContext,
            tenantId = config.tenantId,
            onPush = { memberRefId, phoneNumber, isActive, isTermsAccepted, onBoardingContext, pinStatus ->
                 navigation.push(
                     Config.OTP(
                         memberRefId = memberRefId,
                         onBoardingContext = onBoardingContext,
                         phoneNumber = phoneNumber,
                         isActive = isActive,
                         isTermsAccepted = isTermsAccepted,
                         pinStatus = pinStatus
                    )
                 )
            },
        )

    private fun otpComponent(componentContext: ComponentContext, config: Config.OTP): OtpComponent =
        DefaultOtpComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            memberRefId = config.memberRefId,
            onBoardingContext = config.onBoardingContext,
            phoneNumber = config.phoneNumber,
            isActive = config.isActive,
            pinStatus = config.pinStatus,
            isTermsAccepted = config.isTermsAccepted
        ) { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext, pinStatus ->
            when (onBoardingContext) {
                OnBoardingContext.REGISTRATION -> navigation.push(
                    Config.Register(
                        phoneNumber = phoneNumber,
                        isTermsAccepted = isTermsAccepted,
                        isActive = isActive,
                        onBoardingContext = onBoardingContext,
                        pinStatus = pinStatus
                    )
                )

                OnBoardingContext.LOGIN -> {
                    if (memberRefId !== null) navigation.push(
                        Config.Auth(
                            memberRefId = memberRefId,
                            phoneNumber = phoneNumber,
                            isTermsAccepted = isTermsAccepted,
                            isActive = isActive,
                            onBoardingContext = onBoardingContext,
                            pinStatus = pinStatus
                        )
                    )
                }
            }
        }

    private fun registerComponent(componentContext: ComponentContext, config: Config.Register): RegistrationComponent =
        DefaultRegistrationComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            phoneNumber = config.phoneNumber,
            isTermsAccepted = config.isTermsAccepted,
            isActive = config.isActive,
            pinStatus = config.pinStatus,
            onBoardingContext = config.onBoardingContext,
            onRegistered = { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext, pinStatus ->
                navigation.push(Config.Auth(
                    memberRefId = memberRefId,
                    phoneNumber = phoneNumber,
                    isTermsAccepted = isTermsAccepted,
                    isActive = isActive,
                    onBoardingContext = onBoardingContext,
                    pinStatus = pinStatus
                ))
            }
        )

    private fun authComponent(componentContext: ComponentContext, config: Config.Auth): AuthComponent =
        DefaultAuthComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            phoneNumber = config.phoneNumber,
            isTermsAccepted = config.isTermsAccepted,
            isActive = config.isActive,
            pinStatus = config.pinStatus,
            onBoardingContext = config.onBoardingContext,
            onLogin = {
                navigation.replaceAll(Config.RootBottom(false))
            }
        )

    val scope = coroutineScope(prestaDispatchers.main + SupervisorJob())

    private fun rootBottomComponent(componentContext: ComponentContext, config: Config.RootBottom): RootBottomComponent =
        DefaultRootBottomComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            gotoAllTransactions = {
                navigation.push(Config.AllTransactions)
            },
            gotToPendingApprovals = {
                navigation.push(Config.LoanPendingApprovals)
            },
            logoutToSplash = {
                println("::::::should logout here::::::")
                scope.launch {
                    if (it) {
                        navigation.replaceAll(Config.Splash)
                    }
                }
            },
            gotoPayLoans = {
                navigation.bringToFront(Config.PayLoan)
            },
            gotoPayRegistrationFees = { correlationId, amount ->
                navigation.bringToFront(Config.ProcessingTransaction(amount = amount, correlationId = correlationId, mode = PaymentTypes.MEMBERSHIPFEES))
            },
            processTransaction = {correlationId, amount, mode ->
                navigation.bringToFront(Config.ProcessingTransaction(correlationId, amount, mode))
            },
            processLoanState = {
                scope.launch {
                    if (it !== null) {
                        navigation.bringToFront(Config.ProcessingLoanLoanDisbursement(
                            it.correlationId,
                            it.amount,
                            it.fees
                        ))
                    }
                }
            },
            backTopProfile = config.backTopProfile
        )

    private fun allTransactionHistory(componentContext: ComponentContext): TransactionHistoryComponent =
        DefaultTransactionHistoryComponent(
            componentContext = componentContext,
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            onPop = {
                navigation.pop()
            }
        )

    private fun payLoanComponent(componentContext: ComponentContext): PayLoanComponent =
        DefaultPayLoanComponent(
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            componentContext = componentContext,
            onPayClicked = { amount, correlationId ->
                navigation.push(Config.ProcessingTransaction(amount = amount.toDouble(), correlationId = correlationId, mode = PaymentTypes.LOAN))
            },
            onPop = {
                navigation.pop()
            }
        )
    private fun payLoanPromptComponent(componentContext: ComponentContext, config: Config.PayLoanPrompt): PayLoanPromptComponent =
        DefaultPayLoanPromptComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            amount = config.amount,
            correlationId = config.correlationId,
            navigateToCompleteFailure =  { paymentStatus ->
                if (paymentStatus == PaymentStatuses.COMPLETED) {
                    println("navigate to success")
                }
            }
        )
    private fun payRegistrationFeeComponent(componentContext: ComponentContext, config: Config.PayRegistrationFee): PayRegistrationFeeComponent =
        DefaultPayRegistrationFeeComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            amount = config.amount,
            correlationId = config.correlationId,
            navigateToCompleteFailure =  { paymentStatus ->
                if (paymentStatus == PaymentStatuses.COMPLETED) {
                    println("navigate to success")
                }
            }
        )


    private fun processingTransactionComponent(componentContext: ComponentContext, config: Config.ProcessingTransaction): ProcessingTransactionComponent =
        DefaultProcessingTransactionComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            correlationId = config.correlationId,
            amount = config.amount,
            navigateToCompleteFailure = { paymentStatus ->
                if (paymentStatus == PaymentStatuses.COMPLETED) {
                    println("::::::::Show COMPLETED")
                }
            },
            onPop = {
                navigation.pop()
            },
            navigateToProfile = {
                navigation.replaceAll(Config.RootBottom(false))
            }
        )


    private fun processingLoanLoanDisbursementComponent(
        componentContext: ComponentContext,
        config: Config.ProcessingLoanLoanDisbursement
    ): ProcessLoanDisbursementComponent =
        DefaultProcessLoanDisbursementComponent(
            storeFactory = storeFactory,
            componentContext = componentContext,
            requestId = config.correlationId,
            amount = config.amount,
            mainContext = prestaDispatchers.main,
            fees = config.fees,
            navigateToCompleteFailure = {
                navigation.replaceAll(Config.RootBottom(true))
            }
        )

    enum class OnBoardingContext {
        LOGIN,
        REGISTRATION
    }

    private sealed class Config : Parcelable {
        @Parcelize
        data class Tenant(val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        object Splash : Config()
        @Parcelize
        data class Welcome (val context: OnBoardingContext) : Config()
        @Parcelize
        data class OnBoarding (val onBoardingContext: OnBoardingContext, val tenantId: String? = null) : Config()
        @Parcelize
        data class OTP (val memberRefId: String?, val onBoardingContext: OnBoardingContext, val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean, val pinStatus: PinStatus?) : Config()
        @Parcelize
        data class Auth(val memberRefId: String?, val phoneNumber: String, val isTermsAccepted: Boolean, val isActive: Boolean, val onBoardingContext: OnBoardingContext, val pinStatus: PinStatus?) : Config()
        @Parcelize
        data class Register(val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean, val onBoardingContext: OnBoardingContext, val pinStatus: PinStatus?) : Config()
        @Parcelize
        object AllTransactions : Config()
        @Parcelize
        object LoanPendingApprovals : Config()
        @Parcelize
        data class RootBottom(val backTopProfile: Boolean) : Config()
        @Parcelize
        object PayLoan :Config()
        @Parcelize
        data class PayLoanPrompt(val amount: String, val correlationId: String) :Config()
        @Parcelize
        data class PayRegistrationFee(val amount: Double, val correlationId: String) :Config()
        @Parcelize
        data class ProcessingTransaction(val correlationId: String, val amount: Double, val mode: PaymentTypes) :Config()
        @Parcelize
        data class ProcessingLoanLoanDisbursement(val correlationId: String, val amount: Double, val fees: Double) : Config()
    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    super.onResume()
                    navigation.replaceAll(Config.Splash)
                }
            }
        )
    }
}