package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun HomeCardListItem(
    name: String,
    onClick: (String) -> Unit,
    balance: Double?,
    lastSavingsAmount: String? = null,
    lastSavingsDate: String? = null
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
                        fontSize = 16.sp
                    )

                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFE5F1F5))
                            .size(30.dp),
                        onClick = {
                            showExpanded = !showExpanded
                            onClick(name)
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
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
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
                                    if (name == "Shares Count") " Shares" else " KES"
                        } else "",
                        color= MaterialTheme.colorScheme.onBackground,
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
                Row (modifier = Modifier
                    .padding(top = 11.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Last Savings",
                        color= MaterialTheme.colorScheme.outline, // #002C56
                        fontSize = 16.sp
                    )
                }
                Row (modifier = Modifier
                    .padding(top = 1.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.background(
                            brush = ShimmerBrush (
                                targetValue = 1300f,
                                showShimmer = lastSavingsAmount == null || lastSavingsDate == null
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ).defaultMinSize(150.dp),
                        text = if (lastSavingsAmount !== null && lastSavingsDate !== null)
                                "KES $lastSavingsAmount - `$lastSavingsDate Days Ago"
                        else "",
                        color= MaterialTheme.colorScheme.onBackground, // #002C56
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(showExpanded) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Savings Balances",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Row (
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(end = 10.dp),
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
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "Current Savings",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )

                            Text(
                                text = "KES 5,000.00",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}