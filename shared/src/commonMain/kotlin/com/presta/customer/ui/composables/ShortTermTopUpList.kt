package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ShortTermTopUpList(
    component: ShortTermLoansComponent,
    state: ShortTermLoansStore.State
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var enabled by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            Text(
                text = "Select Loan to Top Up",
                modifier = Modifier
                    .padding(top = 25.dp),
                fontSize = 14.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )

            if(state.prestaShortTermTopUpList?.loans==null){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center){
                    Text(text = "Top Ups have not been activated",
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp
                    )

                }

            }else{

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    state.prestaShortTermTopUpList.loans.mapIndexed { index, topUpList ->
                        item {
                            TopUpListView(
                                Index = index,
                                selected = selectedIndex == index,
                                onClick = { index: Int ->
                                    selectedIndex = if (selectedIndex == index) -1 else index
                                    if(!enabled){
                                        enabled=true
                                    }

                                },
                                topUpList.name.toString(),
                                topUpList.maxAmount.toString(),
                                topUpList.loanBalance.toString(),
                                topUpList.daysAvailable.toString()
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 26.dp)
        ) {
            ActionButton("Proceed", onClickContainer = {
                if (state.prestaShortTermTopUpList !== null && state.prestaShortTermTopUpList.loans !== null) {
                    if (state.prestaShortTermTopUpList.loans.isNotEmpty()) {
                        val maxAmount = state.prestaShortTermTopUpList.loans[selectedIndex].maxAmount
                        val minAmount = state.prestaShortTermTopUpList.loans[selectedIndex].minAmount
                        val loaRefId = state.prestaShortTermTopUpList.loans[selectedIndex].loanRefId
                        val interestRate = state.prestaShortTermTopUpList.loans[selectedIndex].interest
                        val loanName = state.prestaShortTermTopUpList.loans[selectedIndex].name
                        val loanPeriod = state.prestaShortTermTopUpList.loans[selectedIndex].maxPeriod
                        val loanPeriodUnit = state.prestaShortTermTopUpList.loans[selectedIndex].termUnit
                        val referencedLoanRefId= state.prestaShortTermTopUpList.loans[selectedIndex].loanRefId
                        if (maxAmount != null) {
                            if (minAmount != null) {
                                if (loaRefId != null) {
                                    if (loanName != null) {
                                        if (interestRate != null) {
                                            if (loanPeriodUnit != null) {
                                                if (referencedLoanRefId != null) {
                                                    component.onConfirmSelected(
                                                        loaRefId,
                                                        maxAmount,
                                                        minAmount,
                                                        loanName,
                                                        interestRate,
                                                        loanPeriod.toString(),
                                                        loanPeriodUnit,
                                                        referencedLoanRefId
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }, enabled = enabled && selectedIndex >= 0  )
        }
        Spacer(
            modifier = Modifier
                .padding(bottom = 80.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpListView(
    Index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    name: String,
    Amount: String,
    Balance: String,
    DaysAvailable: String
) {
    ElevatedCard(
        onClick = {
            onClick.invoke(Index)
        },
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Row(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onClick.invoke(Index) }
                )
                Column {
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        color= MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )

                    Text(
                        text = "KES " + formatMoney(Amount.toDouble()),
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                }
                Row{
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text(
                            text ="Balance. KES "+ formatMoney(Balance.toDouble()) ,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = DaysAvailable,
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                    }
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
            }
        }
    }
}