package com.presta.customer.ui.components.banKDisbursement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.OptionsSelectionContainer
import com.presta.customer.ui.composables.ProductSelectionCard2
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.labelTextColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BankDisbursementScreen(component: BankDisbursementComponent) {
    var launchPopUp by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
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
                NavigateBackTopBar("Disbursement Method", onClickContainer = {
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
                    text = "Select Disbursement Method",
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 25.dp)
                )

                //Select  banks  from the pop up
                if (launchPopUp) {
                    Popup() {
                        // Composable to select The bank
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
                                    .fillMaxHeight(0.9f)
                                    .padding(
                                        start = 26.dp,
                                        end = 26.dp,
                                        top = 5.dp,
                                        bottom = 90.dp
                                    ),
                                colors = CardDefaults
                                    .elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                ) {

                                    Text(
                                        "Select Bank",
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 17.dp),
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        fontSize = 14.sp,
                                        color = labelTextColor
                                    )
                                    Text(
                                        "Select Options Below",
                                        modifier = Modifier
                                            .padding(start = 16.dp),
                                        fontSize = 10.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                    )
                                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {

                                            LazyColumn(
                                                modifier = Modifier
                                                    .wrapContentHeight()
                                            ) {

                                                items(7) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(color = Color.White)
                                                            .padding(
                                                                top = 10.dp,
                                                                start = 16.dp,
                                                                end = 16.dp
                                                            )
                                                    ) {
                                                        OptionsSelectionContainer(
                                                            "KCB",
                                                            onClickContainer = {


                                                            })
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 20.dp,
                                            bottom = 20.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    OutlinedButton(
                                        border = BorderStroke(width = 1.dp, color = labelTextColor),
                                        onClick = {
                                            launchPopUp = false
                                        },
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .height(30.dp),
                                    ) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                            color = labelTextColor,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )

                                    }
                                    OutlinedButton(
                                        colors = ButtonDefaults.outlinedButtonColors(containerColor = actionButtonColor),
                                        border = BorderStroke(
                                            width = 0.dp,
                                            color = actionButtonColor
                                        ),
                                        onClick = {
                                            launchPopUp = false
                                        },
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .height(30.dp),
                                    ) {

                                        Text(
                                            text = "Proceed",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)

                                            )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ProductSelectionCard2("Select Bank", onClickContainer = {
                        //Business  Logic
                        //banK details pop up card
                        launchPopUp = true

                    })

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    ProductSelectionCard2("Account Number", onClickContainer = {
                        //Business  Logic
                    })
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 60.dp)
                )
                ActionButton("Proceed", onClickContainer = {
                    //Navigate to process The  Transaction and show  success or  failed Transaction
                    component.onConfirmSelected()

                })

            }

        }

    }

}