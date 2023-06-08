package com.presta.customer.ui.components.topUp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.topUp.LoanTopUpComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.OptionsSelectionContainer
import com.presta.customer.ui.composables.ProductSelectionCard2
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.labelTextColor
//import androidx.compose.ui.window.Popup


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  LoanTopUpContent(
    component: LoanTopUpComponent,
    state: ShortTermLoansStore.State,
){

    var launchPopUp by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var amount by remember {
        mutableStateOf(TextFieldValue())
    }
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {

                NavigateBackTopBar("Topup", onClickContainer = {
                    component.onBackNavSelected()

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
                    text = "Enter top up details",
                    color = MaterialTheme.colorScheme.onBackground
                )
                //show The min  and the  max allowed
              //  state.prestaShortTermTopUpList?.loans?.map{topUpList->}
              //  state.prestaShortTermTopUpList?.loans?.map{topUpList-> topUpList.minAmount}.toString()

                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = "min .15.0- max. 6000.0" ,
                    fontSize = 10.sp
                )

                //select top up Pop up
                //Added overlay  to the po up screen
                if (launchPopUp) {
                    /*Popup() {
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
                                    Text(
                                        text = "Set Loan Term",
                                        modifier = Modifier
                                            .padding(
                                                top = 14.dp
                                            )
                                    )

                                    Text(
                                        text = "Select Options Below",
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(top = 3.dp)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 18.dp)
                                    ) {

                                        OptionsSelectionContainer(
                                            label = "Current term",
                                            onClickContainer = {

                                            })
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        OptionsSelectionContainer(
                                            label = "Emergency Loan at 10%/Month",
                                            onClickContainer = {

                                            })
                                    }
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
                    }*/
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    //Throw Error if amount exceed desired min or max

                    TextInputContainer("Desired Amount", "", inputType = InputTypes.NUMBER){
                        val inputValue: Double? = TextFieldValue(it).text.toDoubleOrNull()
                        if (inputValue != null) {
//                            if ((inputValue >= allowedMinAmount!!.toDouble() && inputValue <= allowedMaxAmount!!.toDouble()) && (TextFieldValue(it).text !== "")
//                            ) {
//                                amount = TextFieldValue(it)
//                                isError = false
//
//                            } else {
//                                isError = true
//                            }
                        }

                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 23.dp)
                ) {
                    ProductSelectionCard2("Loan Term", onClickContainer = {

                        //pop up
                        launchPopUp = true

                    })

                }

                //Text input occurs  Here

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 44.dp)
                ) {

                    ActionButton("Proceed", onClickContainer = {

                        //Proceed To confirm the Details
                        //pass refId to identify Top Up
                        component.onConfirmSelected()
                    }, enabled =true )

                }

            }

        }

    }







}