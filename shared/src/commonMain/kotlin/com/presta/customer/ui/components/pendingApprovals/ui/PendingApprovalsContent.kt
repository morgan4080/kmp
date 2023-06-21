package com.presta.customer.ui.components.pendingApprovals.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingApprovalsContent(
    authState: AuthStore.State,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NavigateBackTopBar("Approvals", onClickContainer = {
                onBack()
            })
        }
    ) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp)
            .padding(paddingValues)
        ) {
            Text(
                modifier = Modifier,
                text = "Select Loan Type",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )
        }
    }
}