package com.presta.customer.ui.components.profile.ui

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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.HomeCardListItem
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
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
    val stateLazyRow = rememberLazyListState()
    val quickLinks: List<QuickLinks> = listOf(
        QuickLinks("Add","Savings", Icons.Outlined.Savings),
        QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney),
        QuickLinks("Pay","Loan", Icons.Outlined.CreditCard),
        QuickLinks("View Full","Statement", Icons.Outlined.Description),

        )
    val item1=quickLinks[0]
    val item2=quickLinks[1]
    val item3=quickLinks[2]
    val item4=quickLinks[3]

    //Modal BottomSheet

    val skipHalfExpanded by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        skipHalfExpanded = skipHalfExpanded
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        modifier = Modifier.padding(innerPadding),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            LazyColumn{
                items(1){
                    ListItem (
                        text = {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                            ){
                                Column(modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                ) {

                                    Row(modifier = Modifier
                                        .padding(top = 17.dp)
                                        .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(text = "Activate  Account",
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                            fontSize = 14.sp
                                        )
                                        Icon(
                                            Icons.Filled.Cancel,
                                            contentDescription = "Cancel  Arrow",
                                            tint = backArrowColor,
                                            modifier = Modifier.clickable {
                                                scope.launch { sheetState.hide() }

                                            }
                                        )
                                    }

                                    Row(modifier = Modifier
                                        .padding(top = 17.dp)
                                        .fillMaxWidth()){
                                        Text(text = "Welcome to Rubani Sacco to, please pay a registration fee of KSH 1,000 to activate your account",
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                            fontSize = 12.sp
                                        )

                                    }

                                    Row(modifier = Modifier.fillMaxWidth()
                                        .padding(top=22.dp, bottom = 22.dp)){
                                        ActionButton("Activate Now!", onClickContainer = {

                                        })
                                    }

                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        LazyColumn(
            // consume insets as scaffold doesn't do it by default
            modifier = Modifier
                .consumeWindowInsets(innerPadding).padding(start = 16.dp, end = 16.dp, top = 16.dp),
            contentPadding = innerPadding
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Presta Capital",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp
                            )
                        }

                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            onClick = {

                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    modifier = Modifier.size(25.dp),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(top = 9.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Hello Morgan",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
            item {
                LazyRow(modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    state = stateLazyRow0,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow0),
                    content = {
                        items(3) {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                            ) {
                                HomeCardListItem(
                                    name = "$it",
                                    onClick = {

                                    },
                                    savingsBalance = if (state.balances !== null) state.balances.savingsBalance.toString() else "0.0",
                                )
                            }
                        }
                    }
                )
            }
            item {
                Paginator(3, stateLazyRow0.firstVisibleItemIndex)
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(top = 12.92.dp, start = 5.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Quick Links",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
            item {

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    state = stateLazyRow,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow)
                ) {
                    item {
                        //Add savings
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .size(57.dp),
                                onClick = {
                                    // component.onAddSavingsSelected()


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
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )

                            Text(
                                text = item1.labelBottom,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        //Apply loan

                        Column(
                            modifier = Modifier
                                .padding(start = 29.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .size(57.dp),
                                onClick = {
                                    //component.onApplyLoanSelected()

                                },
                                content = {
                                    Icon(
                                        imageVector = item2.icon,
                                        modifier = Modifier.size(30.dp),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                }
                            )

                            Text(
                                modifier = Modifier.padding(top = 7.08.dp),
                                text = item2.labelTop,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )

                            Text(
                                text = item2.labelBottom,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        //Pay Loan
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 29.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .size(57.dp),
                                onClick = {
                                    //  component.onPayLoanSelected()


                                },
                                content = {
                                    Icon(
                                        imageVector = item3.icon,
                                        modifier = Modifier.size(30.dp),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                }
                            )

                            Text(
                                modifier = Modifier.padding(top = 7.08.dp),
                                text = item3.labelTop,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )

                            Text(
                                text = item3.labelBottom,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }

                        //View Full  Statement
                        Column(
                            modifier = Modifier
                                .padding(start = 18.dp)
                                .fillMaxSize(),
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
                                        imageVector = item4.icon,
                                        modifier = Modifier.size(30.dp),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                }
                            )

                            Text(
                                modifier = Modifier.padding(top = 7.08.dp),
                                text = item4.labelTop,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )

                            Text(
                                text = item4.labelBottom,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }

                    }

                }
            }
            item {

                Box(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 5.dp)
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.fillParentMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Transactions",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )

                        Box(contentAlignment = Alignment.Center) {
                            Row() {
                                Text(
                                    text = "See all",
                                    textAlign = TextAlign.Center,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )

                                Icon(
                                    Icons.Filled.ArrowForward,
                                    contentDescription = "Forward Arrow",
                                    tint = backArrowColor,
                                )

                            }

                        }
                    }

                }
            }


            item {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    val transactionList = mutableListOf(
                        Transactions(
                            "Savings Deposit",
                            false,
                            "TRGHJK123LL",
                            "15,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan repayment",
                            false,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        ),
                        Transactions(
                            "Loan disbursed",
                            true,
                            "TRGHJK123LL",
                            "5,000",
                            "12 May 2020, 12:23 PM",
                            Icons.Filled.OpenInNew
                        )
                    )
                    transactionList.forEach { transaction ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier.padding(end = 12.dp),
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (transaction.credit) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer)
                                            .size(30.dp),
                                        onClick = {

                                        },
                                        content = {
                                            Icon(
                                                imageVector = transaction.icon,
                                                modifier = if (transaction.credit) Modifier.size(15.dp)
                                                    .rotate(180F) else Modifier.size(15.dp),
                                                contentDescription = null,
                                                tint = if (transaction.credit) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                            )
                                        }
                                    )
                                }
                                Column {
                                    Text(
                                        text = transaction.label,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = transaction.code,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 10.sp,
                                        fontFamily = fontFamilyResource(fontResource = MR.fonts.Poppins.regular)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    modifier = Modifier.align(Alignment.End),
                                    text = transaction.amount,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = transaction.date,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 10.sp,
                                    fontFamily = fontFamilyResource(fontResource = MR.fonts.Poppins.regular)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}