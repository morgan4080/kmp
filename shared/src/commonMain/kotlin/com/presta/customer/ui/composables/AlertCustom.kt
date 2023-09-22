package com.presta.customer.ui.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertCustom() {
    var showDialog by remember { mutableStateOf(true) }

    var animate by remember { mutableStateOf(true) }

    LaunchedEffect(showDialog) {
        delay(2000)
        showDialog = true
    }

    val translateY: Float by animateFloatAsState(
        targetValue = if (showDialog) 0f else 2000f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        finishedListener = {
            animate = showDialog
        }
    )

    if (
        animate
    ) {
        AlertDialog(
            modifier = Modifier.graphicsLayer {
                translationY = translateY
            },
            onDismissRequest = {
                showDialog = false
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 12.dp))
                        .absolutePadding(left = 2.dp, right = 2.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        Column(
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
                                    text = "Title",
                                    color = MaterialTheme.colorScheme.outline,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}