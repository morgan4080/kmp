package com.presta.customer.ui.components.pendingApprovals.ui

import com.presta.customer.ui.composables.ShimmerBrush
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.network.loanRequest.model.PrestaLoanApplicationStatusResponse
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.formatDate
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PendingApprovalsContent(
    authState: AuthStore.State,
    modeOfDisbursementState: ModeOfDisbursementStore.State,
    onBack: () -> Unit,
    checkAuthenticatedUser: () -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }

    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true
        checkAuthenticatedUser()
        delay(1500)
        refreshing = false
    }

    var launchPop by remember { mutableStateOf(false) }
    var selectedLoan by remember { mutableStateOf<PrestaLoanApplicationStatusResponse?>(null) }
    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    val translateY: Float by animateFloatAsState(
        targetValue = if (launchPop) 0f else 2000f,
        animationSpec = tween(200, easing = FastOutSlowInEasing),
        finishedListener = {
            if (!launchPop) {
                selectedLoan = null
            }
        }
    )

    Scaffold(
        topBar = {
            NavigateBackTopBar("Approvals", onClickContainer = {
                onBack()
            })
        }
    ) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp)
            .padding(paddingValues)
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = if (modeOfDisbursementState.loans.isNotEmpty()) "Loans pending approval" else "",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )

            Box (modifier = Modifier
                .padding(top = 16.dp)
                .pullRefresh(refreshState)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (modeOfDisbursementState.loans.isNotEmpty() && !refreshing) {
                        modeOfDisbursementState.loans.map { pendingLoan ->
                            item {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                ) {
                                    ElevatedCard(
                                        onClick = {
                                            selectedLoan = pendingLoan
                                            launchPop = true
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                            .background(color = MaterialTheme.colorScheme.background)
                                    ) {
                                        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
                                            Row(
                                                modifier = Modifier.padding(
                                                    top = 9.dp,
                                                    bottom = 9.dp
                                                )
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = pendingLoan.productName,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.padding(start = 15.dp)
                                                            .fillMaxWidth(0.4F),
                                                        fontSize = 14.sp,
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )

                                                    Text(
                                                        text = formatDate(pendingLoan.applicationDate),
                                                        modifier = Modifier.padding(start = 15.dp),
                                                        fontSize = 10.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }

                                                Row {
                                                    Column(modifier = Modifier.padding(end = 10.dp)) {
                                                        Text(
                                                            text = formatMoney(pendingLoan.amount) + " KES",
                                                            modifier = Modifier.padding(start = 15.dp)
                                                                .align(Alignment.End),
                                                            fontSize = 14.sp,
                                                            color = MaterialTheme.colorScheme.onBackground,
                                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                                                        )

                                                        Text(
                                                            text = when(pendingLoan.applicationStatus) {
                                                                LoanApplicationStatus.NEWAPPLICATION -> "APPROVAL PENDING"
                                                                LoanApplicationStatus.COMPLETED -> "COMPLETED"
                                                                LoanApplicationStatus.INPROGRESS -> "IN PROGRESS"
                                                                LoanApplicationStatus.FAILED -> "FAILED"
                                                                LoanApplicationStatus.INITIATED -> "INITIATED"
                                                            },
                                                            modifier = Modifier.padding(start = 15.dp)
                                                                .align(Alignment.End),
                                                            fontSize = 10.sp,
                                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                            color = Color.Red
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (!refreshing && !modeOfDisbursementState.isLoading && modeOfDisbursementState.loans.isEmpty()) {
                        item {
                            Column (
                                modifier = Modifier.fillMaxWidth().height(500.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.size(150.dp),
                                    imageVector = Icons.Filled.ChatBubble,
                                    contentDescription = null,
                                    tint = actionButtonColor
                                )
                                Spacer(Modifier.height(20.dp))
                                Text(
                                    style = MaterialTheme.typography.labelLarge,
                                    text = "You have no pending approvals",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else if (modeOfDisbursementState.isLoading || refreshing) {
                        items(6) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
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
                                    ) {
                                    }
                                }
                            }
                        }
                    }

                    item {
                        if (
                            authState.error !== null || modeOfDisbursementState.error !== null
                        ) {
                            Column(modifier = Modifier
                                .clip(shape = RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                .padding(16.dp)
                            ) {
                                authState.error?.let { txt -> Text(style = MaterialTheme.typography.labelSmall, text = txt) }
                                modeOfDisbursementState.error?.let { txt -> Text(style = MaterialTheme.typography.labelSmall, text = txt) }
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing,
                    refreshState,
                    Modifier.align(Alignment.TopCenter).zIndex(1f),
                    contentColor = actionButtonColor
                )
            }
        }

        selectedLoan?.let { pendingLoan ->
            Popup {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color.Black.copy(alpha = 0.7f))
                        .graphicsLayer {
                            translationY = translateY
                        },
                    verticalArrangement = Arrangement.Top
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .padding(
                                start = 25.dp,
                                end = 25.dp,
                                top = 100.dp
                            )
                    ) {
                        Column (
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
                                        text = "KES "+ formatMoney(pendingLoan.amount) ,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                    )
                                }

                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                ) {
                                    LoanDetailsRow("Requested Amount","KES "+formatMoney(pendingLoan.amount))
                                    LoanDetailsRow("Interest", "${pendingLoan.interestRate}" + " %")
                                    LoanDetailsRow("Loan  Period", pendingLoan.loanPeriod)
                                    LoanDetailsRow("Application date", formatDate(pendingLoan.applicationDate))
                                    LoanDetailsRow("Balance brought forward", formatMoney(pendingLoan.balanceBF))
                                    LoanDetailsRow("Repayment amount", formatMoney(pendingLoan.repaymentAmount))
                                }

                                Row (
                                    modifier = Modifier
                                        .padding(top = 40.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        modifier = Modifier.width(150.dp)
                                            .border(
                                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                                shape = RoundedCornerShape(size = 12.dp)
                                            ),
                                        shape = RoundedCornerShape(size = 12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = MaterialTheme.colorScheme.primary
                                        ),
                                        onClick = {
                                            launchPop = false
                                        }
                                    ) {
                                        Text(
                                            text = "Dismiss",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
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

@Composable
fun LoanDetailsRow(
    label: String,
    data: String?,
) {
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
            text = data ?: "",
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
        )
    }
}