package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.ui.SelectGuarantorsView
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

data class Disbursement_modes(
    val name: String,
    val value: String,
    val selected: Boolean
)

data class Repayment_modes(
    val name: String,
    val value: String,
    val selected: Boolean
)

@Composable
fun UserInformation(
    signProfileState: SignHomeStore.State
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var launchDisbursementModePopUp by remember { mutableStateOf(false) }
    var launchPaymentModePopUp by remember { mutableStateOf(false) }
    var lastName by remember { mutableStateOf(TextFieldValue()) }
    var disbursementMode  by remember { mutableStateOf("") }
    var repaymentMode  by remember { mutableStateOf("") }



    Column(modifier = Modifier.padding(top = 20.dp)) {
        LazyColumn() {
            if (signProfileState.prestaTenantByPhoneNumber?.firstName == null) {
                items(6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
                            .background(color = MaterialTheme.colorScheme.background),
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
            } else {
                lastName = TextFieldValue(signProfileState.prestaTenantByPhoneNumber.firstName)
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
                                userInput = lastName.text,
                                label = "first Name"
                            ) {
                                val inputValue: String = TextFieldValue(it).text
                                lastName = TextFieldValue(it)
//                                if (inputValue != "") {
//                                    if (TextFieldValue(it).text !== "") {
//                                        lastName = TextFieldValue(it)
//
//                                    } else {
//
//                                    }
//                                }
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.lastName,
                                label = "last name"
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.idNumber,
                                label = "ID number"
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.phoneNumber,
                                label = "Phone Number"
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LiveTextContainer(
                                userInput = signProfileState.prestaTenantByPhoneNumber.email,
                                label = "email"
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            ModeSelectionCard(label = "Disbursement Mode", onClickContainer = {
                                //launch POP Up
                                launchDisbursementModePopUp = true


                            })
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            ModeSelectionCard(label = "Repayment Mode", onClickContainer = {
                                //launch POP Up
                                launchPaymentModePopUp = true
                            })
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp)
                        ) {
                            ActionButton(
                                label = "Submit  Loan Request", onClickContainer = {
                                    //Submit  the loan Request and pass the required data
                                    //Execute  Submit Loan Request Api- pass   the required data
                                    //submit The loan request


                                    println("Name is :::::::")
                                    println(lastName.text)
                                },
                                enabled = true
                            )
                        }
                    }
                }
            }
            //popup Disbursement mode
            item {
                if (launchDisbursementModePopUp) {

                    val disburementModeListing = listOf(
                        Disbursement_modes("Cheques", "Cheques", selected = true),
                        Disbursement_modes("My Account", "My Account", selected = true),
                        Disbursement_modes("EFT", "EFT", selected = true)
                    )
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

                                                disburementModeListing.mapIndexed { indexed, disbursementModes ->
                                                    item {
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
                                                                Index = indexed,
                                                                selected = selectedIndex == indexed,
                                                                onClick = { index: Int ->
                                                                    selectedIndex = if (selectedIndex == index) -1 else index
                                                                    if (selectedIndex > -1) {
                                                                        disbursementMode = disburementModeListing[selectedIndex].name
                                                                    }
                                                                },
                                                                label = disbursementModes.name
                                                            )
                                                        }
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
                    val repaymentmentModeListing = listOf(
                        Repayment_modes("Check Off ", "Check Off", selected = true),
                        Repayment_modes("Paybill", "Paybill", selected = true),
                        Repayment_modes("Standing Order", "Standing Order", selected = true)
                    )
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

                                                repaymentmentModeListing.mapIndexed { indexedPay, repamentModes ->
                                                    item {
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
                                                                Index = indexedPay,
                                                                selected = selectedIndex == indexedPay,
                                                                onClick = { index: Int ->
                                                                    selectedIndex = if (selectedIndex == index) -1 else index
                                                                    if (selectedIndex > -1) {
                                                                        repaymentMode= repaymentmentModeListing[selectedIndex].name
                                                                    }
                                                                },
                                                                label = repamentModes.name
                                                            )
                                                        }
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