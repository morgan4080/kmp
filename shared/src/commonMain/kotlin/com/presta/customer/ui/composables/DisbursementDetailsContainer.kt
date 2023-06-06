package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import dev.icerock.moko.resources.compose.fontFamilyResource


@Composable
fun DisbursementDetailsContainer(
    state: ShortTermLoansStore.State) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 10.dp)
            .border(BorderStroke(0.5.dp, Color.White), shape = RoundedCornerShape(size = 12.dp))
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 23.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 24.dp,
                    )
                    .fillMaxHeight(0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Disbursement Amount ",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes 30,000",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                    )
                }
                //data Rows
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())){
                    //Amount
                        disbursementDetailsRow("Requested Amount", "Kes 30,000")
                    //Interest
                    disbursementDetailsRow("Interest", state.prestaShortTermLoanProductById?.interestRate.toString()+" %")

                    //Fee  Charges

                    //Loan  Period
                    disbursementDetailsRow("Loan  Period",
                        state.prestaShortTermLoanProductById?.maxTerm.toString()+ " "+
                    state.prestaShortTermLoanProductById?.loanPeriodUnit)
                    // Due date

                    //Balance Brought Forward

                    //Repayment  amount

                }
            }
        }
    }
}

@Composable
fun disbursementDetailsRow(label: String, data: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
        )

        Text(
            modifier = Modifier.background(
                brush = ShimmerBrush(
                    targetValue = 1300f,
                    showShimmer = data == null
                ),
                shape = RoundedCornerShape(12.dp)
            ).defaultMinSize(50.dp),
            text = if (data !== null) data else "",
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
            textAlign = TextAlign.End
        )
    }
}