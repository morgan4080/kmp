package com.presta.customer.ui.components.payLoan.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.network.profile.model.LoanBreakDown
import com.presta.customer.ui.components.pendingApprovals.ui.LoanDetailsRow
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.LoanStatusContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PayLoanContent(
    onPaySelected: (desiredAmount: String, loan: LoanBreakDown) -> Unit,
    onBack: () -> Unit,
    state: ProfileStore.State
) {
    val stateLazyRow0 = rememberLazyListState()
    var amount by remember {
        mutableStateOf(TextFieldValue())
    }
    var launchPopUp by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold (
        modifier = Modifier
            .padding(LocalSafeArea.current)
            .fillMaxHeight(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(bottom = 80.dp)
            )
        },
        topBar = {
            NavigateBackTopBar("Pay Loan", onClickContainer = {
                onBack()
            })
        }
    ) {
        if (state.loansBalances !== null && state.loansBalances.loanBreakDown.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp)
                    .padding(it)
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Your Active Loans",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Metropolis.medium)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    state = stateLazyRow0,
                    flingBehavior = rememberSnapFlingBehavior(
                        lazyListState = stateLazyRow0
                    ),
                    content = {
                        if (state.isLoading) {
                            items(1) {
                                Box(
                                    modifier = Modifier.fillParentMaxWidth()
                                ) {
                                    LoanStatusContainer(
                                        totalAmount = null,
                                        amountDue = null,
                                        dueDate = null,
                                        loanType = null,
                                        loanStatus = null,
                                        loansCount = null,
                                        loanIndex = null
                                    )
                                }
                            }
                        } else {
                            state.loansBalances.loanBreakDown.map {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxWidth()
                                    ) {
                                        LoanStatusContainer(
                                            totalAmount = it.totalBalance,
                                            amountDue = it.amountDue,
                                            dueDate = it.dueDate,
                                            loanType = it.loanType,
                                            loanStatus = it.loanStatus,
                                            loansCount = state.loansBalances.loanCount,
                                            loanIndex = stateLazyRow0.firstVisibleItemIndex + 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Paginator(state.loansBalances.loanBreakDown.size, stateLazyRow0.firstVisibleItemIndex)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {

                    TextInputContainer("Desired Amount","", inputType = InputTypes.NUMBER, callback = { str ->
                        amount = TextFieldValue(str)
                    })

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp)
                ) {
                    ActionButton("Pay Now", onClickContainer = {
                        if (amount.text !== "" && state.loansBalances.loanBreakDown.isNotEmpty()) {
                            onPaySelected(amount.text, state.loansBalances.loanBreakDown[stateLazyRow0.firstVisibleItemIndex])
                        }
                    }, enabled = (amount.text !== "" && state.loansBalances.loanBreakDown.isNotEmpty()))
                }

                Card(
                    onClick = {
                        launchPopUp = !launchPopUp
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.5.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        "View more details",
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = backArrowColor,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light)
                    )
                }

                if (launchPopUp && state.loansBalances.loanBreakDown.isNotEmpty()) {
                    MoreDetails(
                        loanBreakDown = state.loansBalances.loanBreakDown[stateLazyRow0.firstVisibleItemIndex],
                        dismiss = {launchPopUp = !launchPopUp}
                    )
                }
            }
        } else {
            if (!state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(start = 16.dp, end = 16.dp)
                        .padding(it),
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
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 25.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You do not have any unpaid loans",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.bold),
                            textAlign = TextAlign.Center,
                            lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoreDetails(
    loanBreakDown: LoanBreakDown,
    dismiss: () -> Unit
) {
    Popup {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Black.copy(alpha = 0.7f)),
            verticalArrangement = Arrangement.Top
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.99f)
                    .padding(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                ) {

                    Row(modifier = Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Loan Details",
                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.medium),
                            fontSize = 14.sp
                        )
                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = "Cancel  Arrow",
                            tint = backArrowColor,
                            modifier = Modifier.clickable {
                                dismiss()
                            }.absoluteOffset(y = -(20.dp))
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(vertical = 7.dp),
                        text = "Balance: KES ${formatMoney(loanBreakDown.totalBalance)}",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.bold),
                    )

                    val loanDetailsMap: MutableMap<String, String?> = mutableMapOf(
                        "Loan Type" to loanBreakDown.loanType,
                        "Loan Status" to loanBreakDown.loanStatus,
                        "Amount Due" to formatMoney(loanBreakDown.amountDue),
                        "Requested Amount" to formatMoney(loanBreakDown.requestedAmount),
                        "Interest Rate" to formatMoney(loanBreakDown.interestRate),
                        "Interest Amount" to formatMoney(loanBreakDown.interestAmount),
                        "Loan Period" to loanBreakDown.loanPeriod,
                        "Due Date" to loanBreakDown.dueDate,
                        "Balance BF" to formatMoney(loanBreakDown.balanceBF),
                        "Repayment Amount" to formatMoney(loanBreakDown.repaymentAmount),
                        "Disbursed Amount" to formatMoney(loanBreakDown.disbursedAmount)
                    )

                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f).background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                        contentPadding = PaddingValues(vertical = 5.dp, horizontal = 16.dp)
                    ) {
                        loanDetailsMap.map { detail ->
                            item {
                                LoanDetailsRow(detail.key, detail.value)
                            }
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth().padding(top = 14.dp, bottom = 7.dp)
                    ) {
                        Text(
                            text = "Loan schedule",
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold)
                        )

                    }

                    LazyColumn(
                        modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
                    ) {
                        loanBreakDown.loanSchedule.map { schedule ->
                            val replacementPattern = Regex("\\s")
                            val replacedWhiteSpace = schedule.scheduleDate.replace(regex = replacementPattern, replacement = "T")
                            val localDateTime = LocalDateTime.parse(replacedWhiteSpace)

                            item {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Column {
                                        Text(
                                            modifier = Modifier.align(Alignment.Start),
                                            text = "${localDateTime.dayOfMonth} ${localDateTime.month}",
                                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular)
                                        )

                                        Text(
                                            modifier = Modifier.align(Alignment.Start),
                                            text = "${localDateTime.time}",
                                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular)
                                        )
                                    }

                                    Column {
                                        Text(
                                            modifier = Modifier.align(Alignment.End),
                                            text = formatMoney(schedule.balance),
                                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold)
                                        )

                                        Text(
                                            modifier = Modifier.align(Alignment.End),
                                            text = schedule.status,
                                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    OutlinedButton(
                        onClick = {
                            dismiss()
                        }
                    ) {
                        Text(
                            text = "Dismiss",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}