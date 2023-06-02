package com.presta.customer.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun LoanLimitContainer(){
    ElevatedCard(modifier = Modifier
        .padding(top = 10.dp)
        .border(BorderStroke(0.5.dp, Color.White), shape = RoundedCornerShape(size = 12.dp))) {
        Box (modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column (
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
                )
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Loan  Limit ",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 16.sp
                    )
                }
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Min.Kes 10,000-Max 50,000",
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)

                    )
                }
                Row (modifier = Modifier
                    .padding(top = 11.dp)
                    .fillMaxWidth()
                ) {
                    Column(){

                        Text(
                            text = "Interest",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light), // #002C56
                            fontSize = 10.sp
                        )

                        Text(
                            text = "12%",
                            color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        )

                    }

                    Spacer(modifier = Modifier.padding(start = 42.dp))



                    Column(){

                        Text(
                            text = "Max loan  Period",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light), // #002C56
                            fontSize = 10.sp
                        )

                        Text(
                            text = "12 Months",
                            color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )

                    }
                }
            }
        }
    }
}
