package com.presta.customer.ui.composables

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.network.profile.model.PrestaBalancesResponse
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeCardListItem(name: String, onClick: (String) -> Unit,savingsBalance:String) {
    var showExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .border(BorderStroke(1.dp,Color.White), shape = RoundedCornerShape(size = 12.dp))
            .clip(RoundedCornerShape(size = 12.dp))
            .absolutePadding(left = 2.dp, right = 2.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Box (modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
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
                        text = "Total Savings",
                        color= MaterialTheme.colorScheme.outline, // #002C56
                        fontSize = 16.sp
                    )

                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
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
                        text = savingsBalance,
                        color= MaterialTheme.colorScheme.onBackground,
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
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
                        text = "KES 5,000 - 5 Days Ago",
                        color= MaterialTheme.colorScheme.onBackground, // #002C56
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(showExpanded) {

                }
            }
        }
    }
}