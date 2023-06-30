package com.presta.customer.ui.components.customerBanks.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.customerBanks.CustomerBanksComponent
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

data class TabData(
    val label: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CustomerBanksScreen(
    component: CustomerBanksComponent
) {
    val modeOfDisbursementState by component.modeOfDisbursementState.collectAsState()
    val pagerState = rememberPagerState()
    val tabs = listOf(
        TabData(
            "Add Bank",
            Icons.Filled.Add
        )
    )

    Scaffold (
        topBar = {
            NavigateBackTopBar("Bank Disbursement", onClickContainer = {
                component.onBackNavSelected()
            })
        }
    ) {
        Column (
            modifier = Modifier.padding(it)
        ) {
            ScrollableTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                edgePadding = 16.dp,
                indicator = {},
                divider = {}
            ) {
                tabs.forEach {
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFE5F1F5))
                            .size(30.dp),
                        onClick = {
                            // navigate()
                        },
                        content = {
                            Icon(
                                imageVector = it.icon,
                                modifier = Modifier.size(25.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = it.label,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )
                        }
                    )
                }
            }
        }
    }
}