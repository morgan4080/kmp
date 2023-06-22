package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun DisbursementDetailsContainer(
    component: LoanConfirmationComponent,
    modeOfDisbursementState: ModeOfDisbursementStore.State,
) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 10.dp)
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
                        text = if(modeOfDisbursementState.prestaLoanQuotation!=null) "Disbursement Amount " else "",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                modifier = Modifier
                                .clip(shape = RoundedCornerShape(12.dp))
                            .defaultMinSize(minHeight = 20.dp, minWidth = 150.dp)
                            .background(brush = ShimmerBrush(modeOfDisbursementState.prestaLoanQuotation==null, 800f))
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth()
                ) {
                    if (modeOfDisbursementState.prestaLoanQuotation==null){

                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "",
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .defaultMinSize(minHeight = 10.dp, minWidth = 100.dp)
                                    .background(brush = ShimmerBrush(true, 800f))
                                    .padding(top = 10.dp)
                            )
                        }

                    }else{

                        Text(
                            text = "Kes " + formatMoney(component.amount),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                        )

                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    //Amount
                    disbursementDetailsRow(
                        "Requested Amount",
                        "Kes " + formatMoney(component.amount,
                        ),
                        modeOfDisbursementState
                    )
                    //Interest
                    disbursementDetailsRow("Interest", component.loanInterest + " %", modeOfDisbursementState)
                    //Fee  Charges

                    //Loan  Period
                    disbursementDetailsRow(
                        "Loan  Period",
                        component.loanPeriod.toString() + " " +
                                component.loanPeriodUnit,
                        modeOfDisbursementState
                    )
                    // Due date
                    disbursementDetailsRow(
                        "Maturity date",
                        if (modeOfDisbursementState.prestaLoanQuotation !== null) modeOfDisbursementState.prestaLoanQuotation.maturityDate else "",
                        modeOfDisbursementState
                    )
                    disbursementDetailsRow(
                        "Balance brought forward",
                        if (modeOfDisbursementState.prestaLoanQuotation !== null) "Kes" + formatMoney(
                            modeOfDisbursementState.prestaLoanQuotation.balanceBroughtForward
                        ) else "",
                        modeOfDisbursementState
                    )
                    disbursementDetailsRow(
                        "Repayment amount",
                        if (modeOfDisbursementState.prestaLoanQuotation !== null) "Kes" + formatMoney(
                            modeOfDisbursementState.prestaLoanQuotation.totalAmount
                        ) else "",
                        modeOfDisbursementState
                    )
                }
            }
        }
    }
}

@Composable
fun disbursementDetailsRow(
    label: String, data: String?,
    modeOfDisbursementState: ModeOfDisbursementStore.State? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (modeOfDisbursementState?.prestaLoanQuotation == null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "",
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .defaultMinSize(minHeight = 20.dp, minWidth = 150.dp)
                        .background(brush = ShimmerBrush(true, 800f))
                )

                Text(
                    text = "",
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .defaultMinSize(minHeight = 20.dp, minWidth = 100.dp)
                        .background(brush = ShimmerBrush(true, 800f))
                )
            }

        } else {

            Text(
                text = label,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
            )

            Text(
                text = data ?: "",
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
            )
        }
    }
}