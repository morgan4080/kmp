package com.presta.customer.ui.components.profile.ui


import ShimmerBrush
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.HomeCardListItem
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.composables.singleTransaction
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class QuickLinks(val labelTop: String, val labelBottom: String, val icon: ImageVector, val action: () -> Unit)

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun ProfileContent(
    authState: AuthStore.State,
    state: ProfileStore.State,
    innerPadding: PaddingValues,
    seeAllTransactions: () -> Unit,
    goToSavings: () -> Unit,
    goToLoans: () -> Unit,
    goToPayLoans: () -> Unit,
    goToStatement: () -> Unit
) {
    val stateLazyRow0 = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val quickLinks: List<QuickLinks> = listOf(
        QuickLinks("Add", "Savings", Icons.Outlined.Savings, goToSavings),
        QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney, goToLoans),
        QuickLinks("Pay", "Loan", Icons.Outlined.CreditCard, goToPayLoans),
        QuickLinks("View Full", "Statement", Icons.Outlined.Description, goToStatement)
    )

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
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
        "Total Savings Amount" to null,
        "Total Loan Balance" to null
    )

    if (state.savingsBalances !== null ) {
        balancesMap["Total Savings Amount"] = state.savingsBalances.savingsTotalAmount
    }

    if (state.loansBalances !== null) {
        balancesMap["Total Loan Balance"] = state.loansBalances.totalBalance
    }

    println("::::::::state.savingsBalances.lastSavingsAmount")
    println(state.savingsBalances)

    ModalBottomSheetLayout(
        modifier = Modifier.padding(innerPadding),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
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

                        })
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier =Modifier
                        .background(color =  MaterialTheme.colorScheme.background)
                        .padding(start = 9.dp, end = 16.dp),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        modifier = Modifier.background(
                                            brush = ShimmerBrush(
                                                targetValue = 1300f,
                                                showShimmer = authState.authUserResponse?.companyName == null
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ).defaultMinSize(200.dp),
                                        text = if (authState.authUserResponse !== null) authState.authUserResponse.companyName else "",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 18.sp
                                    )
                                }
                                IconButton(
                                    modifier = Modifier.absoluteOffset(x = 15.dp).alpha(0.0f),
                                    onClick = {
//                                        onAuthEvent(AuthStore.Intent.LogOutUser)
                                    },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Outlined.Logout,
                                            modifier = Modifier.size(25.dp),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )
                            }
                        }
                    }
                )
            },
            content = { innerPadding ->
                LazyColumn(
                    // consume insets as scaffold doesn't do it by default
                    modifier = Modifier
                        .consumeWindowInsets(innerPadding)
                        .wrapContentHeight(),
                    contentPadding = innerPadding
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
                            .consumeWindowInsets(innerPadding)
                            .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            state = stateLazyRow0,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow0),
                            content = {
                                balancesMap.map { balance ->
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
                                                goToLoans = goToLoans,
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
                        Paginator(balancesMap.size, stateLazyRow0.firstVisibleItemIndex)
                    }
                    item {
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
                    item {
                        Row (
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            quickLinks.map { item1 ->
                                Column (
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
                                                tint = MaterialTheme.colorScheme.inverseOnSurface
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
                        singleTransaction(state.transactionHistory)
                    }

                    item {

                        Spacer(
                            modifier = Modifier
                                .padding(bottom = 50.dp)
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        )
    }
}