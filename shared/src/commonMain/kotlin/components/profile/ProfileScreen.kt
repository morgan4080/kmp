package components.profile

import composables.HomeCardListItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.Paginator


data class QuickLinks(val labelTop: String,val labelBottom: String, val icon: ImageVector)
data class Transactions(val label: String, val credit: Boolean, val code: String, val amount: String, val date: String, val icon: ImageVector)

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(component: ProfileComponent, innerPadding: PaddingValues) {
    val model by component.model.subscribeAsState()
    val stateLazyRow0 = rememberLazyListState()
    val stateLazyRow = rememberLazyListState()
    var quickLinks: List<QuickLinks>

    LazyColumn(
        // consume insets as scaffold doesn't do it by default
        modifier = Modifier
            .consumeWindowInsets(innerPadding).padding(16.dp),
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

                                }
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
             quickLinks = listOf(
                QuickLinks("Add","Savings", Icons.Outlined.Savings),
                QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney),
                QuickLinks("Pay","Loan", Icons.Outlined.CreditCard),
                QuickLinks("View Full","Statement", Icons.Outlined.Description),
            )

            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                state = stateLazyRow,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow)
            ) {
                quickLinks.forEach { link ->
                    item {
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
                                          //iterate through  the list of items and execute on condition
                                          //Test Navigate to add Savings--Savings child
                                         // component.onProfileSelected()

                                    for (i in quickLinks.indices) {

                                    }

                                },
                                content = {
                                    Icon(
                                        imageVector = link.icon,
                                        modifier = Modifier.size(30.dp),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                }
                            )

                            Text(
                                modifier = Modifier.padding(top = 7.08.dp),
                                text = link.labelTop,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )

                            Text(
                                text = link.labelBottom,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
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
                Text(
                    text = "Transactions",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
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
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row {
                            Column (
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
                                            modifier = if (transaction.credit) Modifier.size(15.dp).rotate(180F) else Modifier.size(15.dp),
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
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light
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
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
    }

}