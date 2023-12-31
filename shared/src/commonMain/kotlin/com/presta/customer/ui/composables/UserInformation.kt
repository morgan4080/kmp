package com.presta.customer.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
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
import com.presta.customer.ui.theme.primaryColor
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
    onSignProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    var selectedDisburseMentIndex by remember { mutableStateOf(-1) }
    var selectedPaymentModeIndex by remember { mutableStateOf(-1) }
    var launchDisbursementModePopUp by remember { mutableStateOf(false) }
    var launchPaymentModePopUp by remember { mutableStateOf(false) }
    var sendLoanRequest by remember { mutableStateOf(false) }
    var launchHandleLoanRequestPopUp by remember { mutableStateOf(false) }
    var disbursementMode by remember { mutableStateOf("") }
    var repaymentMode by remember { mutableStateOf("") }
    var employer by remember { mutableStateOf("") }
    var employmentTerms by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("") }
    var postalAddress by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var grossSalary by remember { mutableStateOf("") }
    var netSalary by remember { mutableStateOf("") }
    var kraPin by remember { mutableStateOf("") }
    var employmentNumber by remember { mutableStateOf("") }
    var businessLocation by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("") }
    var guarantor1_fosa_account by remember { mutableStateOf("") }
    var guarantor2_fosa_account by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var poBox by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var telephoneNumber by remember { mutableStateOf("") }

    val guarantorList = arrayListOf<Guarantor>()
    for (item in component.guarantorList) {
        val refId = item.guarantorRefId
        val amount = item.amount
        val guarantorName = item.guarantorFirstName
        guarantorList.add(Guarantor(refId, amount, guarantorName))
        guarantor1_fosa_account=item.guarantor1_fosa_account
        guarantor2_fosa_account=item.guarantor2_fosa_account
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(signProfileState.error) {
        if (signProfileState.error !== null) {
            snackbarHostState.showSnackbar(
                signProfileState.error
            )
            // clear error
            onSignProfileEvent(SignHomeStore.Intent.ClearError)
        }
    }
    LaunchedEffect(state.prestaLongTermLoanRequestData) {
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
        }
    }
    signProfileState.prestaTenantByPhoneNumber?.details?.map {
        if (it.key.contains("employer")) {
            employer = it.value.value.toString()
        }
        if (it.key.contains("employmentTerms")) {
            employmentTerms = it.value.value.toString()
        }
        if (it.key.contains("designation")) {
            designation = it.value.value.toString()
        }
        if (it.key.contains("postalAddress")) {
            postalAddress = it.value.value.toString()
        }
        if (it.key.contains("dob")) {
            dob = it.value.value.toString()
        }
        if (it.key.contains("department")) {
            department = it.value.value.toString()
        }
        if (it.key.contains("gross")) {
            grossSalary = it.value.value.toString()
        }
        if (it.key.contains("net")) {
            netSalary = it.value.value.toString()
        }
        if (it.key.contains("kra")) {
            kraPin = it.value.value.toString()
        }
        if (it.key.contains("employment")) {
            employmentNumber = it.value.value.toString()
        }
        if (it.key.contains("businessL")) {
            businessLocation = it.value.value.toString()
        }
        if (it.key.contains("businessT")) {
            businessType = it.value.value.toString()
        }
        if (it.key.contains("postal_code")) {
        postalCode = it.value.value.toString()
        }
        if (it.key.contains("po_box")) {
            poBox = it.value.value.toString()
        }
        if (it.key.contains("city")) {
            city = it.value.value.toString()
        }
        if (it.key.contains("telephone_number")) {
            telephoneNumber = it.value.value.toString()
        }
    }



    var disbursementModeListing = listOf(
        DisbursementModes("Cheques", "CHEQUE", selected = true),
        DisbursementModes("My Account", "MY_ACCOUNT", selected = true),
        DisbursementModes("EFT", "EFT", selected = true),
        DisbursementModes("RTGS", "RTGS", selected = true)
    )

    signProfileState.prestaClientSettings?.let {
        it.response.details?.let { detailsMap ->
            detailsMap.map { det ->
                if (det.key == "disbursement_mode" && det.value.type == "ENUM")  {
                    disbursementModeListing = det.value.meta.keys.map { key ->
                        DisbursementModes(key, key, selected = true)
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.padding(top = 20.dp)) {
        // popup Disbursement mode
        // Added the popUps on top of Parent Column to prevent them from freezing
        if (launchDisbursementModePopUp) {
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
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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

                                        disbursementModeListing.mapIndexed { indexed, disbursementModes ->
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
                                                        selected = selectedDisburseMentIndex == indexed,
                                                        onClick = { index: Int ->
                                                            selectedDisburseMentIndex =
                                                                if (selectedDisburseMentIndex == index) -1 else index
                                                            if (selectedDisburseMentIndex > -1) {
                                                                disbursementMode =
                                                                    disbursementModeListing[selectedDisburseMentIndex].name
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                                        idx = indexedPay,
                                                        selected = selectedPaymentModeIndex == indexedPay,
                                                        onClick = { index: Int ->
                                                            selectedPaymentModeIndex  =
                                                                if (selectedPaymentModeIndex  == index) -1 else index
                                                            if (selectedPaymentModeIndex > -1) {
                                                                repaymentMode = repaymentmentModeListing[selectedPaymentModeIndex ].name
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )
                            }
                        }
                    }
                }
            }
        }
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )
                            }
                        }
                    }
                }
            }
        }
        LazyColumn {
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
                listOf(
                    signProfileState.firstName,
                    signProfileState.lastName,
                    signProfileState.email,
                    signProfileState.idNumber,
                    signProfileState.introducer,
                ).map { inputMethod ->
                    item {
                        when(inputMethod.inputTypes) {
                            InputTypes.ENUM -> {
                                val (selectedOption, onOptionSelected) = remember { mutableStateOf(inputMethod.enumOptions[0]) }

                                Column(modifier = Modifier.selectableGroup()) {
                                    inputMethod.enumOptions.forEach { text ->
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                                .selectable(
                                                    selected = (text == selectedOption),
                                                    onClick = {
                                                        onOptionSelected(text)
                                                        if (inputMethod.enabled) {
                                                            onSignProfileEvent(
                                                                SignHomeStore.Intent.UpdateInputValue(
                                                                    inputMethod.fieldType,
                                                                    TextFieldValue(text)
                                                                )
                                                            )
                                                        }
                                                    },
                                                    role = Role.RadioButton
                                                )
                                                .padding(horizontal = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = (text == selectedOption),
                                                onClick = null // null recommended for accessibility with screenreaders
                                            )
                                            Text(
                                                text = text,
                                                style = MaterialTheme.typography.bodySmall.merge(),
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            else -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .shadow(0.5.dp, RoundedCornerShape(10.dp))
                                        .background(
                                            color = MaterialTheme.colorScheme.inverseOnSurface,
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                ) {
                                    BasicTextField(
                                        modifier = Modifier
                                            .focusRequester(focusRequester)
                                            .height(65.dp)
                                            .padding(
                                                top = 20.dp,
                                                bottom = 16.dp,
                                                start = 16.dp,
                                                end = 16.dp
                                            )
                                            .absoluteOffset(y = 2.dp),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType =
                                            when (inputMethod.inputTypes) {
                                                InputTypes.NUMBER -> KeyboardType.Number
                                                InputTypes.STRING -> KeyboardType.Text
                                                InputTypes.PHONE -> KeyboardType.Phone
                                                InputTypes.URI -> KeyboardType.Uri
                                                InputTypes.EMAIL -> KeyboardType.Email
                                                InputTypes.PASSWORD -> KeyboardType.Password
                                                InputTypes.NUMBER_PASSWORD -> KeyboardType.NumberPassword
                                                InputTypes.DECIMAL -> KeyboardType.Decimal
                                                else -> KeyboardType.Text
                                            }
                                        ),
                                        value = inputMethod.value,
                                        enabled = inputMethod.enabled,
                                        onValueChange = {
                                            if (inputMethod.enabled) {
                                                onSignProfileEvent(
                                                    SignHomeStore.Intent.UpdateInputValue(
                                                        inputMethod.fieldType,
                                                        it
                                                    )
                                                )
                                            }

                                        },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                                            fontSize = 13.sp,
                                            fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                            letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                            fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                                        ),
                                        decorationBox = { innerTextField ->

                                            if (inputMethod.value.text.isEmpty()
                                            ) {
                                                Text(
                                                    modifier = Modifier.alpha(.3f),
                                                    text = inputMethod.inputLabel,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }

                                            AnimatedVisibility(
                                                visible = inputMethod.value.text.isNotEmpty(),
                                                modifier = Modifier.absoluteOffset(y = -(16).dp),
                                                enter = fadeIn() + expandVertically(),
                                                exit = fadeOut() + shrinkVertically(),
                                            ) {
                                                Text(
                                                    text = inputMethod.inputLabel,
                                                    color = primaryColor,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 11.sp
                                                )
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) {

                                                    innerTextField()
                                                }

                                                AnimatedVisibility(
                                                    visible = inputMethod.value.text.isNotEmpty(),
                                                    enter = fadeIn() + expandVertically(),
                                                    exit = fadeOut() + shrinkVertically(),
                                                ) {

                                                    IconButton(
                                                        modifier = Modifier.size(18.dp),
                                                        onClick = {
                                                            if (inputMethod.enabled) {
                                                                onSignProfileEvent(
                                                                    SignHomeStore.Intent.UpdateInputValue(
                                                                        inputMethod.fieldType,
                                                                        TextFieldValue()
                                                                    )
                                                                )
                                                            }
                                                        },
                                                        content = {
                                                            Icon(
                                                                modifier = Modifier.alpha(0.4f),
                                                                imageVector = Icons.Filled.Cancel,
                                                                contentDescription = null,
                                                                tint = actionButtonColor
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }

                                if (inputMethod.errorMessage !== "") {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 22.dp),
                                        text = inputMethod.errorMessage,
                                        fontSize = 10.sp,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
                //Todo--remove this
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        ModeSelectionCard(label = "Disbursement Mode*",
                            description = if (disbursementMode != "") disbursementMode else "",
                            onClickContainer = {
                                launchDisbursementModePopUp = true

                            })
                    }


                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        ModeSelectionCard(label = "Repayment Mode*",
                            description = if (repaymentMode != "") repaymentMode else "",
                            onClickContainer = {
                                launchPaymentModePopUp = true
                            })
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        ActionButton(
                            label = "Submit  Loan Request", onClickContainer = {
                                authState.cachedMemberData?.let { memberData ->
                                    SignHomeStore.Intent.UpdatePrestaTenantPersonalInfo(
                                        token = memberData.accessToken,
                                        memberRefId = signProfileState.prestaTenantByPhoneNumber.refId,
                                        firstName = signProfileState.firstName.value.text,
                                        lastName = signProfileState.lastName.value.text,
                                        phoneNumber = signProfileState.introducer.value.text,
                                        idNumber = signProfileState.idNumber.value.text,
                                        email = signProfileState.email.value.text
                                    ) {
                                        authState.cachedMemberData.let { catchedData ->
                                            ApplyLongTermLoansStore.Intent.RequestLongTermLoan(
                                                token = catchedData.accessToken,
                                                details = DetailsData(
                                                    loan_purpose_1 = component.loanCategory,
                                                    loan_purpose_2 = component.loanPurpose,
                                                    loan_purpose_3 = component.loanPurposeCategory,
                                                    loanPurposeCode = component.loanPurposeCategoryCode,
                                                    loanPeriod = component.loanPeriod.toString(),
                                                    repayment_period = component.loanPeriod.toString(),
                                                    employer_name = employer,
                                                    employment_terms = employmentTerms,
                                                    employment_number = employmentNumber,
                                                    business_location = businessLocation,
                                                    business_type = businessType,
                                                    net_salary = netSalary,
                                                    gross_salary = grossSalary,
                                                    disbursement_mode = disbursementMode,
                                                    repayment_mode = repaymentMode,
                                                    loan_type = component.loanType,
                                                    kra_pin = kraPin,
                                                    witness_payroll_no = component.witnessPayrollNo,
                                                    designation = designation,
                                                    postal_address = postalAddress,
                                                    dob = dob,
                                                    postal_code= postalCode,
                                                    city=city,
                                                    po_box=poBox,
                                                    department=department,
                                                    telephone_number=telephoneNumber,
                                                    applicant_fosa_account="",
                                                    guarantor1_fosa_account=guarantor1_fosa_account,
                                                    guarantor2_fosa_account=guarantor2_fosa_account,
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
                                        }.let {
                                            onLongTermLoanEvent(
                                                it
                                            )
                                        }

                                        sendLoanRequest = true
                                    }
                                }?.let {
                                    onSignProfileEvent(
                                        it
                                    )
                                }
                            },
                            enabled = repaymentMode != "" && disbursementMode != "" && signProfileState.firstName.value.text != "" && signProfileState.lastName.value.text != "" && signProfileState.introducer.value.text != "" && signProfileState.email.value.text != "" && signProfileState.idNumber.value.text != "",
                            loading = state.isLoading
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    }
}

