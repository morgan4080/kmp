package com.presta.customer.ui.components.payLoan

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.LoanStatusContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.composables.disbursementDetailsRow
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.labelTextColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PayLoanScreen(component: PayLoanComponent) {
    val stateLazyRow0 = rememberLazyListState()
    var amount by remember {
        mutableStateOf(TextFieldValue())
    }
    var launchPopUp by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .padding(LocalSafeArea.current)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(bottom = 80.dp)
            )
        }
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                NavigateBackTopBar("Pay Loan", onClickContainer = {

                })
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {

                Text(
                    modifier = Modifier,
                    text = "My Loans",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                )

                LazyRow(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    state = stateLazyRow0,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow0),
                    content = {
                        items(3) {
                            Box(
                                modifier = Modifier.fillParentMaxWidth()
                            ) {
                                LoanStatusContainer()
                            }
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Paginator(3, stateLazyRow0.firstVisibleItemIndex)

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                ) {

                    TextInputContainer(
                        "Desired Amount",
                        "",
                        inputType = InputTypes.NUMBER,
                        callback = {
                            amount = TextFieldValue(it)
                        })

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp)
                ) {
                    ActionButton("Pay Now", onClickContainer = {
                        component.onPaySelected()

                    })

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
                        "View More Details",
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                //View More details popup
                //Added overlay  to the po up screen
                if (launchPopUp) {
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
                                    .fillMaxHeight(0.8f)
                                    .padding(
                                        start = 25.dp,
                                        end = 25.dp,
                                        top = 26.dp
                                    ),
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp
                                        )
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .padding(top = 14.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Loan Details",
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                            fontSize = 14.sp
                                        )
                                        Icon(
                                            Icons.Filled.Cancel,
                                            contentDescription = "Cancel  Arrow",
                                            tint = backArrowColor,
                                            modifier = Modifier.clickable {
                                                launchPopUp = false

                                            }
                                        )
                                    }

                                    Text(
                                        text = "Loan Balance",
                                        modifier = Modifier
                                            .padding(top = 7.dp),
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                    ) {

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Kes 30,000",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                            )
                                            Text(
                                                text = "(PerForming)",
                                                fontSize = 10.sp,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                                            )

                                        }

                                    }

                                    //data Rows

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.4f)
                                    ) {
                                        items(8) {
                                            disbursementDetailsRow("Requested Amount", "Kes 30,000")

                                        }

                                    }

                                    //Loan schedule

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Loan schedule",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )

                                    }

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.7f)
                                    ) {
                                        items(5) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(top = 7.dp)
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                LoanScheduleContainer()
                                            }

                                        }

                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 21.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    OutlinedButton(modifier = Modifier.height(30.dp),
                                        onClick = {
                                            launchPopUp = false

                                        }) {
                                        Text(
                                            text = "Dismiss",
                                            fontSize = 9.sp,
                                            color = labelTextColor
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
fun LoanScheduleContainer() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = "12 May 2023",
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                color = labelTextColor
            )
            Text(
                text = "12:20pm",
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
            )

        }

        Column() {
            Text(
                text = "kes 8000.00",
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
            )

            Text(
                text = "Paid",
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
            )

        }

    }

}



























