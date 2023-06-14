package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainModalDrawerSheet(
    items: List<Pair<String, ImageVector>>,
    authState: AuthStore.State,
    selectedItem: Pair<String, ImageVector>,
    onItemsClick: (Pair<String, ImageVector>) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContentColor = MaterialTheme.colorScheme.onBackground,
        windowInsets = WindowInsets(
            top = LocalSafeArea.current.calculateTopPadding() + 20.dp,
            bottom = LocalSafeArea.current.calculateBottomPadding()
        ),
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            modifier = Modifier.background(
                brush = ShimmerBrush(
                    targetValue = 1300f,
                    showShimmer = authState.authUserResponse?.companyName == null
                ),
                shape = RoundedCornerShape(12.dp)
            ).defaultMinSize(200.dp).padding(NavigationDrawerItemDefaults.ItemPadding),
            text = if (authState.authUserResponse !== null) authState.authUserResponse.companyName else "",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.second, contentDescription = null) },
                label = { Text(item.first) },
                selected = item == selectedItem,
                onClick = {
                    onItemsClick(item)
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(.6f),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}