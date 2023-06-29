package com.presta.customer.ui.components.loanConfirmation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.DisbursementDetailsContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.theme.actionButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanConfirmationContent(
    component: LoanConfirmationComponent,
    authState: AuthStore.State,
    modeOfDisbursementState: ModeOfDisbursementStore.State,
    state: ShortTermLoansStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues
) {
    var launchPopUp by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NavigateBackTopBar(component.loanName, onClickContainer = {
                component.onBackNavSelected()
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp).padding(it)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            //confirm the specific loan operation
            item {
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Confirm " + component.loanOperation + " Details",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            //Disbursement Details
            item {
                DisbursementDetailsContainer(component, modeOfDisbursementState)
            }
            //action Button

            item {
                Row(modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)) {
                    ActionButton("Confirm", onClickContainer = {
                        launchPopUp = true
                    }, loading = state.isLoading || authState.isLoading)
                }
            }

            item {
                if (
                    state.error !== null || modeOfDisbursementState.error !== null
                ) {
                    Column(modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(16.dp)
                    ) {
                        state.error?.let { txt -> Text(style = MaterialTheme.typography.labelSmall, text = txt) }
                        modeOfDisbursementState.error?.let { txt -> Text(style = MaterialTheme.typography.labelSmall, text = txt) }
                    }
                }
            }
        }
        if (launchPopUp) {
            Popup {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color.Black.copy(alpha = 0.7f)),
                    verticalArrangement = Arrangement.Center
                ) {

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 25.dp,
                                end = 25.dp,
                                top = 26.dp
                            ),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                )
                        ) {
                            Text(
                                text = "Confirm loan request",
                                modifier = Modifier
                                    .padding(
                                        top = 14.dp
                                    )
                            )

                            Text(
                                text = "Press proceed to confirm your loan request",
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .padding(top = 3.dp)
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            ElevatedCard(
                                onClick = {
                                    launchPopUp = false
                                }, modifier = Modifier
                                    .padding(start = 16.dp)
                            ) {

                                Text(
                                    text = "Dismiss",
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .padding(
                                            top = 5.dp,
                                            bottom = 5.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                )
                            }
                            ElevatedCard(
                                onClick = {
                                    launchPopUp = false
                                    component.onConfirmSelected()
                                }, modifier = Modifier
                                    .padding(end = 16.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = actionButtonColor)
                            ) {
                                Text(
                                    text = "Proceed",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .padding(
                                            top = 5.dp,
                                            bottom = 5.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


