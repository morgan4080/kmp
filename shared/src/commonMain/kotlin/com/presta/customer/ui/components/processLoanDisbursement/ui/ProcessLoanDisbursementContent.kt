package com.presta.customer.ui.components.processLoanDisbursement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.processLoanDisbursement.store.ProcessingLoanDisbursementStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ProcessLoanDisbursementContent(
    amount: Double,
    fees: Double,
    phoneNumber: String?,
    state:ProcessingLoanDisbursementStore.State
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            NavigateBackTopBar("", onClickContainer = {

            })

        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Phone Number $phoneNumber",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "KSH ${formatMoney(amount)}",
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Fees ${formatMoney(fees)}",
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }

        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(70.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.then(Modifier.size(60.dp).alpha(1f)),
                    )
                }

            }
            Row(
                modifier = Modifier.padding(start = 80.dp, end = 80.dp, top = 22.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (state.paymentStatus!=null)"Your transaction is ${state.paymentStatus.status}" else "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}