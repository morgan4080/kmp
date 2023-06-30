package com.presta.customer.ui.components.banKDisbursement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ProductSelectionCard2
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDisbursementScreen(
    component: BankDisbursementComponent
) {
    var launchPopUp by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    Scaffold(
        modifier = Modifier,
        topBar = {
            NavigateBackTopBar("Disbursement Method", onClickContainer = {
                component.onBackNavSelected()

            })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
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
                component.onConfirmSelected()

            })
        }

        if (launchPopUp) {
            Popup {
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
                                                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                                    .padding(
                                                        top = 10.dp,
                                                        start = 16.dp,
                                                        end = 16.dp
                                                    )
                                            ) {
                                                SelectBankView(
                                                    Index =   1,
                                                    selected = selectedIndex == 1,
                                                    onClick = { index: Int ->
                                                        selectedIndex =
                                                            if (selectedIndex == index) -1 else index
//                                                                if (!enabled) {
//                                                                    enabled = true
//                                                                }
                                                    },
                                                    label = "Kcb"
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
                                    launchPopUp = false
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBankView(
    Index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    label: String,
    description: String? = null,
) {
    ElevatedCard(
        onClick = {
            onClick.invoke(Index)
        },

        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)

    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )

                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 12.sp,
                        )
                    }
                }

                Row {
                    //Created a Custom checkBox
                    Spacer(modifier = Modifier.weight(1f))
                    Card(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .clip(shape = CircleShape),
                        elevation = CardDefaults.cardElevation(0.dp),
                        border = BorderStroke(
                            1.dp,
                            color = if (selected) actionButtonColor else Color.Gray
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(if (selected) actionButtonColor else MaterialTheme.colorScheme.background)
                                .clickable {
                                    //onClickContainer(mode)
                                    onClick.invoke(Index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected)
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check Box",
                                    tint = Color.White,
                                    modifier = Modifier.padding(1.dp)
                                )
                        }
                    }

                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
            }
        }
    }
}