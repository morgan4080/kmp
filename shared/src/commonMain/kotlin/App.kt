import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.ScatterPlot
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SettingsApplications
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.AppTheme


data class QuickLinks(val labelTop: String,val labelBottom: String, val icon: ImageVector)
data class Transactions(val label: String, val credit: Boolean, val code: String, val amount: String, val date: String, val icon: ImageVector)

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun App() {
    AppTheme {
        Scaffold(
            content = { innerPadding ->
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
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                    item {
                        val state = rememberLazyListState()
                        LazyRow(modifier = Modifier
                            .consumeWindowInsets(innerPadding)
                            .padding(top = 10.dp),
                            state = state,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            LazyRow(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {
                                items(3) {
                                    when (it) {
                                        0 -> {
                                            Box(
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .width(20.dp)
                                                    .height(10.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(0xFF489AAB.toInt()))
                                            )
                                        }

                                        1 -> {
                                            Box(
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE5E5E5.toInt()))
                                            )
                                        }

                                        2 -> {
                                            Box(
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE5E5E5.toInt()))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .padding(top = 12.92.dp, start = 5.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Quick Links",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                    item {
                        val quickLinks = listOf(
                            QuickLinks("Add","Savings", Icons.Outlined.Savings),
                            QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney),
                            QuickLinks("Pay","Loan", Icons.Outlined.CreditCard),
                            QuickLinks("View Full","Statement", Icons.Outlined.Description),
                        )
                        val state = rememberLazyListState()
                        LazyRow(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            state = state,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                            content = {
                                quickLinks.forEach { link ->
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            IconButton(
                                                modifier = Modifier
                                                    .clip(shape = CircleShape)
//                                                            RoundedCornerShape(10.dp)
                                                    .background(MaterialTheme.colorScheme.primary)
                                                    .size(57.dp),
                                                onClick = {

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
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Light
                                            )

                                            Text(
                                                text = link.labelBottom,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Light
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .padding(top = 26.dp, start = 5.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Transactions",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )

                                            Text(
                                                text = transaction.code,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Light
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            modifier = Modifier.align(Alignment.End),
                                            text = transaction.amount,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = transaction.date,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomTabNavigator()
            },
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeCardListItem(name: String, onClick: (String) -> Unit) {
    var showExpanded by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 12.dp))
            .absolutePadding(left = 2.dp, right = 2.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Box (modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
            Column (
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
                )
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Total Savings",
                        color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp
                    )

                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .size(30.dp),
                        onClick = {
                            showExpanded = !showExpanded
                            onClick(name)
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                modifier = if (showExpanded) Modifier.size(25.dp).rotate(180F) else Modifier.size(25.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes 100,983.32",
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Row (modifier = Modifier
                    .padding(top = 11.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Last Savings",
                        color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp
                    )
                }
                Row (modifier = Modifier
                    .padding(top = 1.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "KES 5,000 - 5 Days Ago",
                        color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(showExpanded) {
                    Image(
                        painterResource("compose-multiplatform.xml"),
                        null
                    )
                }
            }
        }
    }
}

@Composable
fun BottomTabNavigator() {
    val screens = listOf("Home", "Loans", "Savings", "Sign")
    var selectedScreen by remember { mutableStateOf(screens.first()) }
    BottomAppBar (
        contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 25.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        screens.forEach { screen ->
            NavigationBarItem (
                icon = { Icon(imageVector = getIconForScreen(screen), contentDescription = null, modifier = Modifier.size(27.dp)) },
                label = {
                    Text(
                        text = screen,
                        color= if (screen == selectedScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.absoluteOffset(y = 28.dp)
                    )
                },
                selected = screen == selectedScreen,
                onClick = {
                    selectedScreen = screen
                },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun getIconForScreen(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Outlined.Home
        "Loans" -> Icons.Outlined.Wallet
        "Savings" -> Icons.Outlined.Logout
        "Sign" -> Icons.Outlined.ScatterPlot
        else -> Icons.Outlined.Home
    }
}

expect fun getPlatformName(): String