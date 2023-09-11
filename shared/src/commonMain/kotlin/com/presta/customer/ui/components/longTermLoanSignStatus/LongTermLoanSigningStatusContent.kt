package com.presta.customer.ui.components.longTermLoanSignStatus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanSigningStatusContent(
    component: LongtermLoanSigningStatusComponent
) {
    Scaffold(
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 60.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        AnimatedVisibility(
                            visible = true
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(150.dp)
                                    .background(MaterialTheme.colorScheme.background)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(150.dp),
                                    tint = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            }
                        }
                        AnimatedVisibility(

                            visible = false
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(150.dp),
                                tint = Color.Red
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 80.dp, end = 80.dp, top = 50.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "LOAN NUMBER " + component.loanNumber,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 15.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                textAlign = TextAlign.Center,
                                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
                            )
                            Text(
                                text = "AMOUNT " +"KES "+ formatMoney(component.amount) ,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 15.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                textAlign = TextAlign.Center,
                                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = "SIGNED SUCCESSFULLY!",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 15.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                textAlign = TextAlign.Center,
                                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(size = 12.dp),
                            onClick = {
                                // Todo---Go back to profile
                                component.navigateToProfile()
                            }
                        ) {
                            Text(
                                text = "Done",
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                            )
                        }
                    }
                }
            }
        }
    }
}