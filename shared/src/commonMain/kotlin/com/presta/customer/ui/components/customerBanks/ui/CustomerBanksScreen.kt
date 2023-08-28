package com.presta.customer.ui.components.customerBanks.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.loanRequest.model.PrestaCustomerBanksResponse
import com.presta.customer.ui.components.customerBanks.CustomerBanksComponent
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

data class TabData(
    val label: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CustomerBanksScreen(
    component: CustomerBanksComponent
) {
    val buttons = listOf(
        TabData(
            "Add Bank",
            Icons.Filled.Add
        )
    )
    val modeOfDisbursementState by component.modeOfDisbursementState.collectAsState()
    val authState by component.authState.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        buttons.size
    }
    var selectedIndex by remember { mutableStateOf(-1) }
    var selectedAccount by remember { mutableStateOf<PrestaCustomerBanksResponse?>(null) }

    Scaffold (
        topBar = {
            NavigateBackTopBar("Disbursement Method", onClickContainer = {
                component.onBackNavSelected()
            })
        }
    ) {
        Column (
            modifier = Modifier.padding(it)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Bank Disbursement",
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                fontSize = 14.sp
            )

            ScrollableTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp),
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                edgePadding = 16.dp,
                indicator = {},
                divider = {}
            ) {
                buttons.forEach { tabData ->
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .width(42.dp)
                                .height(42.dp),
                            onClick = {
                                component.addBankSelected()
                            },
                            content = {
                                Icon(
                                    imageVector = tabData.icon,
                                    modifier = Modifier.size(25.dp),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        )

                        Text(
                            modifier = Modifier.padding(vertical = 7.dp),
                            text = tabData.label,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Bank Accounts",
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                fontSize = 14.sp
            )

            LazyColumn (
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                modeOfDisbursementState.customerBanks.mapIndexed { index, account ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 7.dp
                                )
                        ) {
                            SelectAccountView(
                                Index = index,
                                selected = selectedIndex == index,
                                onClick = { index: Int ->
                                    selectedIndex = if (selectedIndex == index) {
                                        selectedAccount = null
                                        -1
                                    } else {
                                        selectedAccount = account
                                        index
                                    }
                                },
                                deleteAccount = {
                                    authState.cachedMemberData?.let { cachedMemberData ->
                                        component.onModeOfDisbursementEvent(ModeOfDisbursementStore.Intent.DeleteCustomerBank(
                                            cachedMemberData.accessToken,
                                            account.refId
                                        ))
                                    }
                                },
                                label = account.accountName,
                                description = account.accountNumber
                            )
                        }
                    }
                }
                if (modeOfDisbursementState.customerBanks.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "You don't have any bank accounts",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp, start = 16.dp, end = 16.dp)
            ) {
                ActionButton("Proceed", onClickContainer = {
                    selectedAccount?.let { account ->
                        component.onProceed(account)
                    }
                }, enabled = selectedAccount !== null, loading = modeOfDisbursementState.isLoading)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAccountView(
    Index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    deleteAccount: () -> Unit,
    label: String,
    description: String? = null,
) {
    ElevatedCard(
        onClick = {
            onClick.invoke(Index)
        }
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                    Card(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .border(
                                border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.outline),
                                shape = CircleShape
                            ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(if (selected) actionButtonColor else MaterialTheme.colorScheme.background)
                                .clickable {
                                    onClick.invoke(Index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check Box",
                                    tint = Color.White,
                                    modifier = Modifier.padding(1.dp)
                                )
                            }
                        }
                    }
                }

                Column {

                    Text(
                        text = label,
                        modifier = Modifier
                            .padding(start = 15.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                    )

                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }

                Row {
                    //Created a Custom checkBox
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(19.dp)
                            .clickable {
                                deleteAccount()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = null,
                            tint = actionButtonColor.copy(
                                alpha = 0.4f
                            )
                        )
                    }

                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
            }
        }
    }
}