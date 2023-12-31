package com.presta.customer.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimator
import com.moriatsushi.insetsx.SystemBarsBehavior
import com.moriatsushi.insetsx.rememberWindowInsetsController
import com.presta.customer.SharedStatus
import com.presta.customer.ui.components.addGuarantors.ui.AddGuarantorsScreen
import com.presta.customer.ui.components.addWitness.AddwitnessScreen
import com.presta.customer.ui.components.applyLongTermLoan.ui.ApplyLongTermLoanScreen
import com.presta.customer.ui.components.auth.ui.AuthScreen
import com.presta.customer.ui.components.favouriteGuarantors.ui.FavouriteGaurantorsScreen
import com.presta.customer.ui.components.guarantorshipRequests.ui.GuarantorshipRequestScreen
import com.presta.customer.ui.components.longTermLoanConfirmation.ui.LongTermLoanConfirmationScreen
import com.presta.customer.ui.components.longTermLoanDetails.ui.LongTermLoanDetailsScreen
import com.presta.customer.ui.components.longTermLoanSignStatus.LongTermLoanSigningStatusScreen
import com.presta.customer.ui.components.onBoarding.ui.OnBoardingScreen
import com.presta.customer.ui.components.otp.ui.OtpScreen
import com.presta.customer.ui.components.payLoan.ui.PayLoanScreen
import com.presta.customer.ui.components.payLoanPropmpt.ui.PayLoanPromptScreen
import com.presta.customer.ui.components.payRegistrationFeePrompt.ui.PayRegistrationFeeScreen
import com.presta.customer.ui.components.pendingApprovals.ui.PendingApprovalsScreen
import com.presta.customer.ui.components.processLoanDisbursement.ui.ProcessLoanDisbursementScreen
import com.presta.customer.ui.components.processingTransaction.ui.ProcessingTransactionScreen
import com.presta.customer.ui.components.registration.ui.RegistrationScreen
import com.presta.customer.ui.components.replaceGuarantor.ui.ReplaceGuarantorScreen
import com.presta.customer.ui.components.root.RootComponent
import com.presta.customer.ui.components.rootBottomSign.RootBottomSignScreen
import com.presta.customer.ui.components.rootBottomStack.RootBottomScreen
import com.presta.customer.ui.components.selectLoanPurpose.ui.SelectLoanPurposeScreen
import com.presta.customer.ui.components.signGuarantorForm.ui.SignGuarantorFormScreen
import com.presta.customer.ui.components.signLoanForm.ui.SignLoanFormScreen
import com.presta.customer.ui.components.signWitnessForm.ui.SignWitnessFormScreen
import com.presta.customer.ui.components.splash.SplashScreen
import com.presta.customer.ui.components.tenant.ui.TenantScreen
import com.presta.customer.ui.components.transactionHistory.ui.TransactionHistoryScreen
import com.presta.customer.ui.components.welcome.WelcomeScreen
import com.presta.customer.ui.components.witnessRequests.ui.WitnessRequestScreen
import com.presta.customer.ui.composables.SplashLMS
import com.presta.customer.ui.composables.SplashSign

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

    //Todo---- skip to Sign depending on the organization  requirements

    var rotated by remember { mutableStateOf(false) }
    var rotated2 by remember { mutableStateOf(false) }
    Children(
        stack = component.childStack,
        animation = stackAnimation(true) { child ->
            when (child.instance) {
                is RootComponent.Child.SignAppChild -> stackAnimator { _, direction, content ->
                    if (rotated || direction !== Direction.ENTER_FRONT) {
                        content(
                            Modifier
                        )
                    } else if (direction == Direction.ENTER_FRONT) {
                        SplashSign (
                            callback = {
                                rotated = child.instance is RootComponent.Child.SignAppChild
                            }
                        )
                    }
                }
                is RootComponent.Child.RootBottomChild -> stackAnimator { _, _, content ->
                    if (rotated2) {
                        content(
                            Modifier
                        )
                    } else {
                        SplashLMS (
                            callback = {
                                rotated2 = child.instance is RootComponent.Child.RootBottomChild
                            }
                        )
                    }
                }
                else -> fade() + scale()
            }
        },// tabAnimation()
    ) {
        if (it.instance !is RootComponent.Child.SignAppChild) {
            rotated = false
        }
        if (it.instance !is RootComponent.Child.RootBottomChild) {
            rotated2 = false
        }
        when (val child = it.instance) {
            is RootComponent.Child.TenantChild -> TenantScreen(child.component)
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
            is RootComponent.Child.LongTermLoanDetailsChild -> LongTermLoanDetailsScreen(child.component)
            is RootComponent.Child.SelectLoanPurposeChild-> SelectLoanPurposeScreen(child.component)
            is RootComponent.Child.AddGuarantorsChild-> AddGuarantorsScreen(child.component)
            is RootComponent.Child.GuarantorshipRequestChild -> GuarantorshipRequestScreen(child.component)
            is RootComponent.Child.FavouriteGuarantorsChild-> FavouriteGaurantorsScreen(child.component)
            is RootComponent.Child.AddWitnessChild -> AddwitnessScreen(child.component)
            is RootComponent.Child.WitnessRequestChild-> WitnessRequestScreen(child.component)
            is RootComponent.Child.LongTermLoanConfirmationChild-> LongTermLoanConfirmationScreen(child.component)
            is RootComponent.Child.LongTermLoanSigningStatusChild-> LongTermLoanSigningStatusScreen(child.component)
            is RootComponent.Child.SignGuarantorDocumentChild-> SignGuarantorFormScreen(child.component)
            is RootComponent.Child.SignWitnessDocumentChild-> SignWitnessFormScreen(child.component)
            is RootComponent.Child.SignLoanFormChild-> SignLoanFormScreen(child.component)
            is RootComponent.Child.ReplaceGuarantorChild-> ReplaceGuarantorScreen(child.component)
        }
    }
}