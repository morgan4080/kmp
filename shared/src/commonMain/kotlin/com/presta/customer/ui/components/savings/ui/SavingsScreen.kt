package com.presta.customer.ui.components.savings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import com.presta.customer.ui.components.savings.SavingsComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.CurrentSavingsContainer
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.labelTextColor
import com.presta.customer.ui.composables.TransactionHistoryContainer
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun SavingsScreen(
    component: SavingsComponent
) {

    val authState by component.authState.collectAsState()

    val savingsState by component.savingsState.collectAsState()

    SavingsContent(
        authState = authState,
        state = savingsState,
        onAddSavingsSelected = component::onAddSavingsSelected,
        onBack = component::onBack,
        loadEssentials = component::loadEssentials,
    )
}