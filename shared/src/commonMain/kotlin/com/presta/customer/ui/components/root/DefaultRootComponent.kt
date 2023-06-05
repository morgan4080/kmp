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
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.network.onBoarding.model.PinStatus
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
import com.presta.customer.ui.components.registration.DefaultRegistrationComponent
import com.presta.customer.ui.components.registration.RegistrationComponent
import com.presta.customer.ui.components.rootBottomStack.DefaultRootBottomComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomComponent
import com.presta.customer.ui.components.splash.DefaultSplashComponent
import com.presta.customer.ui.components.splash.SplashComponent
import com.presta.customer.ui.components.transactionHistory.DefaultTransactionHistoryComponent
import com.presta.customer.ui.components.transactionHistory.TransactionHistoryComponent
import com.presta.customer.ui.components.welcome.DefaultWelcomeComponent
import com.presta.customer.ui.components.welcome.WelcomeComponent
import prestaDispatchers

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
            is Config.Splash -> RootComponent.Child.SplashChild(splashComponent(componentContext))
            is Config.Welcome -> RootComponent.Child.WelcomeChild(welcomeComponent(componentContext, config))
            is Config.OnBoarding -> RootComponent.Child.OnboardingChild(onboardingComponent(componentContext, config))
            is Config.OTP -> RootComponent.Child.OTPChild(otpComponent(componentContext, config))
            is Config.Register -> RootComponent.Child.RegisterChild(registerComponent(componentContext, config))
            is Config.Auth -> RootComponent.Child.AuthChild(authComponent(componentContext, config))
            is Config.RootBottom -> RootComponent.Child.RootBottomChild(rootBottomComponent(componentContext))
            is Config.AllTransactions -> RootComponent.Child.AllTransactionsChild(allTransactionHistory(componentContext))
            is Config.PayLoanPrompt->RootComponent.Child.PayLoanPromptChild(payLoanPromptComponent(componentContext))
            is Config.PayLoan->RootComponent.Child.PayLoanChild(payLoanComponent(componentContext))
        }

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
            },
            navigateToProfile = {
                navigation.replaceAll(Config.RootBottom)
            }
        )

    private fun welcomeComponent(componentContext: ComponentContext, config: Config.Welcome): WelcomeComponent =
        DefaultWelcomeComponent(
            componentContext = componentContext,
            onBoardingContext = config.context,
            onGetStartedSelected = {
                navigation.push(
                    Config.OnBoarding(
                        onBoardingContext = it
                    )
                )
            },
        )

    private fun onboardingComponent(componentContext: ComponentContext, config: Config.OnBoarding): OnBoardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            onBoardingContext = config.onBoardingContext,
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
            phoneNumber = config.phoneNumber,
            isTermsAccepted = config.isTermsAccepted,
            isActive = config.isActive,
            pinStatus = config.pinStatus,
            onBoardingContext = config.onBoardingContext,
            onLogin = {
                navigation.replaceAll(Config.RootBottom)
            }
        )

    private fun rootBottomComponent(componentContext: ComponentContext): RootBottomComponent =
        DefaultRootBottomComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            gotoAllTransactions = {
                navigation.push(Config.AllTransactions)
            },
            logoutToSplash = {
                navigation.replaceAll(Config.Splash)
            },
            gotoPayLoans = {
                navigation.bringToFront(Config.PayLoan)
            }
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
            componentContext = componentContext,
            onPayClicked = {
                //push  to confirm Loan Details Screen
                //Navigate to pay Loan child  a child Of loans
                //Show  the Pay  Loan child first
                ///loansNavigation.push(DefaultRootLoansComponent.ConfigLoans.PayLoanPrompt)
            }
        )
    private fun payLoanPromptComponent(componentContext: ComponentContext): PayLoanPromptComponent =
        DefaultPayLoanPromptComponent(
            componentContext = componentContext,

            )



    enum class OnBoardingContext {
        LOGIN,
        REGISTRATION
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object Splash : Config()
        @Parcelize
        data class Welcome (val context: OnBoardingContext) : Config()
        @Parcelize
        data class OnBoarding (val onBoardingContext: OnBoardingContext) : Config()
        @Parcelize
        data class OTP (val memberRefId: String?, val onBoardingContext: OnBoardingContext, val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean, val pinStatus: PinStatus?) : Config()
        @Parcelize
        data class Auth(val memberRefId: String?, val phoneNumber: String, val isTermsAccepted: Boolean, val isActive: Boolean, val onBoardingContext: OnBoardingContext, val pinStatus: PinStatus?) : Config()
        @Parcelize
        data class Register(val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean, val onBoardingContext: OnBoardingContext, val pinStatus: PinStatus?) : Config()
        @Parcelize
        object AllTransactions : Config()
        @Parcelize
        object RootBottom : Config()
        @Parcelize
        object PayLoan :Config()
        @Parcelize
        object PayLoanPrompt :Config()

    }
}