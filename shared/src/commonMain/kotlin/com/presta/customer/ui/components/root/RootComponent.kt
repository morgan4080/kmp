package com.presta.customer.ui.components.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent
import com.presta.customer.ui.components.addWitness.AddWitnessComponent
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.auth.AuthComponent
import com.presta.customer.ui.components.favouriteGuarantors.FavouriteGuarantorsComponent
import com.presta.customer.ui.components.guarantorshipRequests.GuarantorshipRequestComponent
import com.presta.customer.ui.components.longTermLoanSignStatus.LongtermLoanSigningStatusComponent
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent
import com.presta.customer.ui.components.longTermLoanDetails.LongTermLoanDetailsComponent
import com.presta.customer.ui.components.onBoarding.OnBoardingComponent
import com.presta.customer.ui.components.otp.OtpComponent
import com.presta.customer.ui.components.payLoan.PayLoanComponent
import com.presta.customer.ui.components.payLoanPropmpt.PayLoanPromptComponent
import com.presta.customer.ui.components.payRegistrationFeePrompt.PayRegistrationFeeComponent
import com.presta.customer.ui.components.pendingApprovals.LoanPendingApprovalsComponent
import com.presta.customer.ui.components.processLoanDisbursement.ProcessLoanDisbursementComponent
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionComponent
import com.presta.customer.ui.components.registration.RegistrationComponent
import com.presta.customer.ui.components.replaceGuarantor.ReplaceGuarantorComponent
import com.presta.customer.ui.components.rootBottomSign.RootBottomSignComponent
import com.presta.customer.ui.components.rootBottomStack.RootBottomComponent
import com.presta.customer.ui.components.selectLoanPurpose.SelectLoanPurposeComponent
import com.presta.customer.ui.components.signGuarantorForm.SignGuarantorFormComponent
import com.presta.customer.ui.components.signLoanForm.SignLoanFormComponent
import com.presta.customer.ui.components.splash.SplashComponent
import com.presta.customer.ui.components.tenant.TenantComponent
import com.presta.customer.ui.components.transactionHistory.TransactionHistoryComponent
import com.presta.customer.ui.components.welcome.WelcomeComponent
import com.presta.customer.ui.components.witnessRequests.WitnessRequestComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>
    //get  the guarantor sign status to navigate user to appropriate screen

    sealed class Child {
        class TenantChild(val component: TenantComponent) : Child()
        class SplashChild(val component: SplashComponent) : Child()
        class WelcomeChild(val component: WelcomeComponent) : Child()
        class OnboardingChild(val component: OnBoardingComponent) : Child()
        class OTPChild(val component: OtpComponent) : Child()
        class RegisterChild(val component: RegistrationComponent) : Child()
        class AuthChild(val component: AuthComponent) : Child()
        class AllTransactionsChild(val component: TransactionHistoryComponent) : Child()
        class RootBottomChild(val component: RootBottomComponent) : Child()
        class RootBottomSignChild(val component: RootBottomSignComponent) : Child()
        class PayLoanChild(val component: PayLoanComponent) :Child()
        class PayLoanPromptChild(val component: PayLoanPromptComponent) : Child()
        class PayRegistrationFeeChild(val component: PayRegistrationFeeComponent) : Child()
        class ProcessingTransactionChild(val component: ProcessingTransactionComponent) : Child()
        class ProcessingLoanDisbursementChild(val component: ProcessLoanDisbursementComponent) : Child()
        class LoanPendingApprovalsChild(val component: LoanPendingApprovalsComponent) : Child()
        class SignAppChild(val component: RootBottomSignComponent) : Child()
        class ApplyLongtermLoanChild(val component: ApplyLongTermLoanComponent) : Child()
        class LongTermLoanDetailsChild(val component: LongTermLoanDetailsComponent): Child()
        class SelectLoanPurposeChild(val component: SelectLoanPurposeComponent): Child()
        class AddGuarantorsChild(val component: AddGuarantorsComponent): Child()
        class GuarantorshipRequestChild(val component: GuarantorshipRequestComponent): Child()
        class FavouriteGuarantorsChild(val component: FavouriteGuarantorsComponent): Child()
        class AddWitnessChild(val component: AddWitnessComponent): Child()
        class WitnessRequestChild(val component: WitnessRequestComponent): Child()
        class LongTermLoanConfirmationChild(val component: LongTermLoanConfirmationComponent): Child()
        class LongTermLoanSigningStatusChild(val component: LongtermLoanSigningStatusComponent): Child()
        class SignDocumentChild(val component: SignGuarantorFormComponent): Child()
        class SignLoanFormChild(val component: SignLoanFormComponent): Child()
        class ReplaceGuarantorChild(val component: ReplaceGuarantorComponent): Child()

    }

}