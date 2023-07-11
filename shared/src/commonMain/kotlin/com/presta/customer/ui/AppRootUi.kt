package com.presta.customer.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.moriatsushi.insetsx.SystemBarsBehavior
import com.moriatsushi.insetsx.rememberWindowInsetsController
import com.presta.customer.SharedStatus
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanScreen
import com.presta.customer.ui.components.auth.ui.AuthScreen
import com.presta.customer.ui.components.onBoarding.ui.OnBoardingScreen
import com.presta.customer.ui.components.otp.ui.OtpScreen
import com.presta.customer.ui.components.payLoan.ui.PayLoanScreen
import com.presta.customer.ui.components.payLoanPropmpt.ui.PayLoanPromptScreen
import com.presta.customer.ui.components.payRegistrationFeePrompt.ui.PayRegistrationFeeScreen
import com.presta.customer.ui.components.pendingApprovals.ui.PendingApprovalsScreen
import com.presta.customer.ui.components.processLoanDisbursement.ui.ProcessLoanDisbursementScreen
import com.presta.customer.ui.components.processingTransaction.ui.ProcessingTransactionScreen
import com.presta.customer.ui.components.registration.ui.RegistrationScreen
import com.presta.customer.ui.components.root.RootComponent
import com.presta.customer.ui.components.rootBottomSign.RootBottomSignScreen
import com.presta.customer.ui.components.rootBottomStack.RootBottomScreen
import com.presta.customer.ui.components.splash.SplashScreen
import com.presta.customer.ui.components.tenant.ui.TenantScreen
import com.presta.customer.ui.components.transactionHistory.ui.TransactionHistoryScreen
import com.presta.customer.ui.components.welcome.WelcomeScreen

@Composable
fun AppRootUi(component: RootComponent, connectivityStatus: SharedStatus?) {

    val windowInsetsController = rememberWindowInsetsController()
    val isDark = isSystemInDarkTheme()
    LaunchedEffect(Unit) {
        windowInsetsController?.apply {
            // Hide the status bars
            setIsStatusBarsVisible(true)
            // Hide the navigation bars
            setIsNavigationBarsVisible(false)
            // Change an options for behavior when system bars are hidden
            setSystemBarsBehavior(SystemBarsBehavior.Immersive)
            // The status bars icon + content will change to a light color
            setStatusBarContentColor(dark = isDark)
            // The navigation bars icons will change to a light color (android only)
            setNavigationBarsContentColor(dark = isDark)
        }
    }
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),// tabAnimation()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.TenantChild -> TenantScreen(child.component, connectivityStatus)
            is RootComponent.Child.SplashChild -> SplashScreen(child.component, connectivityStatus)
            is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
            is RootComponent.Child.OnboardingChild -> OnBoardingScreen(child.component)
            is RootComponent.Child.OTPChild -> OtpScreen(child.component)
            is RootComponent.Child.RegisterChild -> RegistrationScreen(child.component)
            is RootComponent.Child.AuthChild -> AuthScreen(child.component)
            is RootComponent.Child.AllTransactionsChild -> TransactionHistoryScreen(child.component)
            is RootComponent.Child.RootBottomChild -> RootBottomScreen(child.component)
            is RootComponent.Child.RootBottomSignChild -> RootBottomSignScreen(child.component)
            is RootComponent.Child.PayLoanChild-> PayLoanScreen(child.component)
            is RootComponent.Child.PayLoanPromptChild-> PayLoanPromptScreen(child.component)
            is RootComponent.Child.PayRegistrationFeeChild-> PayRegistrationFeeScreen(child.component)
            is RootComponent.Child.ProcessingTransactionChild-> ProcessingTransactionScreen(child.component)
            is RootComponent.Child.ProcessingLoanDisbursementChild-> ProcessLoanDisbursementScreen(child.component)
            is RootComponent.Child.LoanPendingApprovalsChild-> PendingApprovalsScreen(child.component)
            is RootComponent.Child.SignAppChild-> RootBottomSignScreen(child.component)
            is RootComponent.Child.ApplyLongtermLoanChild -> ApplyLongTermLoanScreen(child.component)
        }
    }
}