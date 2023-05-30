package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Paginator(count: Int, currentIndex: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            items(count) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .width(if (it == currentIndex) 16.dp else 37.dp)
                    .height(10.dp)
                    .clip(if (it == currentIndex) RoundedCornerShape(10.dp) else CircleShape)
                    .background(
                        if (it == currentIndex)
                            MaterialTheme.colorScheme.primary
                        else
                            when(MaterialTheme.colorScheme.background == Color(0xFFF7F7F7)) {
                                true -> Color(0xFFD0D0D0)
                                false -> MaterialTheme.colorScheme.inverseOnSurface
                            }
                    )
                )
            }
        }
    }
}