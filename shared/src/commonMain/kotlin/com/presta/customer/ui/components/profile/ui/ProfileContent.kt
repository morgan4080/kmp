package com.presta.customer.ui.components.profile.ui


import ShimmerBrush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Badge
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.network.authDevice.model.TenantServiceConfig
import com.presta.customer.network.authDevice.model.TenantServiceConfigResponse
import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.modeofDisbursement.store.ModeOfDisbursementStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.HomeCardListItem
import com.presta.customer.ui.composables.MainModalDrawerSheet
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.composables.singleTransaction
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class QuickLinks(val labelTop: String, val labelBottom: String, val icon: ImageVector, val action: () -> Unit, val badge: Boolean = false)

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun ProfileContent(
    authState: AuthStore.State,
    state: ProfileStore.State,
    addSavingsState: AddSavingsStore.State,
    modeOfDisbursementState: ModeOfDisbursementStore.State,
    innerPadding: PaddingValues,
    seeAllTransactions: () -> Unit,
    goToSavings: () -> Unit,
    goToLoans: () -> Unit,
    goToPayLoans: () -> Unit,
    goToLoansPendingApproval: () -> Unit,
    activateAccount: (amount: Double) -> Unit,
    logout: () -> Unit,
    reloadModels: () -> Unit,
) {
    val stateLazyRow0 = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val quickLinks: List<QuickLinks> = listOf(
        QuickLinks("Add", "Savings", Icons.Outlined.Savings, goToSavings),
        QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney, goToLoans),
        QuickLinks("Pay", "Loan", Icons.Outlined.CreditCard, goToPayLoans),
        QuickLinks("Awaiting", "Approval", Icons.Outlined.Notifications, goToLoansPendingApproval, true)
    )

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    )

    LaunchedEffect(authState.cachedMemberData) {
        if (authState.cachedMemberData !== null && authState.cachedMemberData.registrationFeeStatus == "NOT_PAID") {
            delay(2000L)
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    val balancesMap: MutableMap<String, Double?> = mutableMapOf(
        "Total Loan Balance" to null,
        "Total Savings Amount" to null
    )

    if (state.savingsBalances !== null ) {
        balancesMap["Total Savings Amount"] = state.savingsBalances.savingsTotalAmount
    }

    if (state.loansBalances !== null) {
        balancesMap["Total Loan Balance"] = state.loansBalances.totalBalance
    }

    var refreshing by remember { mutableStateOf(false) }

    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true

        reloadModels()

        delay(1500)
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    val items = listOf(
        "Log Out" to Icons.Outlined.Logout,
    )
    var selectedItem by remember { mutableStateOf("Log Out" to Icons.Outlined.Logout) }

    val scopeDrawer = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val savingsIsFalse = authState.tenantServicesConfig.contains(
        TenantServiceConfigResponse(TenantServiceConfig.savings, false)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainModalDrawerSheet(
                items,
                authState,
                logout,
                selectedItem,
                onItemsClick = { item ->
                    scopeDrawer.launch { drawerState.close() }
                    if (item !== null) selectedItem = item
                }
            )
        },
        content = {
            ModalBottomSheetLayout(
                modifier = Modifier,
                sheetState = sheetState,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetContent = {
                    AnimatedVisibility(
                        modifier = Modifier.padding(bottom = 85.dp),
                        visible = !authState.isLoading,
                        enter = fadeIn() + expandVertically()
                    ) {
                        Column (
                            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
                        ) {
                            Column (modifier = Modifier.padding(horizontal = 16.dp, vertical = 26.dp)) {
                                Row(
                                    modifier = Modifier
                                        .padding(top = 17.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Activate  Account",
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        fontSize = 14.sp
                                    )
                                    Icon(
                                        Icons.Filled.Cancel,
                                        contentDescription = "Cancel  Arrow",
                                        tint = backArrowColor,
                                        modifier = Modifier.absoluteOffset(y = -(20).dp).clickable {
                                            scope.launch { sheetState.hide() }
                                        }
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(top = 17.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Welcome to ${if (authState.authUserResponse !== null) authState.authUserResponse.companyName else ""}," +
                                                " please pay a registration fee of KSH ${if (authState.cachedMemberData !== null) authState.cachedMemberData.registrationFees else ""} to activate your account",
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                        fontSize = 12.sp
                                    )

                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 22.dp, bottom = 22.dp)
                                ) {
                                    ActionButton("Activate Now!", onClickContainer = {
                                        if (authState.cachedMemberData !== null) {
                                            activateAccount(
                                                authState.cachedMemberData.registrationFees
                                            )
                                        }
                                    }, enabled = authState.cachedMemberData !== null, loading = addSavingsState.isLoading)
                                }
                            }
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            modifier =Modifier
                                .background(color =  MaterialTheme.colorScheme.background),
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                            title = {},
                            navigationIcon = {
                                Text(
                                    modifier = Modifier.padding(start = 9.dp).background(
                                        brush = ShimmerBrush(
                                            targetValue = 1300f,
                                            showShimmer = authState.authUserResponse?.companyName == null
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ).defaultMinSize(200.dp),
                                    text = if (authState.authUserResponse !== null) authState.authUserResponse.companyName else "",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = TextStyle(fontSize = 18.sp),
                                )
                            },
                            actions = {
                                IconButton(
                                    modifier = Modifier.zIndex(1f),
                                    onClick = {
                                        scopeDrawer.launch { drawerState.open() }
                                    },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Filled.Apps,
                                            modifier = Modifier.size(25.dp),
                                            contentDescription = "Menu pending",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )
                            }
                        )
                    },
                    content = { pd ->
                        Box (Modifier.consumeWindowInsets(pd).pullRefresh(refreshState)) {

                            LazyColumn(
                                modifier = Modifier
                                    .wrapContentHeight(),
                                contentPadding = pd
                            ) {

                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.background(
                                                brush = ShimmerBrush(
                                                    targetValue = 1300f,
                                                    showShimmer = authState.authUserResponse?.firstName == null
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            ).defaultMinSize(150.dp),
                                            text = if (authState.authUserResponse !== null) "Hello ${authState.authUserResponse.firstName}" else "",
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                item {
                                    LazyRow(modifier = Modifier
                                        .fillMaxWidth()
                                        .consumeWindowInsets(pd)
                                        .padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                                        state = stateLazyRow0,
                                        flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow0),
                                        content = {
                                            balancesMap.filter { balance ->
                                                !(savingsIsFalse && balance.key == "Total Savings Amount")
                                            }.map { balance ->
                                                item {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(horizontal = 14.dp)
                                                            .fillParentMaxWidth(0.95f)
                                                    ) {
                                                        HomeCardListItem (
                                                            name = balance.key,
                                                            balance = balance.value,
                                                            lastAmount = if (state.savingsBalances !== null) state.savingsBalances.lastSavingsAmount else null,
                                                            lastDate =  if (state.savingsBalances !== null) state.savingsBalances.lastSavingsDate else null,
                                                            loanStatus =  if (state.loansBalances !== null && balance.key == "Total Loan Balance") state.loansBalances.loanStatus else null,
                                                            totalLoans = if (state.loansBalances !== null && balance.key == "Total Loan Balance") state.loansBalances.loanCount else null,
                                                            savingsBalance = if (state.savingsBalances !== null) state.savingsBalances.savingsBalance else null,
                                                            sharesBalance = if (state.savingsBalances !== null) state.savingsBalances.sharesBalance else null,
                                                            goToPayLoans = goToPayLoans,
                                                            goToSavings = goToSavings,
                                                            loanBreakDown = if (state.loansBalances !== null) state.loansBalances.loanBreakDown else null
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                                item {
                                    Paginator(if (savingsIsFalse) 1 else balancesMap.size, stateLazyRow0.firstVisibleItemIndex)
                                }
                                item {
                                    AnimatedVisibility(
                                        !authState.isLoading,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 12.92.dp)
                                                .padding(start = 16.dp, end = 16.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Quick Links",
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontSize = 18.sp,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                                            )
                                        }
                                    }
                                }
                                item {
                                    AnimatedVisibility(
                                        !authState.isLoading,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        Row (
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(top = 16.dp)
                                                .padding(start = 16.dp, end = 16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val transition = rememberInfiniteTransition()
                                            val translateAnimation = transition.animateFloat(
                                                initialValue = 0f,
                                                targetValue = 5f,
                                                animationSpec = infiniteRepeatable(
                                                    animation = tween(800), repeatMode = RepeatMode.Reverse
                                                )
                                            )

                                            quickLinks.filter{ link ->
                                                !(savingsIsFalse && link.labelBottom == "Savings")
                                            }.map { item1 ->
                                                BadgedBox(badge = {
                                                    if (item1.badge && modeOfDisbursementState.loans.isNotEmpty()) {
                                                        Badge (
                                                            modifier = Modifier.graphicsLayer {
                                                                translationY = translateAnimation.value
                                                            }
                                                        ) { Text(modeOfDisbursementState.loans.count().toString()) }
                                                    }
                                                }) {
                                                    Column(
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        IconButton(
                                                            modifier = Modifier
                                                                .clip(shape = RoundedCornerShape(10.dp))
                                                                .background(MaterialTheme.colorScheme.primary)
                                                                .size(57.dp),
                                                            onClick = {
                                                                item1.action()
                                                            },
                                                            content = {
                                                                Icon(
                                                                    imageVector = item1.icon,
                                                                    modifier = Modifier.size(30.dp),
                                                                    contentDescription = null,
                                                                    tint = Color.White
                                                                )
                                                            }
                                                        )

                                                        Text(
                                                            modifier = Modifier.padding(top = 7.08.dp),
                                                            text = item1.labelTop,
                                                            color = MaterialTheme.colorScheme.onBackground,
                                                            fontSize = 12.sp,
                                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                        )

                                                        Text(
                                                            text = item1.labelBottom,
                                                            color = MaterialTheme.colorScheme.onBackground,
                                                            fontSize = 12.sp,
                                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(top = 26.dp, bottom = 10.dp)
                                            .padding(start = 16.dp, end = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Transactions",
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = 18.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                                        )

                                        Row (
                                            modifier = Modifier.clickable{seeAllTransactions()},
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        )  {
                                            Text(
                                                modifier = Modifier.padding(end = 5.dp),
                                                text = "See all",
                                                textAlign = TextAlign.Center,
                                                color = backArrowColor,
                                                fontSize = 12.sp,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                                style = MaterialTheme.typography.headlineSmall
                                            )

                                            Icon (
                                                Icons.Filled.ArrowForward,
                                                contentDescription = "Forward Arrow",
                                                tint = backArrowColor,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                                item {
                                    Column (modifier = Modifier.padding(horizontal = 16.dp)) {
                                        singleTransaction(state.transactionHistory)
                                    }
                                }
                                item {

                                    Spacer(
                                        modifier = Modifier
                                            .padding(bottom = 50.dp)
                                    )
                                }
                            }

                            PullRefreshIndicator(refreshing, refreshState,
                                Modifier
                                    .padding(pd)
                                    .align(Alignment.TopCenter).zIndex(1f),
                                contentColor = actionButtonColor
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                )
            }
        }
    )
}