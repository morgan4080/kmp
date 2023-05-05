import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.ScatterPlot
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.AppTheme


data class QuickLinks(val labelTop: String,val labelBottom: String, val icon: ImageVector)

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun App() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize().background(Color.White),
            color = Color.White
        ) {
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
                                        Box(
                                            modifier = Modifier
                                                .padding(end = 5.dp)
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF489AAB.toInt())) // #8F8F8F
                                        )

                                        Text(
                                            text = "Presta Capital",
                                            color = Color.Black,
                                            fontSize = 20.sp
                                        )
                                    }

                                    IconButton(
                                        onClick = {

                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Filled.GridView,
                                                modifier = Modifier.size(30.dp),
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
                                    .padding(top = 9.dp, start = 5.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Hello Morgan",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        item {
                            val state = rememberLazyListState()
                            LazyRow(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                                state = state,
                                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                                content = {
                                    items(3) {
                                        Box(
                                            modifier = Modifier
                                                .padding(PaddingValues(start = 2.dp, end = 9.dp))
                                                .fillParentMaxWidth(0.99f)
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
                                    .padding(top = 20.dp)
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
                                    .padding(top = 9.dp, start = 5.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Quick Links",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        item {
                            val quickLinks = listOf(
                                QuickLinks("Add","Savings", Icons.Outlined.Savings),
                                QuickLinks("Apply", "Loan", Icons.Outlined.AttachMoney),
                                QuickLinks("Pay","Loan", Icons.Outlined.CreditCard),
                                QuickLinks("View Full","Statement", Icons.Outlined.FileOpen),
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
                                                        .clip(shape = RoundedCornerShape(10.dp))
                                                        .background(MaterialTheme.colorScheme.primary)
                                                        .size(57.dp),
                                                    onClick = {

                                                    },
                                                    content = {
                                                        Icon(
                                                            imageVector = link.icon,
                                                            modifier = Modifier.size(25.dp),
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.inverseOnSurface
                                                        )
                                                    }
                                                )

                                                Text(
                                                    text = link.labelTop,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium
                                                )

                                                Text(
                                                    text = link.labelBottom,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                },
                bottomBar = {
                    BottomTabNavigator()
                },
            )
        }
    }
}

@Composable
fun HomeCardListItem(name: String, onClick: (String) -> Unit) {
    ElevatedCard {
        Box (modifier = Modifier.background(Color(0xFFFFFFFF.toInt()))) {
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
                            .background(Color(0xFFE5F1F5.toInt()))
                            .size(30.dp),
                        onClick = {
                            onClick(name)
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                modifier = Modifier.size(25.dp),
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
                        text = "Kes 5,000 - 5 days ago",
                        color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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
        contentPadding = PaddingValues(16.dp),
        containerColor = Color.White
    ) {
        screens.forEach { screen ->
            NavigationBarItem (
                icon = { Icon(imageVector = getIconForScreen(screen), contentDescription = null) },
                label = {
                    Text(
                        text = screen,
                        color= if (screen == selectedScreen) MaterialTheme.colorScheme.primary else Color(0xFF002C56.toInt()),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.absoluteOffset(y = 30.dp)
                    )
                },
                selected = screen == selectedScreen,
                onClick = {
                    selectedScreen = screen
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White, selectedIconColor = MaterialTheme.colorScheme.primary)
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