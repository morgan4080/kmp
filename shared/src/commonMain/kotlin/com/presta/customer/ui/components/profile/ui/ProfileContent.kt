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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.OpenInNew
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.profile.model.PostingType
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.HomeCardListItem
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class QuickLinks(val labelTop: String, val labelBottom: String, val icon: ImageVector)

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun ProfileContent(
    authState: AuthStore.State,
    state: ProfileStore.State,
    onEvent: (ProfileStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues,
) {
    val stateLazyRow0 = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val quickLinks: List<QuickLinks> = listOf(
        QuickLinks("Add", "Savings", Icons.Outlined.Savings),
        QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney),
        QuickLinks("Pay", "Loan", Icons.Outlined.CreditCard),
        QuickLinks("View Full", "Statement", Icons.Outlined.Description)
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
        "Savings Balance" to null,
        "Shares Balance" to null,
        "Shares Count" to null,
        "Savings Total Amount" to null,
        "Price Per Share" to null
    )

    if (state.balances !== null) {
        val (savingsBalance, sharesBalance, sharesCount, savingsTotalAmount, pricePerShare) = state.balances
        balancesMap["Savings Balance"] = savingsBalance
        balancesMap["Shares Balance"] = sharesBalance
        balancesMap["Shares Count"] = sharesCount.toDouble()
        balancesMap["Savings Total Amount"] = savingsTotalAmount
        balancesMap["Price Per Share"] = pricePerShare
    }

    ModalBottomSheetLayout(
        modifier = Modifier.padding(innerPadding),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
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
                                    modifier = Modifier.absoluteOffset(x = 15.dp),
                                    onClick = {
                                        onAuthEvent(AuthStore.Intent.LogOutUser)
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
                                                onClick = {
                                                    print(it)
                                                    print(balance.value)
                                                },
                                                balance = balance.value,
                                                lastSavingsAmount = null,
                                                lastSavingsDate = null
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
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            )  {
                                Text(
                                    modifier = Modifier.padding(end = 10.dp),
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

                    if (state.transactionHistory !== null) {
                        state.transactionHistory.map { transaction ->
                            item {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .padding(start = 16.dp, end = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row {
                                            Column(
                                                modifier = Modifier.padding(end = 12.dp),
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                IconButton(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (transaction.postingType == PostingType.CR)
                                                                MaterialTheme.colorScheme.secondaryContainer
                                                            else
                                                                MaterialTheme.colorScheme.errorContainer
                                                        ).size(30.dp),
                                                    onClick = {

                                                    },
                                                    content = {
                                                        Icon(
                                                            imageVector = Icons.Filled.OpenInNew,
                                                            modifier = if (transaction.postingType == PostingType.CR)
                                                                Modifier.size(15.dp)
                                                                    .rotate(180F)
                                                            else
                                                                Modifier.size(15.dp),
                                                            contentDescription = null,
                                                            tint = if (transaction.postingType == PostingType.CR)
                                                                MaterialTheme.colorScheme.secondary
                                                            else
                                                                MaterialTheme.colorScheme.error
                                                        )
                                                    }
                                                )
                                            }
                                            Column {
                                                Row(
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .padding(top = 4.dp)
                                                        .clip(shape = RoundedCornerShape(15.dp))
                                                        .fillMaxWidth(0.5f)
                                                ) {
                                                    Text(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        text = transaction.purpose,
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        fontSize = 14.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                        textAlign = TextAlign.End
                                                    )
                                                }

                                                Row(
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .padding(top = 4.dp)
                                                        .clip(shape = RoundedCornerShape(15.dp))
                                                        .fillMaxWidth(0.5f)
                                                ) {
                                                    Text(
                                                        modifier = Modifier
                                                            .fillMaxWidth(),
                                                        text = transaction.transactionId,
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        fontSize = 14.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                        textAlign = TextAlign.End
                                                    )
                                                }
                                            }
                                        }
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .background(MaterialTheme.colorScheme.background)
                                                    .padding(top = 4.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .fillMaxWidth(0.5f)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    text = transaction.amount.toString(),
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                    textAlign = TextAlign.End
                                                )
                                            }

                                            //Date

                                            Row(
                                                modifier = Modifier
                                                    .background(MaterialTheme.colorScheme.background)
                                                    .padding(top = 4.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .fillMaxWidth(0.5f)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    text = transaction.transactionDate,
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                    textAlign = TextAlign.End
                                                )

                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (state.transactionHistory.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .padding(start = 16.dp, end = 16.dp)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = "You don't have transaction history",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        items(3) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .padding(start = 16.dp, end = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Row {
                                        Column(
                                            modifier = Modifier.padding(end = 12.dp),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            IconButton(
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .background(Color.Transparent).size(30.dp),
                                                onClick = {},
                                                content = {
                                                    Text(
                                                        text = "",
                                                        modifier = Modifier
                                                            .background(
                                                                brush = ShimmerBrush(
                                                                    targetValue = 1300f,
                                                                    showShimmer = true
                                                                ),
                                                                shape = RoundedCornerShape(12.dp))
                                                            .defaultMinSize(minHeight = 8.dp, minWidth = 25.dp)
                                                    )
                                                }
                                            )
                                        }
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .background(MaterialTheme.colorScheme.background)
                                                    .padding(top = 4.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .fillMaxWidth(0.5f)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .background(
                                                            ShimmerBrush(
                                                                targetValue = 1300f,
                                                                showShimmer = true
                                                            )
                                                        ).fillMaxWidth(),
                                                    text = "",
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                    textAlign = TextAlign.End
                                                )
                                            }

                                            Row(
                                                modifier = Modifier
                                                    .background(MaterialTheme.colorScheme.background)
                                                    .padding(top = 4.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .fillMaxWidth(0.5f)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .background(
                                                            ShimmerBrush(
                                                                targetValue = 1300f,
                                                                showShimmer = true
                                                            )
                                                        )
                                                        .fillMaxWidth(),
                                                    text = "",
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                    textAlign = TextAlign.End
                                                )
                                            }
                                        }
                                    }
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.background)
                                                .padding(top = 4.dp)
                                                .clip(shape = RoundedCornerShape(15.dp))
                                                .fillMaxWidth(0.5f)
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .background(
                                                        ShimmerBrush(
                                                            targetValue = 1300f,
                                                            showShimmer = true
                                                        )
                                                    )
                                                    .fillMaxWidth(),
                                                text = "",
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontSize = 14.sp,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                textAlign = TextAlign.End
                                            )
                                        }

                                        //Date

                                        Row(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.background)
                                                .padding(top = 4.dp)
                                                .clip(shape = RoundedCornerShape(15.dp))
                                                .fillMaxWidth(0.5f)
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .background(
                                                        ShimmerBrush (
                                                            targetValue = 1300f,
                                                            showShimmer = true
                                                        )
                                                    )
                                                    .fillMaxWidth(),
                                                text = "",
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontSize = 14.sp,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                                textAlign = TextAlign.End
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        )
    }
}