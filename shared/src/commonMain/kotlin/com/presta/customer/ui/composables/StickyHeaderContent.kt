package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StickyHeaderContent(initial: Char) {
    LazyRow (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        contentPadding = PaddingValues(vertical = 5.dp, horizontal = 16.dp)
    ) {
        item {
            Text(
                text = initial.toString(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}