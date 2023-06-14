package com.presta.customer.ui.components.loanConfirmation.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.DisbursementDetailsContainer
import com.presta.customer.ui.composables.NavigateBackTopBar

@Composable
fun LoanConfirmationContent(
    component: LoanConfirmationComponent,
    authState: AuthStore.State,
    state: ShortTermLoansStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues
) {
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                //set Title based on the  Data carried by the RefId
                NavigateBackTopBar(component.loanName, onClickContainer = {
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
                    text = "Confirm " + component.loanOperation + " Details",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
                //Disbursement Details
                DisbursementDetailsContainer(state, component)
                //action Button
                Row(modifier = Modifier.padding(top = 30.dp)) {
                    ActionButton("Confirm", onClickContainer = {
                        //Navigate  to mode of Disbursement
                        component.onConfirmSelected(
                            component.refId,
                            component.amount,
                            component.loanPeriod,
                            component.loanType,
                            loanName = component.loanName,
                            referencedLoanRefId =component.referencedLoanRefId,
                            currentTerm = component.currentTerm
                        )

                    })
                }
            }
        }
    }
}


