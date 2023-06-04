package com.presta.customer.ui.components.addSavings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.OptionsSelectionContainer
import com.presta.customer.ui.composables.ProductSelectionCard2
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavingsContent (
    onConfirmSelected: (mode: PaymentTypes, amount: Double) -> Unit,
    onBackNavSelected: () -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    onAddSavingsEvent: (AddSavingsStore.Intent) -> Unit,
    authState: AuthStore.State,
    state: AddSavingsStore.State,
    innerPadding: PaddingValues
) {
    var launchPopUp by remember { mutableStateOf(false) }

    var paymentMode: PaymentTypes? by remember { mutableStateOf(null) }

    val snackBarHostState = remember { SnackbarHostState() }

    var amount by remember {
        mutableStateOf(TextFieldValue())
    }

    LaunchedEffect(
        state.error,
        authState.error
    ) {
        if (state.error !== null) {
            println("::::::state.error")
            println(state.error)
            snackBarHostState.showSnackbar(
                state.error
            )

            onAddSavingsEvent(AddSavingsStore.Intent.UpdateError(null))
        }

        if (authState.error !== null) {
            snackBarHostState.showSnackbar(
                authState.error
            )

            onAuthEvent(AuthStore.Intent.UpdateError(null))
        }
    }

    Scaffold (
        modifier = Modifier
            .padding(innerPadding)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(bottom = 80.dp)
            )
        }
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()) {
                NavigateBackTopBar("Add Savings", onClickContainer = {
                    onBackNavSelected()
                })
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier,
                    text = "Enter Savings Details",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                )

                if (launchPopUp) {

                    Popup {

                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(color = Color.Black.copy(alpha = 0.7f)),
                            verticalArrangement = Arrangement.Center
                        ) {

                            ElevatedCard (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 25.dp,
                                        end = 25.dp,
                                        top = 26.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp)
                                ) {
                                    Text (
                                        text = "Savings Type",
                                        modifier = Modifier
                                            .padding(
                                                top = 14.dp)
                                    )

                                    Text(
                                        text = "Select Options Below",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 18.dp)
                                    ) {
                                        OptionsSelectionContainer(PaymentTypes.SAVINGS, "Current Savings", onClickContainer = {
                                            paymentMode = it
                                        })
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        OptionsSelectionContainer(PaymentTypes.SHARES, "Shares", onClickContainer = {
                                            paymentMode = it
                                        })
                                    }

                                }
                                Row (
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

                                    ElevatedCard (onClick = {
                                        launchPopUp = false
                                        paymentMode = null
                                    }, modifier = Modifier
                                        .padding(start = 16.dp)) {

                                        Text (
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(
                                                top = 5.dp,
                                                bottom = 5.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                            )
                                        )

                                    }
                                    ElevatedCard (
                                        onClick = {
                                            launchPopUp = false
                                        },
                                        modifier = Modifier.padding(end = 16.dp),
                                        colors = CardDefaults.elevatedCardColors(containerColor = actionButtonColor)
                                    ) {
                                        Text (
                                            text = "Proceed",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(
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

                Row (modifier = Modifier.fillMaxWidth().padding(top = 23.dp)){
                    ProductSelectionCard2(if (paymentMode == null) "Select Savings" else paymentMode.toString(), onClickContainer = {
                        launchPopUp = true
                    })

                }

                //Text input occurs  Here

                Row (
                    modifier = Modifier.fillMaxWidth().padding(top = 33.dp)
                ) {
                    TextInputContainer("Desired Amount","", inputType = InputTypes.NUMBER, callback = {
                        amount = TextFieldValue(it)
                    })
                }
                Row (modifier = Modifier.fillMaxWidth().padding(top = 44.dp)) {
                    ActionButton("Confirm", onClickContainer = {
                        if (paymentMode !== null && amount.text !== "") {
                            onConfirmSelected(
                                paymentMode!!,
                                amount.text.toDouble()
                            )
                        }
                    }, enabled = paymentMode !== null && amount.text !== "", loading = state.isLoading)
                }
            }
        }
    }
}