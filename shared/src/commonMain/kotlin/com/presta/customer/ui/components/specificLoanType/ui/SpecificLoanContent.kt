package com.presta.customer.ui.components.specificLoanType.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.specificLoanType.SpecificLoansComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.LoanLimitContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource


@Composable
fun SpecificLoaContent(
    component: SpecificLoansComponent,
    authState: AuthStore.State,
    state: ShortTermLoansStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues,
) {
    var amount by remember {
        mutableStateOf(TextFieldValue())
    }
    var isError by remember { mutableStateOf(false) }
    var isPeriodError by remember { mutableStateOf(false) }
    val allowedMaxAmount = state.prestaLoanEligibilityStatus?.amountAvailable
    val allowedMinAmount = state.prestaShortTermLoanProductById?.minAmount
    val allowedMaxTerm = state.prestaShortTermLoanProductById?.maxTerm
    val allowedMinTerm = state.prestaShortTermLoanProductById?.minTerm
    val loanPeriodUnit = state.prestaShortTermLoanProductById?.loanPeriodUnit
    val referencedLoanRefId = null
    val  currentTerm = false
    val focusRequester = remember { FocusRequester() }
    var desiredPeriod by remember { mutableStateOf(TextFieldValue()) }
    val emptyDesiredPeriod by remember { mutableStateOf(TextFieldValue()) }

            if (allowedMaxTerm!=null ){
                desiredPeriod = TextFieldValue(allowedMaxTerm.toString())
            }

    val pattern = remember { Regex("^\\d+\$") }

    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NavigateBackTopBar(if (state.prestaShortTermLoanProductById?.name != null) component.loanName else "",
                    onClickContainer = {
                        component.onBackNavSelected()
                    })
            }
            LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxWidth()) {
                item {
                    Text(
                        modifier = Modifier,
                        text = "Enter Loan Amount",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )
                }
                item {
                    LoanLimitContainer(state)
                }
                item {
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        TextInputContainer(
                            "Enter the desired amount",
                            "",
                            inputType = InputTypes.NUMBER,
                        ) {
                            val inputValue: Double? = TextFieldValue(it).text.toDoubleOrNull()
                            if (inputValue != null) {
                                if ((inputValue >= allowedMinAmount!!.toDouble() && inputValue <= allowedMaxAmount!!.toDouble()) && (TextFieldValue(
                                        it
                                    ).text !== "")
                                ) {
                                    amount = TextFieldValue(it)
                                    isError = false

                                } else {
                                    isError = true
                                }
                            }
                        }
                    }
                }
                item {
                    if (isError) {

                        Text(
                            modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                            text = "min value Ksh " + allowedMinAmount.toString() + " max value Ksh " + allowedMaxAmount.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            color = Color.Red
                        )

                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .shadow(0.5.dp, RoundedCornerShape(10.dp))
                            .background(
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = RoundedCornerShape(10.dp)
                            ),
                    ) {
                        BasicTextField (
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .height(65.dp)
                                .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                                .absoluteOffset(y = 2.dp),
                            keyboardOptions = KeyboardOptions(keyboardType =KeyboardType.Number
                            ),
                            value = desiredPeriod,
                            onValueChange = {
                                if (it.text.isEmpty() || it.text.matches(pattern)) {
                                    desiredPeriod= it
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

                                if (desiredPeriod.text.isEmpty()
                                ) {
                                    Text(
                                        modifier = Modifier.alpha(.3f),
                                        text = "Enter desired loan term",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                AnimatedVisibility(
                                    visible = desiredPeriod.text.isNotEmpty(),
                                    modifier = Modifier.absoluteOffset(y = -(16).dp),
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically(),
                                ) {
                                    Text(
                                        text = "Enter Desired loan Term",
                                        color = primaryColor,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 11.sp
                                    )
                                }

                                Row (
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
                                        visible = desiredPeriod.text.isNotEmpty(),
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically(),
                                    ) {

                                        IconButton(
                                            modifier = Modifier.size(18.dp),
                                            onClick = { desiredPeriod=emptyDesiredPeriod},
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

                    if (allowedMaxTerm!=null && desiredPeriod.text!="") {
                        isPeriodError = !(desiredPeriod.text.toInt() >= allowedMinTerm!!.toInt() && desiredPeriod.text.toInt() <= allowedMaxTerm.toInt())

                    }
                }

                item {
                    if (isPeriodError) {
                        Text(
                            modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                            text = "min period is " + allowedMinTerm.toString() +" " + loanPeriodUnit.toString() + " max period is " + allowedMaxTerm.toString() +" " + loanPeriodUnit,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            color = Color.Red
                        )

                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 400.dp)
                    ) {
                        ActionButton(
                            "Confirm",
                            onClickContainer = {

                                if (amount.text !== "" && desiredPeriod.text !== "") {
                                    if (state.prestaShortTermLoanProductById !== null) {
                                        component.onConfirmSelected(
                                            state.prestaShortTermLoanProductById.refId.toString(),
                                            amount.text.toDouble(),
                                            desiredPeriod.text.toInt(),
                                            loanType = LoanType._NORMAL_LOAN,
                                            state.prestaShortTermLoanProductById.name.toString(),
                                            if (state.prestaShortTermLoanProductById.interestRate !== null) state.prestaShortTermLoanProductById.interestRate else 0.0,
                                            state.prestaShortTermLoanProductById.loanPeriodUnit.toString(),
                                            if (state.prestaShortTermLoanProductById.maxTerm !== null) state.prestaShortTermLoanProductById.maxTerm else 1,
                                            referencedLoanRefId =referencedLoanRefId,
                                            currentTerm = currentTerm
                                        )
                                    }
                                }

                            },
                            enabled = amount.text != "" && desiredPeriod.text !== "" && !isError && !isPeriodError
                        )
                    }
                }
            }
        }
    }
}


