package com.presta.customer.ui.components.specificLoanType.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.specificLoanType.SpecificLoansComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.LoanLimitContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.TextInputContainer
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
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
    var desiredPeriod by remember {
        mutableStateOf(TextFieldValue())
    }
    var isError by remember { mutableStateOf(false) }
    var isPeriodError by remember { mutableStateOf(false) }
    val allowedMaxAmount = state.prestaShortTermLoanProductById?.maxAmount
    val allowedMinAmount = state.prestaShortTermLoanProductById?.minAmount
    val allowedMaxTerm = state.prestaShortTermLoanProductById?.maxTerm
    val allowedMinTerm = state.prestaShortTermLoanProductById?.minTerm
    val loanPeriodUnit = state.prestaShortTermLoanProductById?.loanPeriodUnit
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
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .background(color = MaterialTheme.colorScheme.background)
                            .fillMaxHeight()
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Enter Loan  Amount",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                        LoanLimitContainer(state)

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

                        if (isError) {

                            Text(
                                modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                                text = "min value Ksh " + allowedMinAmount.toString() + " max value Ksh " + allowedMaxAmount.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                color = Color.Red
                            )

                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                        ) {
                            TextInputContainer(
                                "Desired Period(Months)",
                                inputType = InputTypes.NUMBER,
                                inputValue = ""
                            ) {
                                val inputPeriod: Int? = TextFieldValue(it).text.toIntOrNull()
                                if (inputPeriod != null) {
                                    if ((inputPeriod >= allowedMinTerm!!.toInt() && inputPeriod <= allowedMaxTerm!!.toInt()) && (TextFieldValue(
                                            it
                                        ).text !== "")
                                    ) {
                                        desiredPeriod = TextFieldValue(it)
                                        isPeriodError = false

                                    } else {
                                        isPeriodError = true
                                    }
                                }
                            }
                        }
                        if (isPeriodError) {
                            Text(
                                modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                                text = "min period is " + allowedMinTerm.toString() + loanPeriodUnit.toString() + " max period is " + allowedMaxTerm.toString() + loanPeriodUnit,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                color = Color.Red
                            )

                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 30.dp)
                        ) {
                            ActionButton(
                                "Confirm",
                                onClickContainer = {
                                    //Navigate  to confirm Screen
                                    if (amount.text !== "" && desiredPeriod.text !== "") {
                                        state.prestaShortTermLoanProductById?.interestRate?.let {
                                            state.prestaShortTermLoanProductById.maxTerm?.let { it1 ->
                                                component.onConfirmSelected(
                                                    state.prestaShortTermLoanProductById.refId.toString(),
                                                    amount.text.toDouble(),
                                                    desiredPeriod.text,
                                                    state.prestaShortTermLoanProductById.name.toString(),
                                                    state.prestaShortTermLoanProductById.name.toString(),
                                                    it,
                                                    state.prestaShortTermLoanProductById.loanPeriodUnit.toString(),
                                                    it1
                                                )
                                            }
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
}


