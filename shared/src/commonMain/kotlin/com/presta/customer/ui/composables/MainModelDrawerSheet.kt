package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainModalDrawerSheet(
    items: List<Pair<String, ImageVector>>,
    authState: AuthStore.State,
    logout: () -> Unit,
    selectedItem: Pair<String, ImageVector>,
    onItemsClick: (Pair<String, ImageVector>) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
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

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    )
            ) {
                Row(modifier = Modifier
                    .padding(bottom = 15.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "User Details",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                        fontSize = 14.sp
                    )
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Full Name",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                    )
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                    if (authState.authUserResponse !== null) {
                        println(authState.authUserResponse)
                        Text(
                            text = "${authState.authUserResponse.firstName} ${authState.authUserResponse.lastName}",
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.second, contentDescription = null) },
                label = { Text(item.first) },
                selected = item == selectedItem,
                onClick = {
                    if (item.first == "Log Out") {
                        logout()
                    }
                    onItemsClick(item)
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(.8f),
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