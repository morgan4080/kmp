package com.presta.customer.ui.components.profile.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.presta.customer.ui.components.profile.ProfileComponent


data class QuickLinks(val labelTop: String,val labelBottom: String, val icon: ImageVector)
data class Transactions(val label: String, val credit: Boolean, val code: String, val amount: String, val date: String, val icon: ImageVector)

@Composable
fun ProfileScreen(
    component: ProfileComponent,
    innerPadding: PaddingValues
) {
    val authState by component.authState.collectAsState()
    val profileState by component.profileState.collectAsState()

    ProfileContent(
        authState= authState,
        state = profileState,
        onEvent = component::onEvent,
        onAuthEvent = component::onAuthEvent,
        innerPadding = innerPadding
    )
}


