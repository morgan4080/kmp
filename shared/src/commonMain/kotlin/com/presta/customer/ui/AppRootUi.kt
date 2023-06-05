package com.presta.customer.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.presta.customer.ui.components.auth.ui.AuthScreen
import com.presta.customer.ui.components.onBoarding.ui.OnBoardingScreen
import com.presta.customer.ui.components.otp.ui.OtpScreen
import com.presta.customer.ui.components.payLoan.PayLoanScreen
import com.presta.customer.ui.components.payLoanPropmpt.PayLoanPromptScreen
import com.presta.customer.ui.components.registration.ui.RegistrationScreen
import com.presta.customer.ui.components.root.RootComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomScreen
import com.presta.customer.ui.components.splash.SplashScreen
import com.presta.customer.ui.components.transactionHistory.ui.TransactionHistoryScreen
import com.presta.customer.ui.components.welcome.WelcomeScreen

@Composable
fun AppRootUi(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),// tabAnimation()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.SplashChild -> SplashScreen(child.component)
            is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
            is RootComponent.Child.OnboardingChild -> OnBoardingScreen(child.component)
            is RootComponent.Child.OTPChild -> OtpScreen(child.component)
            is RootComponent.Child.RegisterChild -> RegistrationScreen(child.component)
            is RootComponent.Child.AuthChild -> AuthScreen(child.component)
            is RootComponent.Child.AllTransactionsChild -> TransactionHistoryScreen(child.component)
            is RootComponent.Child.RootBottomChild -> RootBottomScreen(child.component)
            is RootComponent.Child.PayLoanChild-> PayLoanScreen(child.component)
            is RootComponent.Child.PayLoanPromptChild-> PayLoanPromptScreen(child.component)
        }
    }
}