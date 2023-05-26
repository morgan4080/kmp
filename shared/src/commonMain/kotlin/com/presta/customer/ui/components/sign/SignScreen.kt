package com.presta.customer.ui.components.sign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.ui.helpers.LocalSafeArea

@Composable
fun SignScreen(component: SignComponent) {
    val model by component.model.subscribeAsState()
    Column (
        modifier = Modifier.fillMaxHeight(1f).padding(LocalSafeArea.current),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text(text = "Coming Soon")



    }
}