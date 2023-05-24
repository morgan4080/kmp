package com.presta.customer.ui.components.sign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.ui.helpers.LocalSafeArea

@Composable
fun SignScreen(component: SignComponent) {
    val model by component.model.subscribeAsState()

    LazyColumn (
        modifier = Modifier.fillMaxHeight(1f).padding(LocalSafeArea.current),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = model.items) { item ->
            Row {
                Text(
                    text = item,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}