package com.presta.customer.ui.components.specificLoanType

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.LoanLimitContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.TextInputContainer
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun SpecificLoansScreen(
    component: SpecificLoansComponent
) {
    val inputValue = ""
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NavigateBackTopBar("Emergency Loan", onClickContainer = {
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
                LoanLimitContainer()
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    TextInputContainer("Enter the desired amount", "", inputType = InputTypes.NUMBER)

                }
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {

                    TextInputContainer("Desired Period(Months)", inputValue, inputType = InputTypes.NUMBER)
                }

                //action Button
                Row(
                    modifier = Modifier
                        .padding(top = 30.dp)
                ) {
                    ActionButton("Confirm", onClickContainer = {
                        //Navigate  to confirm Screen
                        component.onConfirmSelected()

                    })

                }

            }

        }

    }

}