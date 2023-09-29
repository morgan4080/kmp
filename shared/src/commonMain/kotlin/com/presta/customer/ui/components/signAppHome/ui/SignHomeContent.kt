package com.presta.customer.ui.components.signAppHome.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.SignHomeComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.MainModalDrawerSheet
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.composables.SignProductSelection
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignHomeContent(
    component: SignHomeComponent,
    state: SignHomeStore.State,
    applyLongTermLoanState: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onApplyLongTermLoanEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    logout: () -> Unit,

    ) {
    var memBerRefId by remember { mutableStateOf("") }
    var great by remember { mutableStateOf("") }
    val pattern = Regex("(\\d{2}):\\d{2}:\\d{2}\\.\\d+")
    val thisTime: LocalTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    val matchResult = pattern.find(thisTime.toString())
    val hours = matchResult?.groups?.get(1)?.value?.toInt()
    great = when (hours) {
        in 6..11 -> {
            "Good Morning"
        }

        in 12..17 -> {
            "Good Afternoon"
        }

        else -> {
            "Good Evening"
        }
    }
    if (state.prestaTenantByPhoneNumber?.refId != null) {
        memBerRefId = state.prestaTenantByPhoneNumber.refId

    }
    if (state.prestaTenantByPhoneNumber?.refId != null) {
        LaunchedEffect(
            authState.cachedMemberData,
            memBerRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsFilteredList(
                    token = it.accessToken,
                    memberRefId = memBerRefId
                )

            }?.let {
                onApplyLongTermLoanEvent(
                    it
                )
            }
        }
    }
    val scopeDrawer = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = listOf(
        "Log Out" to Icons.Outlined.Logout,
    )
    var selectedItem by remember { mutableStateOf("Log Out" to Icons.Outlined.Logout) }

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
            Scaffold(
                modifier = Modifier.padding(LocalSafeArea.current),
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(start = 9.dp),
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
                                    }
                                    IconButton(
                                        modifier = Modifier.absoluteOffset(x = 6.dp).zIndex(1f),
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
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        item {
                            ElevatedCard(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(size = 12.dp)),
                                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(
                                            16.dp
                                        )
                                    ) {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = great.uppercase(),
                                                color= MaterialTheme.colorScheme.onBackground,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                modifier = Modifier.background(
                                                    brush = ShimmerBrush(
                                                        targetValue = 1300f,
                                                        showShimmer = state.prestaTenantByPhoneNumber?.fullName == null
                                                    ),
                                                    shape = RoundedCornerShape(12.dp)
                                                ).defaultMinSize(150.dp),
                                                text = if (state.prestaTenantByPhoneNumber?.fullName !== null) state.prestaTenantByPhoneNumber.firstName.uppercase() else "",
                                                color= MaterialTheme.colorScheme.onBackground,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .padding(top = 2.dp)
                                                    .background(
                                                        brush = ShimmerBrush(
                                                            targetValue = 1300f,
                                                            showShimmer = state.prestaTenantByPhoneNumber?.fullName == null
                                                        ),
                                                        shape = RoundedCornerShape(12.dp)
                                                    ).defaultMinSize(150.dp),
                                                text = if (state.prestaTenantByPhoneNumber?.memberNumber !== null) state.prestaTenantByPhoneNumber.memberNumber.uppercase() else "",
                                                color= MaterialTheme.colorScheme.onBackground,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = "Balance",
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                )
                                            }
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    modifier = Modifier
                                                        .padding(top = 2.dp)
                                                        .background(
                                                            brush = ShimmerBrush(
                                                                targetValue = 1300f,
                                                                showShimmer = state.prestaTenantByPhoneNumber?.totalShares == null
                                                            ),
                                                            shape = RoundedCornerShape(12.dp)
                                                        ).defaultMinSize(50.dp),
                                                    text = if (state.prestaTenantByPhoneNumber?.totalShares !== null) "${
                                                        formatMoney(
                                                            state.prestaTenantByPhoneNumber.totalShares +
                                                                    state.prestaTenantByPhoneNumber.totalDeposits
                                                        )
                                                    } KES" else "",
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = 20.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                                )
                                            }
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    modifier = Modifier
                                                        .padding(top = 2.dp)
                                                        .background(
                                                            brush = ShimmerBrush(
                                                                targetValue = 1300f,
                                                                showShimmer = state.prestaTenantByPhoneNumber?.totalShares == null
                                                            ),
                                                            shape = RoundedCornerShape(12.dp)
                                                        ).defaultMinSize(50.dp),
                                                    text = if (state.prestaTenantByPhoneNumber?.totalShares !== null) "Shares: " +
                                                        formatMoney(
                                                            state.prestaTenantByPhoneNumber.totalShares
                                                        ) + " & " +
                                                        "Deposits: " + formatMoney(
                                                            state.prestaTenantByPhoneNumber.totalDeposits
                                                        ) else "",
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = 12.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                )
                                            }
                                            Divider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 20.dp),
                                                thickness = 2.dp,
                                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    "Loan Requests",
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                )
                                                Text(
                                                    "See all",
                                                    fontSize = 14.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.clickable {
                                                        component.goToLoanRequests()
                                                    }
                                                )

                                            }
                                            if (applyLongTermLoanState.isLoading) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            vertical = 10.dp
                                                        )
                                                        .background(color = Color.Transparent),
                                                ) {
                                                    ElevatedCard(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(color = Color.Transparent),
                                                        colors = CardDefaults.elevatedCardColors(
                                                            containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                                        )
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .defaultMinSize(40.dp, 40.dp)
                                                                .background(
                                                                    ShimmerBrush(
                                                                        targetValue = 1300f,
                                                                        showShimmer = true
                                                                    )
                                                                )
                                                                .fillMaxWidth()
                                                        ) {
                                                        }
                                                    }
                                                }

                                            } else {
                                                if (applyLongTermLoanState.prestaLongTermLoansRequestsFilteredList?.content.isNullOrEmpty()) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Center
                                                    ) {
                                                        Text(
                                                            text = "You don't have Loan Requests",
                                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                            fontSize = 12.sp
                                                        )
                                                    }
                                                } else {
                                                    applyLongTermLoanState.prestaLongTermLoansRequestsFilteredList?.content?.map { filteredLoanRequests ->
                                                        Row(modifier = Modifier.padding(top = 10.dp)) {
                                                            LoanRequestsListing(
                                                                loanName = filteredLoanRequests.loanProductName,
                                                                loanRequestDate = filteredLoanRequests.loanDate,
                                                                loanAmount = formatMoney(
                                                                    filteredLoanRequests.loanAmount
                                                                ) + " KES",
                                                                loanProgress = filteredLoanRequests.loanRequestProgress.toFloat() / 100,
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                            ) {
                                SignProductSelection(
                                    icon = Icons.Outlined.Assignment,
                                    label1 = "Apply for a loan",
                                    label2 = "Create a loan Request",
                                    onClickContainer = {
                                        component.onApplyLoanSelected()

                                    },
                                    backgroundColor = MaterialTheme.colorScheme.primary,
                                    textColor = MaterialTheme.colorScheme.background,
                                    iconTint = MaterialTheme.colorScheme.background
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            ) {
                                SignProductSelection(
                                    icon = Icons.Outlined.Assignment,
                                    label1 = "Guarantorship requests ",
                                    label2 = "View guarantorship requets",
                                    onClickContainer = {
                                        //navigate to view requests
                                        component.guarantorshipRequestsSelected()

                                    },
                                    backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    iconTint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            ) {
                                SignProductSelection(
                                    icon = Icons.Outlined.Grade,
                                    label1 = "Favorite guarantors",
                                    label2 = "View  and edit your favourite guarantors",
                                    onClickContainer = {
                                        //navigate to favourite guarantors
                                        component.favouriteGuarantorsSelected()

                                    },
                                    backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    iconTint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            ) {
                                SignProductSelection(
                                    icon = Icons.Outlined.Shield,
                                    label1 = "Witness request",
                                    label2 = "View witness requests",
                                    onClickContainer = {
                                        component.witnessRequestSelected()

                                    },
                                    backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    iconTint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(bottom = 100.dp))
                        }
                    }
                })


        })


}

@Composable
fun LoanRequestsListing(
    loanName: String,
    loanRequestDate: String,
    loanAmount: String,
    loanProgress: Float
) {
    val progress by remember { mutableStateOf(loanProgress) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircularProgressBarWithText(
                progress = progress,
                text = "${(progress * 100).toInt()}%",
                modifier = Modifier.size(40.dp)
            )

            Column(modifier = Modifier.padding(start = 15.dp)) {
                Text(
                    text = loanName,
                    color= MaterialTheme.colorScheme.onBackground,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = loanRequestDate,
                    color= MaterialTheme.colorScheme.outline,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            text = loanAmount,
            color= MaterialTheme.colorScheme.onBackground,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Composable
fun CircularProgressBarWithText(
    progress: Float,
    text: String,
    strokeWidth: Dp = 2.dp,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(modifier = modifier.size(80.dp)) {
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = strokeWidth,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            modifier = Modifier.align(Alignment.Center)
        )
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = strokeWidth,
            color = color,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        )
    }
}





