package com.presta.customer.ui.components.longTermLoanApplicationStatus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanApplicationStatusContent(
    //amount: Double,
   // retryTransaction: () -> Unit,
    //navigateBack: () -> Unit
) {
    var showprocessing by remember { mutableStateOf(true) }


    Scaffold(
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Row(modifier = Modifier.fillMaxWidth()) {

            }

            Column(modifier = Modifier.fillMaxWidth().padding(top = 50.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Phone Number ${"334454"}",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "KSH ${formatMoney(300.0)}")
                }

            }

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
                                if (showprocessing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.then(
                                            Modifier.size(60.dp)
                                                .alpha(if (showprocessing) 1f else 0.0f)
                                        ),
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(150.dp),
                                        tint = Color.Green
                                    )
                                }
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
                        if (showprocessing) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "TRANSACTION SUCCESSFUL!",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 22.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                    textAlign = TextAlign.Center,
                                    lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "You paid KES ${formatMoney(3000.0)} to ${OrganisationModel.organisation.tenant_name}",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Your transaction ${if (showprocessing) "status is ${"data"}" else "failed"}!",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 20.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                    textAlign = TextAlign.Center,
                                    lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
                                )
                            }
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
                        if (showprocessing) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(size = 12.dp),
                                onClick = {
                                    //navigateBack()
                                }
                            ) {
                                Text(
                                    text = "Done",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                )
                            }
                        } else {
                            Button(
                                modifier = Modifier.width(150.dp)
                                    .border(
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline
                                        ),
                                        shape = RoundedCornerShape(size = 12.dp)
                                    ),
                                shape = RoundedCornerShape(size = 12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    //navigateBack()
                                }
                            ) {
                                Text(
                                    text = "Close",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                )
                            }
                            Button(
                                modifier = Modifier.width(150.dp),
                                shape = RoundedCornerShape(size = 12.dp),
                                onClick = {
                                    //retryTransaction()
                                }
                            ) {
                                Text(
                                    text = "Retry",
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
}