package com.presta.customer.ui.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
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
import com.presta.customer.network.longTermLoans.model.GuarantorDataListing
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.network.payments.model.PaymentStatuses
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent
import com.presta.customer.ui.components.addGuarantors.DefaultAddGuarantorsComponent
import com.presta.customer.ui.components.addWitness.AddWitnessComponent
import com.presta.customer.ui.components.addWitness.DefaultAddWitnessComponent
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.applyLongTermLoan.DefaultApplyLongtermLoanComponent
import com.presta.customer.ui.components.auth.AuthComponent
import com.presta.customer.ui.components.auth.DefaultAuthComponent
import com.presta.customer.ui.components.favouriteGuarantors.DefaultFavouriteGuarantorsComponent
import com.presta.customer.ui.components.favouriteGuarantors.FavouriteGuarantorsComponent
import com.presta.customer.ui.components.guarantorshipRequests.DefaultGuarantorshipRequestComponent
import com.presta.customer.ui.components.guarantorshipRequests.GuarantorshipRequestComponent
import com.presta.customer.ui.components.longTermLoanConfirmation.DefaultLongTermLoanConfirmationComponent
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent
import com.presta.customer.ui.components.longTermLoanDetails.DefaultLongTermLoanDetailsComponent
import com.presta.customer.ui.components.longTermLoanDetails.LongTermLoanDetailsComponent
import com.presta.customer.ui.components.longTermLoanSignStatus.DefaultLongTermLoanSigningStatusComponent
import com.presta.customer.ui.components.longTermLoanSignStatus.LongtermLoanSigningStatusComponent
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
import com.presta.customer.ui.components.replaceGuarantor.DefaultReplaceGuarantorComponent
import com.presta.customer.ui.components.replaceGuarantor.ReplaceGuarantorComponent
import com.presta.customer.ui.components.rootBottomSign.DefaultRootBottomSignComponent
import com.presta.customer.ui.components.rootBottomSign.RootBottomSignComponent
import com.presta.customer.ui.components.rootBottomStack.DefaultRootBottomComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomComponent
import com.presta.customer.ui.components.selectLoanPurpose.DefaultSelectLoanPurposeComponent
import com.presta.customer.ui.components.selectLoanPurpose.SelectLoanPurposeComponent
import com.presta.customer.ui.components.signGuarantorForm.DefaultSignGuarantorFormComponent
import com.presta.customer.ui.components.signGuarantorForm.SignGuarantorFormComponent
import com.presta.customer.ui.components.signLoanForm.DefaultSignLoanFormComponent
import com.presta.customer.ui.components.signLoanForm.SignLoanFormComponent
import com.presta.customer.ui.components.signWitnessForm.DefaultSignWitnessFormComponent
import com.presta.customer.ui.components.signWitnessForm.SignWitnessFormComponent
import com.presta.customer.ui.components.splash.DefaultSplashComponent
import com.presta.customer.ui.components.splash.SplashComponent
import com.presta.customer.ui.components.tenant.DefaultTenantComponent
import com.presta.customer.ui.components.tenant.TenantComponent
import com.presta.customer.ui.components.transactionHistory.DefaultTransactionHistoryComponent
import com.presta.customer.ui.components.transactionHistory.TransactionHistoryComponent
import com.presta.customer.ui.components.welcome.DefaultWelcomeComponent
import com.presta.customer.ui.components.welcome.WelcomeComponent
import com.presta.customer.ui.components.witnessRequests.DefaultWitnessRequestComponent
import com.presta.customer.ui.components.witnessRequests.WitnessRequestComponent
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
    val storeFactory: StoreFactory

) : RootComponent, ComponentContext by componentContext {
    lateinit var loanRefid: String
    lateinit var passedLoanNumber: String
    lateinit var passedAmount: String
    lateinit var passedGuarantorRefId: String
    lateinit var passedMemberRefid: String
    private val navigation = StackNavigation<Config>()

    private val _childStack = childStack(
        source = navigation,
        initialConfiguration = Config.Splash,
        handleBackButton = true,
        childFactory = ::createChild,
        key = "onboardingStack"
    )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = _childStack

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.Tenant -> RootComponent.Child.TenantChild(
            tenantComponent(componentContext, config)
        )

        is Config.Splash -> RootComponent.Child.SplashChild(splashComponent(componentContext))
        is Config.Welcome -> RootComponent.Child.WelcomeChild(
            welcomeComponent(
                componentContext,
                config
            )
        )

        is Config.OnBoarding -> RootComponent.Child.OnboardingChild(
            onBoardingComponent(
                componentContext,
                config
            )
        )

        is Config.OTP -> RootComponent.Child.OTPChild(otpComponent(componentContext, config))
        is Config.Register -> RootComponent.Child.RegisterChild(
            registerComponent(
                componentContext,
                config
            )
        )

        is Config.Auth -> RootComponent.Child.AuthChild(authComponent(componentContext, config))
        is Config.RootBottom -> RootComponent.Child.RootBottomChild(
            rootBottomComponent(
                componentContext,
                config
            )
        )

        is Config.AllTransactions -> RootComponent.Child.AllTransactionsChild(
            allTransactionHistory(
                componentContext
            )
        )

        is Config.PayLoanPrompt -> RootComponent.Child.PayLoanPromptChild(
            payLoanPromptComponent(
                componentContext,
                config
            )
        )

        is Config.PayRegistrationFee -> RootComponent.Child.PayRegistrationFeeChild(
            payRegistrationFeeComponent(componentContext, config)
        )

        is Config.PayLoan -> RootComponent.Child.PayLoanChild(payLoanComponent(componentContext))
        is Config.ProcessingTransaction -> RootComponent.Child.ProcessingTransactionChild(
            processingTransactionComponent(componentContext, config)
        )

        is Config.ProcessingLoanLoanDisbursement -> RootComponent.Child.ProcessingLoanDisbursementChild(
            processingLoanLoanDisbursementComponent(componentContext, config)
        )

        is Config.LoanPendingApprovals -> RootComponent.Child.LoanPendingApprovalsChild(
            loansPendingApprovalComponent(componentContext)
        )

        is Config.SignApp -> RootComponent.Child.SignAppChild(
            signApplication(
                componentContext,
                config
            )
        )

        is Config.ApplyLongTermLoans -> RootComponent.Child.ApplyLongtermLoanChild(
            applyLongTermLoanComponent(componentContext)
        )

        is Config.LongTermLoanDetails -> RootComponent.Child.LongTermLoanDetailsChild(
            longTermLoanDetailsComponent(componentContext, config)
        )

        is Config.SelectLoanPurpose -> RootComponent.Child.SelectLoanPurposeChild(
            selectLoanPurposeComponent(componentContext, config)
        )

        is Config.AddGuarantors -> RootComponent.Child.AddGuarantorsChild(
            addGuarantorsComponent(
                componentContext,
                config
            )
        )

        is Config.GuarantorshipRequest -> RootComponent.Child.GuarantorshipRequestChild(
            guarantorshipRequestComponent(componentContext)
        )

        is Config.FavouriteGuarantors -> RootComponent.Child.FavouriteGuarantorsChild(
            favouriteGuarantorsComponent(componentContext)
        )

        is Config.WitnessRequests -> RootComponent.Child.WitnessRequestChild(
            witnessRequestComponent(componentContext)
        )

        is Config.AddWitness -> RootComponent.Child.AddWitnessChild(
            addWitnessComponent(componentContext, config)
        )

        is Config.LongTermLoanConfirmation -> RootComponent.Child.LongTermLoanConfirmationChild(
            longTermLoanConfirmationComponent(componentContext, config)
        )

        is Config.LongTermLoanSigningStatus -> RootComponent.Child.LongTermLoanSigningStatusChild(
            longTermLoanSigningStatusComponent(componentContext, config)
        )

        is Config.SignDocument -> RootComponent.Child.SignGuarantorDocumentChild(
            signGuarantorDocumentComponent(
                componentContext, config
            )
        )

        is Config.SignWitnessDocument -> RootComponent.Child.SignWitnessDocumentChild(
            signWitnessDocumentComponent(
                componentContext, config
            )
        )

        is Config.SignLoanForm -> RootComponent.Child.SignLoanFormChild(
            signLoanForm(
                componentContext, config
            )
        )

        is Config.ReplaceGuarantor -> RootComponent.Child.ReplaceGuarantorChild(
            replaceGuarantorComponent(componentContext, config)
        )

    }

    private fun loansPendingApprovalComponent(componentContext: ComponentContext): LoanPendingApprovalsComponent =
        DefaultLoanPendingApprovalsComponent(componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onBack = {
                navigation.pop()
            })

    private fun tenantComponent(
        componentContext: ComponentContext, config: Config.Tenant
    ): TenantComponent = DefaultTenantComponent(
        componentContext = componentContext,
        storeFactory = storeFactory,
        mainContext = prestaDispatchers.main,
        onBoardingContext = config.onBoardingContext,
        onSubmit = { onBoardingContext, tenantId ->
            navigation.push(
                Config.OnBoarding(
                    onBoardingContext = onBoardingContext, tenantId = tenantId
                )
            )
        },

        )

    private fun splashComponent(componentContext: ComponentContext): SplashComponent =
        DefaultSplashComponent(componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onSignUp = {
                navigation.push(Config.Welcome(context = OnBoardingContext.REGISTRATION))
            },
            onSignIn = {
                //Todo----Configuration Not Unique
                println(":::::::::::::::::;kjkjkjkjkjkjkjkjkjkjkjkj:::::::::")
                navigation.bringToFront(Config.Welcome(context = OnBoardingContext.LOGIN))
//                navigation.push(Config.Welcome(context = OnBoardingContext.LOGIN))
            },
            navigateToAuth = { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext, pinStatus ->
                navigation.replaceAll(
                    Config.Auth(
                        memberRefId = memberRefId,
                        phoneNumber = phoneNumber,
                        isTermsAccepted = isTermsAccepted,
                        isActive = isActive,
                        onBoardingContext = onBoardingContext,
                        pinStatus = pinStatus
                    )
                )
            })

    private fun welcomeComponent(
        componentContext: ComponentContext, config: Config.Welcome
    ): WelcomeComponent = DefaultWelcomeComponent(
        componentContext = componentContext,
        onBoardingContext = config.context,
        onGetStartedSelected = {

            if (OrganisationModel.organisation.sandbox) {
                println(":::::::::::::::::;uuuuuuuuuuuuuuuuuuuuuuuuuu:::::::::")
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

    private fun onBoardingComponent(
        componentContext: ComponentContext, config: Config.OnBoarding
    ): OnBoardingComponent = DefaultOnboardingComponent(
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

    private fun registerComponent(
        componentContext: ComponentContext, config: Config.Register
    ): RegistrationComponent = DefaultRegistrationComponent(componentContext = componentContext,
        storeFactory = storeFactory,
        phoneNumber = config.phoneNumber,
        isTermsAccepted = config.isTermsAccepted,
        isActive = config.isActive,
        pinStatus = config.pinStatus,
        onBoardingContext = config.onBoardingContext,
        onRegistered = { memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext, pinStatus ->
            navigation.push(
                Config.Auth(
                    memberRefId = memberRefId,
                    phoneNumber = phoneNumber,
                    isTermsAccepted = isTermsAccepted,
                    isActive = isActive,
                    onBoardingContext = onBoardingContext,
                    pinStatus = pinStatus
                )
            )
        })

    private fun authComponent(
        componentContext: ComponentContext, config: Config.Auth
    ): AuthComponent = DefaultAuthComponent(componentContext = componentContext,
        storeFactory = storeFactory,
        mainContext = prestaDispatchers.main,
        phoneNumber = config.phoneNumber,
        isTermsAccepted = config.isTermsAccepted,
        isActive = config.isActive,
        pinStatus = config.pinStatus,
        onBoardingContext = config.onBoardingContext,
        onLogin = {
            navigation.replaceAll(Config.RootBottom(false))
        })

    val scope = coroutineScope(prestaDispatchers.main + SupervisorJob())

    private fun rootBottomComponent(
        componentContext: ComponentContext, config: Config.RootBottom
    ): RootBottomComponent = DefaultRootBottomComponent(componentContext = componentContext,
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
            navigation.bringToFront(
                Config.ProcessingTransaction(
                    amount = amount,
                    correlationId = correlationId,
                    mode = PaymentTypes.MEMBERSHIPFEES
                )
            )
        },
        processTransaction = { correlationId, amount, mode ->
            navigation.bringToFront(Config.ProcessingTransaction(correlationId, amount, mode))
        },
        processLoanState = {
            scope.launch {
                if (it !== null) {
                    navigation.bringToFront(
                        Config.ProcessingLoanLoanDisbursement(
                            it.correlationId, it.amount, it.fees
                        )
                    )
                }
            }
        },
        backTopProfile = config.backTopProfile,
        gotoSignApp = {
            navigation.bringToFront(Config.SignApp(loanRefId = { "" }))
        })

    private fun allTransactionHistory(componentContext: ComponentContext): TransactionHistoryComponent =
        DefaultTransactionHistoryComponent(componentContext = componentContext,
            mainContext = prestaDispatchers.main,
            storeFactory = storeFactory,
            onPop = {
                navigation.pop()
            })

    private fun payLoanComponent(componentContext: ComponentContext): PayLoanComponent =
        DefaultPayLoanComponent(storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            componentContext = componentContext,
            onPayClicked = { amount, correlationId ->
                navigation.push(
                    Config.ProcessingTransaction(
                        amount = amount.toDouble(),
                        correlationId = correlationId,
                        mode = PaymentTypes.LOAN
                    )
                )
            },
            onPop = {
                navigation.pop()
            })

    private fun payLoanPromptComponent(
        componentContext: ComponentContext, config: Config.PayLoanPrompt
    ): PayLoanPromptComponent = DefaultPayLoanPromptComponent(componentContext = componentContext,
        storeFactory = storeFactory,
        mainContext = prestaDispatchers.main,
        amount = config.amount,
        correlationId = config.correlationId,
        navigateToCompleteFailure = { paymentStatus ->
            if (paymentStatus == PaymentStatuses.COMPLETED) {
                println("navigate to success")
            }
        })

    private fun payRegistrationFeeComponent(
        componentContext: ComponentContext, config: Config.PayRegistrationFee
    ): PayRegistrationFeeComponent =
        DefaultPayRegistrationFeeComponent(componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            amount = config.amount,
            correlationId = config.correlationId,
            navigateToCompleteFailure = { paymentStatus ->
                if (paymentStatus == PaymentStatuses.COMPLETED) {
                    println("navigate to success")
                }
            })

    private fun processingTransactionComponent(
        componentContext: ComponentContext, config: Config.ProcessingTransaction
    ): ProcessingTransactionComponent =
        DefaultProcessingTransactionComponent(componentContext = componentContext,
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
            })


    private fun processingLoanLoanDisbursementComponent(
        componentContext: ComponentContext, config: Config.ProcessingLoanLoanDisbursement
    ): ProcessLoanDisbursementComponent =
        DefaultProcessLoanDisbursementComponent(storeFactory = storeFactory,
            componentContext = componentContext,
            requestId = config.correlationId,
            amount = config.amount,
            mainContext = prestaDispatchers.main,
            fees = config.fees,
            navigateToCompleteFailure = {
                navigation.replaceAll(Config.RootBottom(true))
            })

    private fun signApplication(
        componentContext: ComponentContext,
        config: Config.SignApp
    ): RootBottomSignComponent =
        DefaultRootBottomSignComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            gotoApplyAllLoans = {
                //navigate to all  loans outside  bottom scope
                navigation.bringToFront(Config.ApplyLongTermLoans)
            },
            gotoGuarantorshipRequests = {
                navigation.push(Config.GuarantorshipRequest)
            },
            gotoFavouriteGuarantors = {
                navigation.push(Config.FavouriteGuarantors)

            },
            gotoWitnessRequests = {
                navigation.push(Config.WitnessRequests)
            },
            goBackToLMs = {
                //navigate to  Lms
                navigation.bringToFront(Config.RootBottom(backTopProfile = false))
            },
            gotoReplaceGuarantor = { loanRequestRefId, guarantorRefId, guarantorFirstName, guarantorLastName ->
                //navigate  to replace  guarantor
                navigation.push(
                    Config.ReplaceGuarantor(
                        loanRequestRefId,
                        guarantorRefId,
                        guarantorFirstName,
                        guarantorLastName
                    )
                )

            },
            //Todo--- handle the sign loan form  state--------
            gotoSignLoanForm = { loanNumber, amount, loanRequestRefId, memberRefId ->
                navigation.push(
                    Config.SignLoanForm(
                        loanNumber = loanNumber,
                        amount = amount,
                        loanRequestRefId = loanRequestRefId,
                        memberRefId = memberRefId
                    )
                )
            },
            loanRefId = config.loanRefId
        )

    private fun applyLongTermLoanComponent(componentContext: ComponentContext): ApplyLongTermLoanComponent =
        DefaultApplyLongtermLoanComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = { loanRefid ->
                navigation.push(Config.LongTermLoanDetails(loanRefId = loanRefid))

            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onResolveLoanClicked = { loanRequestRefId ->
                //Todo navigate to bottom sign  and launch requests  if refid is not null
                navigation.bringToFront(Config.SignApp(loanRefId = {
                    loanRequestRefId
                }))

            }
        )

    private fun longTermLoanDetailsComponent(
        componentContext: ComponentContext,
        config: Config.LongTermLoanDetails
    ): LongTermLoanDetailsComponent =
        DefaultLongTermLoanDetailsComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()
            },
            onConfirmClicked = { loanRefid, loanType, desireAmount, loanPeriod, requiredGuarantors ->
                //navigate  to select Loan Details
                navigation.push(
                    Config.SelectLoanPurpose(
                        loanRefId = loanRefid,
                        loanType = loanType,
                        desiredAmount = desireAmount,
                        loanPeriod = loanPeriod,
                        requiredGuarantors = requiredGuarantors
                    )
                )
            },
            loanRefId = config.loanRefId,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
        )

    private fun selectLoanPurposeComponent(
        componentContext: ComponentContext,
        config: Config.SelectLoanPurpose
    ): SelectLoanPurposeComponent =
        DefaultSelectLoanPurposeComponent(
            componentContext = componentContext,
            onItemClicked = {
                //signHomeNavigation.pop()
                navigation.pop()
            },
            onContinueClicked = { loanRefId, loanType, desiredAmount, loanPeriod, requiredGuarantors, loanCategory, loanPurpose, loanPurposeCategory, loanPurposeCategoryCode ->
                navigation.push(
                    Config.AddGuarantors(
                        loanRefId = loanRefId,
                        loanType = loanType,
                        desiredAmount = desiredAmount,
                        loanPeriod = loanPeriod,
                        requiredGuarantors = requiredGuarantors,
                        loanCategory = loanCategory,
                        loanPurpose = loanPurpose,
                        loanPurposeCategory = loanPurposeCategory,
                        loanPurposeCategoryCode = loanPurposeCategoryCode
                    )
                )
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            loanRefId = config.loanRefId,
            loanType = config.loanType,
            desiredAmount = config.desiredAmount,
            loanPeriod = config.loanPeriod,
            requiredGuarantors = config.requiredGuarantors
        )

    private fun addGuarantorsComponent(
        componentContext: ComponentContext,
        config: Config.AddGuarantors
    ): AddGuarantorsComponent =
        DefaultAddGuarantorsComponent(
            componentContext = componentContext,
            onItemClicked = {
                //signHomeNavigation.pop()
                navigation.pop()
            },
            onContinueClicked = { loanRefId,
                                  loanType,
                                  desiredAmount,
                                  loanPeriod,
                                  requiredGuarantors,
                                  loanCategory,
                                  loanPurpose,
                                  loanPurposeCategory,
                                  businessType,
                                  businessLocation,
                                  kraPin,
                                  employer,
                                  employmentNumber,
                                  grossSalary,
                                  netSalary,
                                  memberRefId,
                                  guarantorList,
                                  loanPurposeCategoryCode,
                                  witnessRefid ->
                //Navigate to confirm
                navigation.push(
                    Config.LongTermLoanConfirmation(
                        loanRefId = loanRefId,
                        loanType = loanType,
                        desiredAmount = desiredAmount,
                        loanPeriod = loanPeriod,
                        requiredGuarantors = requiredGuarantors,
                        loanCategory = loanCategory,
                        loanPurpose = loanPurpose,
                        loanPurposeCategory = loanPurposeCategory,
                        businessType = businessType,
                        businessLocation = businessLocation,
                        kraPin = kraPin,
                        employer = employer,
                        employmentNumber = employmentNumber,
                        grossSalary = grossSalary,
                        netSalary = netSalary,
                        memberRefId = memberRefId,
                        guarantorList = guarantorList,
                        loanPurposeCategoryCode = loanPurposeCategoryCode,
                        witnessRefId = witnessRefid,
                        witnessName = ""
                    )
                )
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            loanRefId = config.loanRefId,
            loanType = config.loanType,
            desiredAmount = config.desiredAmount,
            loanPeriod = config.loanPeriod,
            requiredGuarantors = config.requiredGuarantors,
            loanCategory = config.loanCategory,
            loanPurpose = config.loanPurpose,
            loanPurposeCategory = config.loanPurposeCategory,
            loanPurposeCategoryCode = config.loanPurposeCategoryCode,
            onNavigateToAddWitnessClicked = { loanRefId,
                                              loanType,
                                              desiredAmount,
                                              loanPeriod,
                                              requiredGuarantors,
                                              loanCategory,
                                              loanPurpose,
                                              loanPurposeCategory,
                                              businessType,
                                              businessLocation,
                                              kraPin,
                                              employer,
                                              employmentNumber,
                                              grossSalary,
                                              netSalary,
                                              memberRefId,
                                              guarantorList,
                                              loanPurposeCategoryCode,
                                              witnessRefid ->
                //navigate to add Witness
                navigation.push(
                    Config.AddWitness(
                        loanRefId = loanRefId,
                        loanType = loanType,
                        desiredAmount = desiredAmount,
                        loanPeriod = loanPeriod,
                        requiredGuarantors = requiredGuarantors,
                        loanCategory = loanCategory,
                        loanPurpose = loanPurpose,
                        loanPurposeCategory = loanPurposeCategory,
                        businessType = businessType,
                        businessLocation = businessLocation,
                        kraPin = kraPin,
                        employer = employer,
                        employmentNumber = employmentNumber,
                        grossSalary = grossSalary,
                        netSalary = netSalary,
                        memberRefId = memberRefId,
                        guarantorList = guarantorList,
                        loanPurposeCategoryCode = loanPurposeCategoryCode,
                        witnessRefId = witnessRefid
                    )
                )
            }
        )

    private fun guarantorshipRequestComponent(componentContext: ComponentContext): GuarantorshipRequestComponent =
        DefaultGuarantorshipRequestComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onAcceptClicked = { loanNumber, amount, loanRequestRefId, memberRefId, guarantorRefId ->
                loanRefid = loanRequestRefId
                passedLoanNumber = loanNumber
                passedAmount = amount.toString()
                passedGuarantorRefId = guarantorRefId
                passedMemberRefid = memberRefId
                //Navigate to sign
                navigation.push(
                    Config.SignDocument(
                        loanNumber = loanNumber,
                        amount = amount,
                        loanRequestRefId = loanRequestRefId,
                        memberRefId = memberRefId,
                        guarantorRefId = guarantorRefId
                    )
                )

            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
        )

    private fun favouriteGuarantorsComponent(componentContext: ComponentContext): FavouriteGuarantorsComponent =
        DefaultFavouriteGuarantorsComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = {
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main
        )

    private fun witnessRequestComponent(componentContext: ComponentContext): WitnessRequestComponent =
        DefaultWitnessRequestComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = {
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onAcceptClicked = { loanNumber, amount, loanRequestRefId, memberRefId, witnessRefId ->
                //Navigate to sign
                navigation.push(
                    Config.SignWitnessDocument(
                        loanNumber = loanNumber,
                        amount = amount,
                        loanRequestRefId = loanRequestRefId,
                        memberRefId = memberRefId,
                        guarantorRefId = witnessRefId
                    )
                )
            }
        )

    private fun addWitnessComponent(
        componentContext: ComponentContext,
        config: Config.AddWitness
    ): AddWitnessComponent =
        DefaultAddWitnessComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = {
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            loanRefId = config.loanRefId,
            loanType = config.loanType,
            desiredAmount = config.desiredAmount,
            loanPeriod = config.loanPeriod,
            requiredGuarantors = config.requiredGuarantors,
            loanCategory = config.loanCategory,
            loanPurpose = config.loanPurpose,
            loanPurposeCategory = config.loanPurposeCategory,
            businessType = config.businessType,
            businessLocation = config.businessLocation,
            kraPin = config.kraPin,
            employer = config.employer,
            employmentNumber = config.employmentNumber,
            grossSalary = config.grossSalary,
            netSalary = config.netSalary,
            memberRefId = config.memberRefId,
            guarantorList = config.guarantorList,
            loanPurposeCategoryCode = config.loanPurposeCategoryCode,
            witnessRefId = config.witnessRefId,
            onAddWitnessClicked = { loanRefId,
                                    loanType,
                                    desiredAmount,
                                    loanPeriod,
                                    requiredGuarantors,
                                    loanCategory,
                                    loanPurpose,
                                    loanPurposeCategory,
                                    businessType,
                                    businessLocation,
                                    kraPin,
                                    employer,
                                    employmentNumber,
                                    grossSalary,
                                    netSalary,
                                    memberRefId,
                                    guarantorList,
                                    loanPurposeCategoryCode,
                                    witnessRefid,
                                    witnessName ->
                navigation.push(
                    Config.LongTermLoanConfirmation(
                        loanRefId = loanRefId,
                        loanType = loanType,
                        desiredAmount = desiredAmount,
                        loanPeriod = loanPeriod,
                        requiredGuarantors = requiredGuarantors,
                        loanCategory = loanCategory,
                        loanPurpose = loanPurpose,
                        loanPurposeCategory = loanPurposeCategory,
                        businessType = businessType,
                        businessLocation = businessLocation,
                        kraPin = kraPin,
                        employer = employer,
                        employmentNumber = employmentNumber,
                        grossSalary = grossSalary,
                        netSalary = netSalary,
                        memberRefId = memberRefId,
                        guarantorList = guarantorList,
                        loanPurposeCategoryCode = loanPurposeCategoryCode,
                        witnessRefId = witnessRefid,
                        witnessName = witnessName
                    )
                )
            }
        )

    private fun longTermLoanConfirmationComponent(
        componentContext: ComponentContext,
        config: Config.LongTermLoanConfirmation
    ): LongTermLoanConfirmationComponent =
        DefaultLongTermLoanConfirmationComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()
            },
            onProductClicked = {
                //navigate to Application Status
                navigation.push(Config.LongTermLoanSigningStatus(loanNumber = "", amount = 0.0))
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            loanRefId = config.loanRefId,
            loanType = config.loanType,
            desiredAmount = config.desiredAmount,
            loanPeriod = config.loanPeriod,
            requiredGuarantors = config.requiredGuarantors,
            loanCategory = config.loanCategory,
            loanPurpose = config.loanPurpose,
            loanPurposeCategory = config.loanPurposeCategory,
            businessType = config.businessType,
            businessLocation = config.businessLocation,
            kraPin = config.kraPin,
            employer = config.employer,
            employmentNumber = config.employmentNumber,
            grossSalary = config.grossSalary,
            netSalary = config.netSalary,
            memberRefId = config.memberRefId,
            guarantorList = config.guarantorList,
            loanPurposeCategoryCode = config.loanPurposeCategoryCode,
            witnessRefId = config.witnessRefId,
            witnessName = config.witnessName,
            navigateToSignLoanFormCLicked = { loanNumber, amount, loanRequestRefId, memberRefId ->
                navigation.push(
                    Config.SignLoanForm(
                        loanNumber = loanNumber,
                        amount = amount,
                        loanRequestRefId = loanRequestRefId,
                        memberRefId = memberRefId
                    )
                )
            }
        )

    private fun longTermLoanSigningStatusComponent(
        componentContext: ComponentContext,
        config: Config.LongTermLoanSigningStatus
    ): LongtermLoanSigningStatusComponent =
        DefaultLongTermLoanSigningStatusComponent(componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            correlationId = "",
            amount = config.amount,
            loanNumber = config.loanNumber,
            navigateToProfileClicked = {
                navigation.replaceAll(Config.SignApp(loanRefId = { "" }))
            }
        )

    private fun signGuarantorDocumentComponent(
        componentContext: ComponentContext,
        config: Config.SignDocument
    ): SignGuarantorFormComponent =
        DefaultSignGuarantorFormComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = {
            },
            loanNumber = config.loanNumber,
            amount = config.amount,
            loanRequestRefId = config.loanRequestRefId,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onDocumentSignedClicked = { loanNumber, amount ->
                //when doc is signed navigate
                navigation.bringToFront(Config.LongTermLoanSigningStatus(loanNumber, amount))
            },
            sign = false,
            memberRefId = config.memberRefId,
            guarantorRefId = config.guarantorRefId,
            coroutinetineDispatcher = prestaDispatchers.main
        )

    private fun signWitnessDocumentComponent(
        componentContext: ComponentContext,
        config: Config.SignWitnessDocument
    ): SignWitnessFormComponent =
        DefaultSignWitnessFormComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = {
            },
            loanNumber = config.loanNumber,
            amount = config.amount,
            loanRequestRefId = config.loanRequestRefId,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onDocumentSignedClicked = { loanNumber, amount ->
                //when doc is signed navigate
                navigation.bringToFront(
                    Config.LongTermLoanSigningStatus(
                        loanNumber = loanNumber,
                        amount = amount
                    )
                )
            },
            sign = false,
            memberRefId = config.memberRefId,
            guarantorRefId = config.guarantorRefId,
            coroutinetineDispatcher = prestaDispatchers.main
        )

    private fun signLoanForm(
        componentContext: ComponentContext,
        config: Config.SignLoanForm
    ): SignLoanFormComponent =
        DefaultSignLoanFormComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()

            },
            onProductClicked = {
            },
            loanNumber = config.loanNumber,
            amount = config.amount,
            loanRequestRefId = config.loanRequestRefId,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onDocumentSignedClicked = { loanNumber, amount ->
                //when doc is signed navigate
                navigation.bringToFront(
                    Config.LongTermLoanSigningStatus(
                        loanNumber = loanNumber,
                        amount = amount
                    )
                )

            },
            sign = false,
            memberRefId = config.memberRefId,
            coroutinetineDispatcher = prestaDispatchers.main
        )

    private fun replaceGuarantorComponent(
        componentContext: ComponentContext,
        config: Config.ReplaceGuarantor
    ): ReplaceGuarantorComponent =
        DefaultReplaceGuarantorComponent(
            componentContext = componentContext,
            onItemClicked = {
                navigation.pop()
            },
            onProductClicked = {
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            loanRequestRefId = config.loanRequestRefId,
            guarantorRefId = config.guarantorRefId,
            guarantorFirstName = config.guarantorFirstname,
            guarantorLastName = config.guarantorLastName
        )

    enum class OnBoardingContext {
        LOGIN, REGISTRATION
    }

    private sealed class Config : Parcelable {
        @Parcelize
        data class Tenant(val onBoardingContext: OnBoardingContext) : Config()

        @Parcelize
        object Splash : Config()

        @Parcelize
        data class Welcome(val context: OnBoardingContext) : Config()

        @Parcelize
        data class OnBoarding(
            val onBoardingContext: OnBoardingContext, val tenantId: String? = null
        ) : Config()

        @Parcelize
        data class OTP(
            val memberRefId: String?,
            val onBoardingContext: OnBoardingContext,
            val phoneNumber: String,
            val isActive: Boolean,
            val isTermsAccepted: Boolean,
            val pinStatus: PinStatus?
        ) : Config()

        @Parcelize
        data class Auth(
            val memberRefId: String?,
            val phoneNumber: String,
            val isTermsAccepted: Boolean,
            val isActive: Boolean,
            val onBoardingContext: OnBoardingContext,
            val pinStatus: PinStatus?
        ) : Config()

        @Parcelize
        data class Register(
            val phoneNumber: String,
            val isActive: Boolean,
            val isTermsAccepted: Boolean,
            val onBoardingContext: OnBoardingContext,
            val pinStatus: PinStatus?
        ) : Config()

        @Parcelize
        object AllTransactions : Config()

        @Parcelize
        data class SignApp(
            val loanRefId: () -> String,
        ) : Config()

        @Parcelize
        object LoanPendingApprovals : Config()

        @Parcelize
        data class RootBottom(val backTopProfile: Boolean) : Config()

        @Parcelize
        object PayLoan : Config()

        @Parcelize
        data class PayLoanPrompt(val amount: String, val correlationId: String) : Config()

        @Parcelize
        data class PayRegistrationFee(val amount: Double, val correlationId: String) : Config()

        @Parcelize
        data class ProcessingTransaction(
            val correlationId: String, val amount: Double, val mode: PaymentTypes
        ) : Config()

        @Parcelize
        data class ProcessingLoanLoanDisbursement(
            val correlationId: String, val amount: Double, val fees: Double
        ) : Config()

        @Parcelize
        object ApplyLongTermLoans : Config()

        @Parcelize
        data class LongTermLoanDetails(
            val loanRefId: String,
        ) : Config()

        @Parcelize
        data class SelectLoanPurpose(
            val loanRefId: String,
            val loanType: String,
            val desiredAmount: Double,
            val loanPeriod: Int,
            val requiredGuarantors: Int,
        ) : Config()

        @Parcelize
        data class AddGuarantors(
            val loanRefId: String,
            val loanType: String,
            val desiredAmount: Double,
            val loanPeriod: Int,
            val requiredGuarantors: Int,
            val loanCategory: String,
            val loanPurpose: String,
            val loanPurposeCategory: String,
            val loanPurposeCategoryCode: String,
        ) : Config()

        @Parcelize
        object GuarantorshipRequest : Config()

        @Parcelize
        object FavouriteGuarantors : Config()

        @Parcelize
        object WitnessRequests : Config()

        @Parcelize
        data class LongTermLoanConfirmation(
            val loanRefId: String,
            val loanType: String,
            val desiredAmount: Double,
            val loanPeriod: Int,
            val requiredGuarantors: Int,
            val loanCategory: String,
            val loanPurpose: String,
            val loanPurposeCategory: String,
            val businessType: String,
            val businessLocation: String,
            val kraPin: String,
            val employer: String,
            val employmentNumber: String,
            val grossSalary: Double,
            val netSalary: Double,
            val memberRefId: String,
            val guarantorList: Set<GuarantorDataListing>,
            val loanPurposeCategoryCode: String,
            val witnessRefId: String,
            val witnessName: String,
        ) : Config()

        @Parcelize
        data class AddWitness(
            val loanRefId: String,
            val loanType: String,
            val desiredAmount: Double,
            val loanPeriod: Int,
            val requiredGuarantors: Int,
            val loanCategory: String,
            val loanPurpose: String,
            val loanPurposeCategory: String,
            val businessType: String,
            val businessLocation: String,
            val kraPin: String,
            val employer: String,
            val employmentNumber: String,
            val grossSalary: Double,
            val netSalary: Double,
            val memberRefId: String,
            val guarantorList: Set<GuarantorDataListing>,
            val loanPurposeCategoryCode: String,
            val witnessRefId: String,
        ) : Config()

        @Parcelize
        data class LongTermLoanSigningStatus(
            val loanNumber: String,
            val amount: Double,
        ) : Config()

        @Parcelize
        data class SignDocument(
            val loanNumber: String,
            val amount: Double,
            val loanRequestRefId: String,
            val memberRefId: String,
            val guarantorRefId: String
        ) : Config()

        @Parcelize
        data class SignWitnessDocument(
            val loanNumber: String,
            val amount: Double,
            val loanRequestRefId: String,
            val memberRefId: String,
            val guarantorRefId: String
        ) : Config()

        @Parcelize
        data class SignLoanForm(
            val loanNumber: String,
            val amount: Double,
            val loanRequestRefId: String,
            val memberRefId: String,
        ) : Config()

        @Parcelize
        data class ReplaceGuarantor(
            val loanRequestRefId: String,
            val guarantorRefId: String,
            val guarantorFirstname: String,
            val guarantorLastName: String
        ) : Config()
    }

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            val nav = true
            override fun onResume() {
                when (childStack.active.configuration) {
                    is Config.AddGuarantors -> {
                        super.onResume()
                    }

                    is Config.SignDocument -> {
                        super.onResume()
                        //if signed navigate
                        navigation.bringToFront(
                            Config.SignDocument(
                                loanNumber = passedLoanNumber,
                                amount = passedAmount.toDouble(),
                                loanRequestRefId = loanRefid,
                                memberRefId = passedMemberRefid,
                                guarantorRefId = passedGuarantorRefId
                            )
                        )
                    }

                    is Config.SignLoanForm -> {
                        super.onResume()

                    }

                    is Config.SignWitnessDocument -> {
                        super.onResume()

                    }

                    is Config.LongTermLoanSigningStatus -> {
                        super.onResume()
                    }

                    else -> {
                        super.onResume()
                        navigation.replaceAll(Config.Splash)
                    }
                }
            }
        })
    }
}