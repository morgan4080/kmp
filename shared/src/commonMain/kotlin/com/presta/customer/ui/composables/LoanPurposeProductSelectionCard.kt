package com.presta.customer.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanPurposeProductSelectionCard(
    label: String,
    myLazyColumn: @Composable () -> Unit,
    index: Int,
    onClick: (Int) -> Unit,
    expandContent:Boolean
) {
    var showExpanded by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 12.dp))
            //.padding(bottom = 10.dp)
            .absolutePadding(left = 2.dp, right = 2.dp, top = 5.dp, bottom = 5.dp),
        colors = CardDefaults.elevatedCardColors(containerColor =MaterialTheme.colorScheme.inverseOnSurface ),
        onClick = {
            onClick.invoke(index)
        }
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                    top = 20.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp,
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.outline,
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
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                modifier = if (showExpanded) Modifier.size(25.dp)
                                    .rotate(90F) else Modifier.size(25.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
                AnimatedVisibility(expandContent) {
                    myLazyColumn()
                }
            }
        }
    }
}
