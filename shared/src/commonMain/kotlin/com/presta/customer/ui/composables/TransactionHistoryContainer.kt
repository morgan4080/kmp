package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun TransactionHistoryContainer() {

    data class Transactions(
        val label: String,
        val credit: Boolean,
        val code: String,
        val amount: String,
        val date: String,
        val icon: ImageVector
    )

    Row(modifier = Modifier.fillMaxWidth()) {


        val transactionList = mutableListOf(
            Transactions(
                "Savings Deposit",
                false,
                "TRGHJK123LL",
                "15,000",
                "12 May 2020, 12:23 PM",
                Icons.Filled.OpenInNew
            )
        )


        transactionList.forEach { transaction ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Column(
                        modifier = Modifier.padding(end = 12.dp),
                    ) {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (transaction.credit)
                                        MaterialTheme.colorScheme.secondaryContainer
                                    else
                                        Color(0xFF9deeff)
                                )
                                .size(30.dp),
                            onClick = {

                            },
                            content = {
                                Icon(
                                    imageVector = transaction.icon,
                                    modifier = if (transaction.credit) Modifier.size(15.dp)
                                        .rotate(180F) else Modifier.size(15.dp),
                                    contentDescription = null,
                                    tint = if (transaction.credit)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        Color(0XFF62a5b3)
                                )
                            }
                        )
                    }
                    Column {
                        Text(
                            text = transaction.label,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = transaction.code,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    }
                }

                Column {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = transaction.amount,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = transaction.date,
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )
                }
            }
        }


    }

}