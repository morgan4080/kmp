package com.presta.customer.ui.components.specificLoanType.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import org.koin.core.KoinApplication.Companion.init

@Composable
fun  SpecificLoaContent(
    component: SpecificLoansComponent,
    authState: AuthStore.State,
    state: ShortTermLoansStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues,
    ){
    var amount by remember {
        mutableStateOf(TextFieldValue())
    }
    var desiredPeriod by remember {
        mutableStateOf(TextFieldValue())
    }

    // Throw error if  user  enters wrong value on   TextField

    val allowedMaxAmount: Double? =state.prestaShortTermLoanProductById?.maxAmount
    val  allowedMinAmount: Double? = state.prestaShortTermLoanProductById?.minAmount
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NavigateBackTopBar( if (state.prestaShortTermLoanProductById?.name!=null) state.prestaShortTermLoanProductById.name.toString() else "",
                        onClickContainer = {
                            component.onBackNavSelected()
                        })
            }
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
                //container Card
                LoanLimitContainer(state)

                Row(modifier = Modifier.padding(top = 16.dp)) {
                    TextInputContainer("Enter the desired amount", "", inputType = InputTypes.NUMBER, callback = {
                        if (allowedMaxAmount !== null && allowedMinAmount !== null  && TextFieldValue(it).text !== "") {
//                            when {
//                                TextFieldValue(it).text.toDouble() <= allowedMaxAmount
//                                && TextFieldValue(it).text.toDouble() >= allowedMinAmount -> {
//                                    amount= TextFieldValue(it)
//                                }
//
//                            }
                        }
                    })

                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    //Desire  period >=min period && <=max period
                    //else  show   error
                    TextInputContainer("Desired Period(Months)", callback = {desiredPeriod=TextFieldValue(it) }, inputType = InputTypes.NUMBER, inputValue = "")
                }

                //action Button
                Row(
                    modifier = Modifier
                        .padding(top = 30.dp)
                ) {
                    ActionButton("Confirm", onClickContainer = {
                        //Navigate  to confirm Screen
                        if (amount.text!== "" && desiredPeriod.text!== ""){
                            //pass  amount  and the desired period
                            component.onConfirmSelected(state.prestaShortTermLoanProductById?.refId.toString())
                        }

                    }, enabled =amount.text!== "" && desiredPeriod.text!== "" )
                }
            }
        }
    }
}


