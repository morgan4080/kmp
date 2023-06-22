package com.presta.customer.ui.components.modeofDisbursement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ProductSelectionCard2
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ModeOfDisbursementContent(
    component: ModeOfDisbursementComponent,
    authState: AuthStore.State,
    state: ModeOfDisbursementStore.State,
    onEvent: (ModeOfDisbursementStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    ) {


    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {

            Row(modifier = Modifier.fillMaxWidth()) {
                NavigateBackTopBar("Disbursement Method", onClickContainer = {
                    component.onBackNavSelected()

                })

            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
            ) {

                Text(
                    modifier = Modifier,
                    text = "Select Disbursement Method",
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    fontSize = 14.sp,

                    )
                Spacer(modifier = Modifier.padding(top = 25.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    //Navigate  to confirm  Screen  and execute  payment
                    ProductSelectionCard2("Mpesa", onClickContainer = {
                        //fees
                        //Due date
                        //Balance brought forward
                        //Repayment amount
                        component.onMpesaSelected(
                            correlationId = component.correlationId,
                            refId = component.refId,
                            amount = component.amount,
                            fees =   component.fees,
                            loanPeriod = component.loanPeriod,
                            loanType = component.loanType,
                            interestRate = component.interestRate,
                            LoanName = component.loanName,
                            loanPeriodUnit =component.loanPeriodUnit ,
                            referencedLoanRefId = component.referencedLoanRefId,
                            currentTerm =component.currentTerm
                        )
                    })
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 11.dp)) {
                    ProductSelectionCard2("Bank", onClickContainer = {
//                        component.onBankSelected()
                    })
                }
            }
        }
    }
}