package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.profile.model.LoanBreakDown
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun HomeCardListItem(
    name: String,
    balance: Double?,
    savingsBalance: Double?,
    sharesBalance: Double?,
    lastAmount: Double? = null,
    lastDate: String? = null,
    loanStatus: String? = null,
    totalLoans: Int? = null,
    goToSavings: () -> Unit = {},
    goToPayLoans: () -> Unit = {},
    loanBreakDown: List<LoanBreakDown>? = null
) {
    var showExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 12.dp))
            .absolutePadding(left = 2.dp, right = 2.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
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
                        text = name,
                        color= MaterialTheme.colorScheme.outline,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFE5F1F5))
                            .size(30.dp),
                        onClick = {
                            showExpanded = !showExpanded
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                modifier = if (showExpanded) Modifier.size(25.dp).rotate(180F) else Modifier.size(25.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.background(
                            brush = ShimmerBrush(
                                targetValue = 1300f,
                                showShimmer = balance == null
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ).defaultMinSize(150.dp),
                        text = if (balance !== null) {
                            balance.toString() +
                                    " KES"
                        } else "",
                        color= MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (name == "Total Savings Amount") "Last Savings" else "Status",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                    if (name == "Total Loan Balance") {
                        Text(
                            text = "Loans",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (name == "Total Savings Amount") {
                        Text(
                            modifier = Modifier.background(
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = lastAmount == null || lastDate == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(150.dp),
                            text = if (lastAmount !== null && lastDate !== null)
                                "KES $lastAmount - $lastDate"
                            else "",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )
                    }



                    if (name == "Total Loan Balance") {
                        Text(
                            modifier = Modifier.background(
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = loanStatus == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(150.dp),
                            text = if (loanStatus !== null) loanStatus else "",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )

                        Text(
                            modifier = Modifier.background(
                                brush = ShimmerBrush (
                                    targetValue = 1300f,
                                    showShimmer = totalLoans == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(150.dp),
                            text = if (totalLoans !== null)
                                       "$totalLoans"
                                   else
                                       "",
                            color= MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                            textAlign = TextAlign.End
                        )
                    }
                }

                AnimatedVisibility(showExpanded) {
                    if (name == "Total Loan Balance") {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text (
                                    text = "Loan Balances",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 15.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )

                                Row (
                                    modifier = Modifier.clickable {
                                        goToPayLoans()
                                    },
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text (
                                        modifier = Modifier.padding(end = 5.dp),
                                        text = "See all",
                                        textAlign = TextAlign.Center,
                                        color = backArrowColor,
                                        fontSize = 12.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        style = MaterialTheme.typography.headlineSmall
                                    )

                                    Icon (
                                        Icons.Filled.ArrowForward,
                                        contentDescription = "Forward Arrow",
                                        tint = backArrowColor,
                                        modifier = Modifier.size(20.dp)
                                    )

                                }
                            }

                            loanBreakDown?.map { breakdown ->
                                Row (
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text (
                                        text = breakdown.loanType,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                        textAlign = TextAlign.End
                                    )

                                    Text (
                                        text = breakdown.totalBalance.toString(),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text (
                                    text = "Savings Balances",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 15.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )

                                Row (
                                    modifier = Modifier.clickable {
                                        goToSavings()
                                    },
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text (
                                        modifier = Modifier.padding(end = 5.dp),
                                        text = "See all",
                                        textAlign = TextAlign.Center,
                                        color = backArrowColor,
                                        fontSize = 12.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        style = MaterialTheme.typography.headlineSmall
                                    )

                                    Icon (
                                        Icons.Filled.ArrowForward,
                                        contentDescription = "Forward Arrow",
                                        tint = backArrowColor,
                                        modifier = Modifier.size(20.dp)
                                    )

                                }
                            }

                            Row (
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text (
                                    text = "Current Savings",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                    textAlign = TextAlign.End
                                )

                                Text (
                                    modifier = Modifier.background(
                                        brush = ShimmerBrush(
                                            targetValue = 1300f,
                                            showShimmer = savingsBalance == null
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ).defaultMinSize(150.dp),
                                    text = if (savingsBalance !== null) savingsBalance.toString() else "",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row (
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text (
                                    text = "Current Shares",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                    textAlign = TextAlign.End
                                )

                                Text (
                                    modifier = Modifier.background(
                                        brush = ShimmerBrush(
                                            targetValue = 1300f,
                                            showShimmer = sharesBalance == null
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ).defaultMinSize(150.dp),
                                    text = if (sharesBalance !== null) sharesBalance.toString() else "",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}