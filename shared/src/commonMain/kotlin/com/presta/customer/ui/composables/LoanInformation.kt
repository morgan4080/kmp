package com.presta.customer.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent
import dev.icerock.moko.resources.compose.fontFamilyResource

data class LoansData(
    val field: String,
    val value: String? = null,
)

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
            field = "Guarantors:",
        ),
        LoansData(
            field = "Witness:",
            value = component.witnessName
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row {
                        Column {
                            Text(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 8.dp, minWidth = 100.dp),
                                text = loanDetails.field,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                            )
                        }
                    }

                    Column {
                        if (loanDetails.field == "Guarantors:") {
                            component.guarantorList.map { listedData ->
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .fillMaxWidth(0.8f),
                                    text = listedData.guarantorFirstName + " " + listedData.guarantorLastName,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                )
                            }

                        } else {
                            loanDetails.value?.let {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .fillMaxWidth(0.8f),
                                    text = it,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 100.dp))
        }
    }
}
