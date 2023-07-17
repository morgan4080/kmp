package com.presta.customer.ui.components.shortTermLoans.ui

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShortTermProductList
import com.presta.customer.ui.composables.ShortTermTopUpList
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShortTermLoansContent(
    component: ShortTermLoansComponent,
    authState: AuthStore.State,
    state: ShortTermLoansStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues,
    reloadModels: () -> Unit,
) {
    var tabs = listOf ("Product", "Top Up")
    var tabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(state.prestaLoanEligibilityStatus) {
        if (state.prestaLoanEligibilityStatus !== null) {
            if (state.prestaLoanEligibilityStatus.loanType == LoanType._TOP_UP) {
                tabs = listOf("Top Up")
                tabIndex = 1
            } else {
                if (
                    !state.prestaLoanEligibilityStatus.isEligible &&
                    !state.prestaLoanEligibilityStatus.isEligibleForNormalLoanAndTopup
                ) {
                    tabIndex = 2
                }
            }
        }
    }

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true
        authState.cachedMemberData?.let {
            ShortTermLoansStore.Intent.GetPrestaLoanEligibilityStatus(
                token = it.accessToken,
                session_id = it.session_id,
                customerRefId = it.refId
            )
            ShortTermLoansStore.Intent.GetPrestaShortTermProductList(
                it.accessToken,
                it.refId
            )
            ShortTermLoansStore.Intent.GetPrestaShortTermTopUpList(
                token = it.accessToken,
                session_id = it.session_id,
                refId = it.refId
            )
        }?.let { onEvent.invoke(it) }
        delay(1500)
        refreshing = false

    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    Scaffold(
        modifier = Modifier
            .fillMaxHeight(),
        topBar = {
            NavigateBackTopBar("Short Term Loan", onClickContainer = {
                component.onBackNav()
            })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
        ) {
            if (
                tabs.isNotEmpty() &&
                !state.isLoading &&
                state.prestaLoanEligibilityStatus !== null &&
                tabIndex != 2
            ) {
                TabRow(selectedTabIndex = tabIndex,
                    containerColor = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(29.dp))
                        .background(Color.Gray.copy(alpha = 0.5f)),
                    indicator = {},
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(70.dp))
                        ) {
                            Card(
                                shape = RoundedCornerShape(70.dp),
                                colors = CardDefaults.cardColors(containerColor = if (tabIndex == index) Color.White else Color.Transparent),
                                modifier = Modifier
                                    .padding(1.5.dp)
                            ) {

                                Tab(text = {
                                    Text(text = title,
                                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.medium),
                                        fontSize = 12.sp
                                    )
                                },
                                    selected = tabIndex == index,
                                    onClick = {
                                        tabIndex = index
                                    },
                                    modifier = Modifier
                                        .clip(shape= RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp, bottomStart = 70.dp, bottomEnd = 70.dp))
                                        .background(color = if (tabIndex == index) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)),
                                    selectedContentColor = Color.Black,
                                    unselectedContentColor = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
            if (
                !state.isLoading &&
                state.prestaLoanEligibilityStatus !== null
            ) {
                when (tabIndex) {
                    0 -> ShortTermProductList(
                        component,
                        state,
                        authState,
                        onEvent,
                        state.prestaLoanEligibilityStatus
                    )

                    1 -> ShortTermTopUpList(
                        component,
                        state,
                        state.prestaLoanEligibilityStatus,
                        authState,
                        onEvent
                    )

                    2 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(0.7f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(150.dp),
                                    imageVector = Icons.Filled.ChatBubble,
                                    contentDescription = null,
                                    tint = actionButtonColor
                                )
                            }
                            Box(modifier = Modifier.pullRefresh(refreshState)) {
                                LazyColumn {
                                    item {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(0.8f)
                                                .padding(top = 25.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "It seems you are not eligible to borrow, reason:",
                                                color = MaterialTheme.colorScheme.onBackground,
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontFamily = fontFamilyResource(MR.fonts.Metropolis.bold),
                                                textAlign = TextAlign.Center,
                                                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(0.8f),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = state.prestaLoanEligibilityStatus.description,
                                                color = actionButtonColor,
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold),
                                                textAlign = TextAlign.Center,
                                                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
                                            )
                                        }
                                    }
                                }

                                PullRefreshIndicator(
                                    refreshing,
                                    refreshState,
                                    Modifier.align(Alignment.TopCenter),
                                    contentColor = actionButtonColor
                                )
                            }
                        }
                    }
                }
            }

            if (state.isLoading) {
                listOf(1,2,3,4,5,6,7,8).map {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(40.dp, 40.dp)
                                    .background(
                                        ShimmerBrush(
                                            targetValue = 1300f,
                                            showShimmer = true
                                        )
                                    )
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}