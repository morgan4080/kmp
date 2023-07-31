package com.presta.customer.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent
import dev.icerock.moko.resources.compose.fontFamilyResource

data class LoansData(val field: String, val value: String)

@Composable
fun LoanInformation(component: LongTermLoanConfirmationComponent) {
    val loanDetailsListing = listOf(
        LoansData(
            field = "Loan Type:",
            value = component.loanType
        ),
        LoansData(
            field = "Months:",
            value = component.loanPeriod.toString()
        ),
        LoansData(
            field = "Amount:",
            value = component.desiredAmount.toString()
        ),
        LoansData(
            field = "Cuarantors:",
            value = ""
        ),
        LoansData(
            field = "Witness:",
            value = ""
        ),
        LoansData(
            field = "Category:",
            value = component.loanCategory
        ),
        LoansData(
            field = "Purpose:",
            value = component.loanPurpose + " :  " + component.loanPurposeCategory
        )
    )
    LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
        loanDetailsListing.map { loanDetails ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        loanDetails.field,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        modifier = Modifier.fillMaxWidth(0.4f)
                    )
                    Text(
                        loanDetails.value,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 100.dp))
        }
    }
}
