package com.presta.customer.ui.components.savings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.savings.store.SavingsStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.CurrentSavingsContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.singleTransaction
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun SavingsContent(
    authState: AuthStore.State,
    state:  SavingsStore.State,
    onAddSavingsSelected: (shareAmount: Double) -> Unit,
    onSeeALlSelected: () -> Unit,
    onBack: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .padding(LocalSafeArea.current)
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                NavigateBackTopBar("Savings", onClickContainer = {
                    onBack()
                })

            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = Color.Transparent)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Savings Overview",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                )

                CurrentSavingsContainer(
                    state.savingsBalances
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 26.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transactions",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 1.dp)
                        .weight(1f)
                ) {
                    item {
                        singleTransaction(state.savingsTransactions)
                    }
                }

                //Action Button
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
                    .padding(top = 30.dp, bottom = 90.dp)) {
                    ActionButton("+ Add Savings", onClickContainer = {
                        //Navigate to Add savings Screen
                        if (state.savingsBalances !== null) {
                            onAddSavingsSelected(state.savingsBalances.pricePerShare.toDouble())
                        }
                    })
                }
            }
        }
    }
}