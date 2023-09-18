package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.ui.components.addGuarantors.ui.SelectGuarantorsView
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

data class DisbursementModes(
    val name: String,
    val value: String,
    val selected: Boolean
)

data class RepaymentModes(
    val name: String,
    val value: String,
    val selected: Boolean
)

@Composable
fun UserInformation(
    component: LongTermLoanConfirmationComponent,
    authState: AuthStore.State,
    signProfileState: SignHomeStore.State,
    onLongTermLoanEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    state: ApplyLongTermLoansStore.State,
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var launchDisbursementModePopUp by remember { mutableStateOf(false) }
    var launchPaymentModePopUp by remember { mutableStateOf(false) }
    var sendLoanRequest by remember { mutableStateOf(false) }
    var launchHandleLoanRequestPopUp by remember { mutableStateOf(false) }
    var lastName by remember { mutableStateOf(TextFieldValue()) }
    var disbursementMode by remember { mutableStateOf("") }
    var repaymentMode by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d+\$") }
    val numberTextPattern = remember { Regex("^[\\p{L}\\d ]+$") }
    val guarantorList = arrayListOf<Guarantor>()
    for (item in component.guarantorList) {
        val refId = item.guarantorRefId
        val amount = item.amount
        val guarantorName = item.guarantorFirstName
        guarantorList.add(Guarantor(refId, amount, guarantorName))
    }
    LaunchedEffect(state.prestaLongTermLoanRequestData?.refId) {
        if (!state.prestaLongTermLoanRequestData?.refId.isNullOrEmpty()) {
            state.prestaLongTermLoanRequestData?.let {
                sendLoanRequest = false
                component.navigateToSignLoanForm(
                    loanNumber = it.loanRequestNumber,
                    amount = state.prestaLongTermLoanRequestData.loanAmount,
                    loanRequestRefId = state.prestaLongTermLoanRequestData.refId,
                    memberRefId = component.memberRefId
                )
            }

        } else {
            //launch pop up to show reason of loan failure
            //Todo--if loan pending reason is not null
            if (state.prestaLongTermLoanRequestData?.refId.isNullOrEmpty() && sendLoanRequest) {
                launchHandleLoanRequestPopUp = true
                sendLoanRequest = false
            }
        }
    }

    Column(modifier = Modifier.padding(top = 20.dp)) {
        //popup Disbursement mode
        //Aded the popUps on top of Parent Column to prevent them from freezing
        if (launchDisbursementModePopUp) {

            val disburementModeListing = listOf(
                DisbursementModes("Cheques", "Cheques", selected = true),
                DisbursementModes("My Account", "My Account", selected = true),
                DisbursementModes("EFT", "EFT", selected = true)
            )
            Popup {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color.Black.copy(alpha = 0.7f)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)
                            .padding(
                                start = 26.dp,
                                end = 26.dp,
                                top = 40.dp,
                                bottom = 90.dp
                            ),
                        colors = CardDefaults
                            .elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                        ) {

                            Text(
                                "SELECT DISBURSEMENT MODE",
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 17.dp),
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                fontSize = 14.sp,
                            )
                            Text(
                                "Select Options Below ",
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )
                            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .wrapContentHeight()
                                    ) {

                                        disburementModeListing.mapIndexed { indexed, disbursementModes ->
                                            item {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                                        .padding(
                                                            top = 10.dp,
                                                            start = 16.dp,
                                                            end = 16.dp
                                                        )
                                                ) {
                                                    SelectGuarantorsView(
                                                        idx = indexed,
                                                        selected = selectedIndex == indexed,
                                                        onClick = { index: Int ->
                                                            selectedIndex =
                                                                if (selectedIndex == index) -1 else index
                                                            if (selectedIndex > -1) {
                                                                disbursementMode =
                                                                    disburementModeListing[selectedIndex].name
                                                            }
                                                        },
                                                        label = disbursementModes.name
                                                    )
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp,
                                    bottom = 20.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            OutlinedButton(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    launchDisbursementModePopUp = false
                                },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(30.dp),
                            ) {

                                Text(
                                    text = "Dismiss",
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )

                            }
                            OutlinedButton(
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = actionButtonColor),
                                border = BorderStroke(
                                    width = 0.dp,
                                    color = actionButtonColor
                                ),
                                onClick = {
                                    launchDisbursementModePopUp = false
                                },
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .height(30.dp),
                            ) {

                                Text(
                                    text = "Proceed",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )
                            }
                        }
                    }
                }
            }
        }
        //popup Repayment mode
        if (launchPaymentModePopUp) {
            val repaymentmentModeListing = listOf(
                RepaymentModes("Check Off ", "Check Off", selected = true),
                RepaymentModes("Paybill", "Paybill", selected = true),
                RepaymentModes("Standing Order", "Standing Order", selected = true)
            )
            Popup {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color.Black.copy(alpha = 0.7f)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)
                            .padding(
                                start = 26.dp,
                                end = 26.dp,
                                top = 40.dp,
                                bottom = 90.dp
                            ),
                        colors = CardDefaults
                            .elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                        ) {

                            Text(
                                "SET PAYMENT MODE",
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 17.dp),
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                fontSize = 14.sp,
                            )
                            Text(
                                "Select Options Below",
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )
                            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .wrapContentHeight()
                                    ) {

                                        repaymentmentModeListing.mapIndexed { indexedPay, repamentModes ->
                                            item {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                                        .padding(
                                                            top = 10.dp,
                                                            start = 16.dp,
                                                            end = 16.dp
                                                        )
                                                ) {
                                                    SelectGuarantorsView(
                                                        idx= indexedPay,
                                                        selected = selectedIndex == indexedPay,
                                                        onClick = { index: Int ->
                                                            selectedIndex =
                                                                if (selectedIndex == index) -1 else index
                                                            if (selectedIndex > -1) {
                                                                repaymentMode =
                                                                    repaymentmentModeListing[selectedIndex].name
                                                            }
                                                        },
                                                        label = repamentModes.name
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp,
                                    bottom = 20.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            OutlinedButton(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    launchPaymentModePopUp = false
                                },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(30.dp),
                            ) {

                                Text(
                                    text = "Dismiss",
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )

                            }
                            OutlinedButton(
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = actionButtonColor),
                                border = BorderStroke(
                                    width = 0.dp,
                                    color = actionButtonColor
                                ),
                                onClick = {
                                    launchPaymentModePopUp = false
                                },
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .height(30.dp),
                            ) {

                                Text(
                                    text = "Proceed",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )
                            }
                        }
                    }
                }
            }
        }
        LazyColumn() {
            if (signProfileState.prestaTenantByPhoneNumber?.firstName == null) {
                items(6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
                            .background(color = MaterialTheme.colorScheme.background),
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(40.dp, 40.dp)
                                    .background(
                                        ShimmerBrush(
                                            targetValue = 1300f,
                                            showShimmer = true
                                        )
                                    )
                                    .fillMaxWidth()
                            ) {
                            }
                        }
                    }
                }
            } else {
                lastName = TextFieldValue(signProfileState.prestaTenantByPhoneNumber.firstName)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            LiveTextContainer(
                                userInput = lastName.text,
                                label = "first Name",
                                keyboardType = KeyboardType.Text,
                                pattern = numberTextPattern
                            ) {
                                val inputValue: String = TextFieldValue(it).text
                                lastName = TextFieldValue(it)
//                                if (inputValue != "") {
//                                    if (TextFieldValue(it).text !== "") {
//                                        lastName = TextFieldValue(it)
//
//                                    } else {
//
//                                    }
//                               }
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.lastName,
                                label = "last name",
                                keyboardType = KeyboardType.Text,
                                pattern = numberTextPattern
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.idNumber,
                                label = "ID number",
                                keyboardType = KeyboardType.Number,
                                pattern = pattern
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.phoneNumber,
                                label = "Phone Number",
                                keyboardType = KeyboardType.Number,
                                pattern = pattern
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.email,
                                label = "email",
                                keyboardType = KeyboardType.Text,
                                pattern = numberTextPattern
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            ModeSelectionCard(label = "Disbursement Mode",
                                description = if (disbursementMode != "") disbursementMode else "",
                                onClickContainer = {
                                    launchDisbursementModePopUp = true

                                })
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            ModeSelectionCard(label = "Repayment Mode",
                                description = if (repaymentMode != "") repaymentMode else "",
                                onClickContainer = {
                                    launchPaymentModePopUp = true
                                })
                        }
                        //Todo --- get the updated response in  real time
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp)
                        ) {
                            ActionButton(
                                label = "Submit  Loan Request", onClickContainer = {
                                    authState.cachedMemberData?.let {
                                        ApplyLongTermLoansStore.Intent.RequestLongTermLoan(
                                            token = it.accessToken,
                                            details = DetailsData(
                                                loan_purpose_1 = component.loanCategory,
                                                loan_purpose_2 = component.loanPurpose,
                                                loan_purpose_3 = component.loanPurposeCategory,
                                                loanPurposeCode = component.loanPurposeCategoryCode,
                                                loanPeriod = component.loanPeriod.toString(),
                                                repayment_period = component.loanPeriod.toString(),
                                                employer_name = component.employer,
                                                employment_type = "",
                                                employment_number = component.employmentNumber,
                                                business_location = component.businessLocation,
                                                business_type = component.businessType,
                                                net_salary = component.netSalary.toString(),
                                                gross_salary = component.grossSalary.toString(),
                                                disbursement_mode = disbursementMode,
                                                repayment_mode = repaymentMode,
                                                loan_type = component.loanType,
                                                kra_pin = component.kraPin
                                            ),
                                            loanProductName = component.loanType,
                                            loanProductRefId = component.loanRefId,
                                            selfCommitment = 0.0,
                                            loanAmount = component.desiredAmount,
                                            memberRefId = component.memberRefId,
                                            memberNumber = signProfileState.prestaTenantByPhoneNumber.memberNumber,
                                            witnessRefId = component.witnessRefId,
                                            guarantorList = guarantorList,
                                        )
                                    }?.let {
                                        onLongTermLoanEvent(
                                            it
                                        )
                                    }
                                    sendLoanRequest = true
                                },
                                enabled = repaymentMode != "" && disbursementMode != "",
                                loading = state.isLoading
                            )
                        }
                    }
                }
            }
            item {
                //lauch pop up to handle loan Request Errors
                if (launchHandleLoanRequestPopUp) {
                    Popup {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(color = Color.Black.copy(alpha = 0.7f)),
                            verticalArrangement = Arrangement.Center
                        ) {
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 26.dp,
                                        end = 26.dp,
                                        top = 40.dp,
                                        bottom = 90.dp
                                    ),
                                colors = CardDefaults
                                    .elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "PENDING REASON",
                                            modifier = Modifier.padding(top = 20.dp),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                        )
                                        Text(
                                            text = if (state.error != null || state.prestaLongTermLoanRequestData?.pendingReason != null) state.error.toString() + state.prestaLongTermLoanRequestData?.pendingReason else "",
                                            modifier = Modifier.padding(top = 10.dp),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 20.dp,
                                            bottom = 10.dp,
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    OutlinedButton(
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        onClick = {
                                            launchHandleLoanRequestPopUp = false
                                        },
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .height(30.dp),
                                    ) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )

                                    }
                                    OutlinedButton(
                                        colors = ButtonDefaults.outlinedButtonColors(containerColor = actionButtonColor),
                                        border = BorderStroke(
                                            width = 0.dp,
                                            color = actionButtonColor
                                        ),
                                        onClick = {
                                            launchHandleLoanRequestPopUp = false
                                        },
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .height(30.dp),
                                    ) {

                                        Text(
                                            text = "Proceed",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    }
}