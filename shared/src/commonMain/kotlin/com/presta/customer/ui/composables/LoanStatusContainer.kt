package com.presta.customer.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.theme.labelTextColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun LoanStatusContainer() {
    ElevatedCard(modifier = Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(size = 12.dp))
        .absolutePadding(left = 2.dp, right = 2.dp, top = 10.dp, bottom = 5.dp)) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
                )
                    .fillMaxWidth()

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {

                        Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = "Emergency Loan",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )
                            Text(
                                text = "1of 3",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                color = labelTextColor,
                            )

                        }

                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Kes, 50,000",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 20.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 11.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column() {

                            Text(
                                text = "Amount Due ",
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )

                            Text(
                                text = "Kes 30,000",
                                color = MaterialTheme.colorScheme.error, // #002C56
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 29.dp)) {

                            Text(
                                text = "Due ",
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )

                            Text(
                                text = "12 May, 2023",
                                color = MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                        }


                        Column(modifier = Modifier.padding(start = 29.dp)) {

                            Text(
                                text = "Status ",
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )

                            Text(
                                text = "Performing",
                                color = MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                        }
                    }
                }
            }
        }
    }
}


