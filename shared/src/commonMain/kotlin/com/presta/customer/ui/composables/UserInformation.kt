package com.presta.customer.ui.composables

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.ui.SelectGuarantorsView
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import androidx.compose.ui.window.Popup

@Composable
fun UserInformation() {
    var selectedIndex by remember { mutableStateOf(-1) }
    var launchDisbursementModePopUp by remember { mutableStateOf(false) }
    var launchPaymentModePopUp by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 20.dp)) {
        var lastName by remember { mutableStateOf(TextFieldValue()) }
        LazyColumn(){
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        LiveTextContainer(
                            userInput = "Murungi",
                            label = "first Name"
                        ) {
                            val inputValue: String = TextFieldValue(it).text
                            if (inputValue != "") {
                                if (TextFieldValue(it).text !== "") {
                                    lastName = TextFieldValue(it)

                                } else {

                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "Mutugi",
                            label = "last name"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "200",
                            label = "ID number"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "0796387377",
                            label = "Phone Number"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "morganmurungi@live.com",
                            label = "email"
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        ModeSelectionCard(label = "Disbursement Mode", onClickContainer = {
                            //launch POP Up
                            launchDisbursementModePopUp=true


                        })
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        ModeSelectionCard(label = "Repayment Mode", onClickContainer = {
                            //launch POP Up
                            launchPaymentModePopUp=true
                        })
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        ActionButton(
                            label = "Submit  Loan Request", onClickContainer = {
                                println("Name is :::::::")
                                println(lastName.text)
                            },
                            enabled = true
                        )
                    }
                }
            }
            //popup Disbursement mode
            item {
                if (launchDisbursementModePopUp) {
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
                                    .fillMaxHeight(0.7f)
                                    .padding(
                                        start = 26.dp,
                                        end = 26.dp,
                                        top = 40.dp,
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
                                        "SELECT DISBURSEMENT MODE",
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 17.dp),
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        fontSize = 14.sp,
                                    )
                                    Text(
                                        "Select Options Below ",
                                        modifier = Modifier
                                            .padding(start = 16.dp),
                                        fontSize = 10.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                    )
                                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            LazyColumn(
                                                modifier = Modifier
                                                    .wrapContentHeight()
                                            ) {
                                                items(3) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                                            .padding(
                                                                top = 10.dp,
                                                                start = 16.dp,
                                                                end = 16.dp
                                                            )
                                                    ) {
                                                        SelectGuarantorsView(
                                                            Index = 0,
                                                            selected = selectedIndex == 0,
                                                            onClick = { index: Int ->
                                                                selectedIndex =
                                                                    if (selectedIndex == index) -1 else index

                                                                // selectedBank = bank
                                                            },
                                                            label = "Cheque"
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
                                        .padding(
                                            top = 20.dp,
                                            bottom = 20.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    OutlinedButton(
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        onClick = {
                                            launchDisbursementModePopUp = false
                                        },
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .height(30.dp),
                                    ) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
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
                                            launchDisbursementModePopUp = false
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
            }
            item {
                //popup Repayment mode
                if (launchPaymentModePopUp) {
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
                                    .fillMaxHeight(0.7f)
                                    .padding(
                                        start = 26.dp,
                                        end = 26.dp,
                                        top = 40.dp,
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
                                        "SET PAYMENT MODE",
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 17.dp),
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        fontSize = 14.sp,
                                    )
                                    Text(
                                        "Select Options Below",
                                        modifier = Modifier
                                            .padding(start = 16.dp),
                                        fontSize = 10.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                    )
                                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            LazyColumn(
                                                modifier = Modifier
                                                    .wrapContentHeight()
                                            ) {
                                                items(3) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                                            .padding(
                                                                top = 10.dp,
                                                                start = 16.dp,
                                                                end = 16.dp
                                                            )
                                                    ) {
                                                        SelectGuarantorsView(
                                                            Index = 0,
                                                            selected = selectedIndex == 0,
                                                            onClick = { index: Int ->
                                                                selectedIndex =
                                                                    if (selectedIndex == index) -1 else index

                                                                // selectedBank = bank
                                                            },
                                                            label = "Checkoff"
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
                                        .padding(
                                            top = 20.dp,
                                            bottom = 20.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    OutlinedButton(
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        onClick = {
                                            launchPaymentModePopUp = false
                                        },
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .height(30.dp),
                                    ) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
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
                                            launchPaymentModePopUp = false
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
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    }
}