package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.shortTermLoans.model.PrestaLoanEligibilityResponse
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.helpers.formatDDMMYY
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.helpers.isOverdue
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShortTermTopUpList(
    component: ShortTermLoansComponent,
    state: ShortTermLoansStore.State,
    eligibilityResponse: PrestaLoanEligibilityResponse?,
    authState: AuthStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var enabled by remember { mutableStateOf(false) }

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        refreshing = false
    }

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
                text = if (eligibilityResponse !== null && eligibilityResponse.isEligible) "Select loan to topup" else   "",
                modifier = Modifier.padding(top = 22.dp),
                fontSize = 14.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )

            val refreshState = rememberPullRefreshState(refreshing, ::refresh,)

            Box(Modifier.pullRefresh(refreshState)) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        state.prestaShortTermTopUpList?.loans?.mapIndexed { index, topUpList ->
                            item {
                                    if (eligibilityResponse != null) {
                                        topUpList.minAmount?.let {
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
                                                eligibilityResponse.amountAvailable,
                                                it,
                                                topUpList.loanBalance.toString(),
                                                if ( topUpList.dueDate!=null)  "Due - "  + topUpList.dueDate else ""
                                            )
                                        }
                                    }
                            }
                        }

                        item {
                            if (
                                state.error !== null || authState.error !== null
                            ) {
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                    .padding(16.dp)
                                ) {
                                    state.error?.let { txt -> Text(style = MaterialTheme.typography.labelSmall, text = txt) }
                                    authState.error?.let { txt -> Text(style = MaterialTheme.typography.labelSmall, text = txt) }
                                }
                            }
                        }
                    }
                PullRefreshIndicator(refreshing, refreshState,
                    Modifier
                        .align(Alignment.TopCenter),
                    contentColor = actionButtonColor
                )

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
                        val referencedLoanRefId = state.prestaShortTermTopUpList.loans[selectedIndex].productRefId
                        val interestRate = state.prestaShortTermTopUpList.loans[selectedIndex].interest
                        val loanName = state.prestaShortTermTopUpList.loans[selectedIndex].name
                        val maxLoanPeriod = state.prestaShortTermTopUpList.loans[selectedIndex].maxPeriod
                        val loanPeriodUnit = state.prestaShortTermTopUpList.loans[selectedIndex].termUnit
                        val minLoanPeriod = state.prestaShortTermTopUpList.loans[selectedIndex].minPeriod
                        val loanRefIds = state.prestaShortTermTopUpList.loans[selectedIndex].loanRefId
                        val loanRefId= state.prestaShortTermTopUpList.loans[selectedIndex].loanRefId
                        if (maxAmount != null) {
                            if (minAmount != null) {
                                if (loanRefId != null) {
                                    if (loanName != null) {
                                        if (interestRate != null) {
                                            if (loanPeriodUnit != null) {
                                                if (referencedLoanRefId != null) {
                                                    if (loanRefIds != null) {
                                                        if (minLoanPeriod != null) {
                                                            if (maxLoanPeriod != null) {
                                                                component.onProceedSelected(
                                                                    referencedLoanRefId,
                                                                    loanRefId,
                                                                    maxAmount,
                                                                    minAmount,
                                                                    loanName,
                                                                    interestRate,
                                                                    maxLoanPeriod,
                                                                    loanPeriodUnit,
                                                                    minLoanPeriod,
                                                                    loanRefIds
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
    maxAmount: Double,
    minAmount:Double,
    balance: String,
    daysAvailable: String,
) {
    ElevatedCard(
        onClick = {
            onClick.invoke(Index)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 5.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Row(
                modifier = Modifier
                    .padding(top = 9.dp, bottom = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    modifier = Modifier.clip(shape = CircleShape),
                    checked = selected,
                    onCheckedChange = { onClick.invoke(Index) }
                )
                Column {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        text = name,
                        fontSize = 14.sp,
                        color= MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )
                    Row {
                        Text(
                            text =  formatMoney(minAmount),
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = " - " + formatMoney(maxAmount),
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                    }
                }
                Row{
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text(
                            modifier = Modifier
                                .align(Alignment.End),
                            text ="Bal. KES "+ formatMoney(balance.toDouble()) ,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            modifier = Modifier.align(Alignment.End),
//                            color = if (isOverdue(daysAvailable)) Color.Red else MaterialTheme.colorScheme.onBackground,
                            text = daysAvailable,
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