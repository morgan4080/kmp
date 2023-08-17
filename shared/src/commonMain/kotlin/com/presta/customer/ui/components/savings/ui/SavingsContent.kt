package com.presta.customer.ui.components.savings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.moriatsushi.insetsx.ExperimentalSoftwareKeyboardApi
import com.presta.customer.MR
import com.presta.customer.ui.components.savings.store.SavingsStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.CurrentSavingsContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.singleTransaction
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalSoftwareKeyboardApi::class
)
@Composable
fun SavingsContent(
    state:  SavingsStore.State,
    onAddSavingsSelected: (shareAmount: Double) -> Unit,
    onBack: () -> Unit,
    loadEssentials: () -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }

    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true
        loadEssentials()
        delay(1500)
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(),
        topBar = {
            NavigateBackTopBar("Savings", onClickContainer = {
                onBack()
            })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
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

                Box (modifier = Modifier.weight(1f).pullRefresh(refreshState)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 1.dp)
                    ) {
                        item {
                            singleTransaction(state.savingsTransactions)
                        }
                    }

                    PullRefreshIndicator(refreshing, refreshState,
                        Modifier
                            .align(Alignment.TopCenter).zIndex(1f),
                        contentColor = actionButtonColor
                    )
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