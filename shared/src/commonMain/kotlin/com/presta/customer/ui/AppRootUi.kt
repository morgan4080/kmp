package com.presta.customer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.moriatsushi.insetsx.SystemBarsBehavior
import com.moriatsushi.insetsx.rememberWindowInsetsController
import com.presta.customer.ui.components.auth.ui.AuthScreen
import com.presta.customer.ui.components.onBoarding.ui.OnBoardingScreen
import com.presta.customer.ui.components.otp.ui.OtpScreen
import com.presta.customer.ui.components.payLoan.ui.PayLoanScreen
import com.presta.customer.ui.components.payLoanPropmpt.ui.PayLoanPromptScreen
import com.presta.customer.ui.components.payRegistrationFeePrompt.ui.PayRegistrationFeeScreen
import com.presta.customer.ui.components.registration.ui.RegistrationScreen
import com.presta.customer.ui.components.root.RootComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomScreen
import com.presta.customer.ui.components.splash.SplashScreen
import com.presta.customer.ui.components.transactionHistory.ui.TransactionHistoryScreen
import com.presta.customer.ui.components.welcome.WelcomeScreen

@Composable
fun AppRootUi(component: RootComponent) {

    val windowInsetsController = rememberWindowInsetsController()
    LaunchedEffect(Unit) {
        // Hide the status bars
        windowInsetsController?.setIsStatusBarsVisible(false)
        // Hide the navigation bars
        windowInsetsController?.setIsNavigationBarsVisible(false)
        // Change an options for behavior when system bars are hidden
        windowInsetsController?.setSystemBarsBehavior(SystemBarsBehavior.Immersive)
    }

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
            is RootComponent.Child.PayRegistrationFeeChild-> PayRegistrationFeeScreen(child.component)
        }
    }
}