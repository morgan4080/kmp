package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.theme.labelTextColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ShortTermTopUpList(
    component: ShortTermLoansComponent,
    state: ShortTermLoansStore.State) {
    var checkedState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            Text(
                text = "Select Loan to Top Up",
                modifier = Modifier
                    .padding(top = 25.dp),
                fontSize = 14.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                state.prestaShortTermTopUpList?.loans?.map{topUpList->

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = checkedState,
                                    onCheckedChange = { checkedState = it },
                                    modifier = Modifier.clip(shape = CircleShape)
                                        .background(color = Color(0xFFE5F1F5))
                                        .height(20.dp)
                                        .width(20.dp),
                                    colors = CheckboxDefaults.colors(uncheckedColor = Color(0xFFE5F1F5))
                                )

                                Column(modifier = Modifier
                                    .padding(start = 10.dp)) {
                                    Text(text = if(topUpList.name!=null)  topUpList.name.toString() else "" ,
                                        fontSize = 12.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "min. "+topUpList.minAmount+"-max. "+topUpList.maxAmount,
                                        fontSize = 10.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                    )
                                }
                            }
                            Column() {

                                Text(text ="Bal.Kes"+ topUpList.loanBalance.toString(),
                                    fontSize = 12.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = topUpList.daysAvailable.toString(),
                                    fontSize = 10.sp,
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
                .padding(bottom = 26.dp)
        ) {

            ActionButton("Proceed", onClickContainer = {
                //Navigate  to topUp Screen
                component.onConfirmSelected("topUp")

            })
        }
        //Action Button appears above the action Button
        Spacer(
            modifier = Modifier
                .padding(bottom = 80.dp)
        )
    }
}