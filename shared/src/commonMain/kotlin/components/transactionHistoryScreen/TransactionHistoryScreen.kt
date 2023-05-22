package components.transactionHistoryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.unit.dp
import composables.AllTransactionHistory
import composables.LoansTransactionHistory
import composables.NavigateBackTopBar
import composables.SavingsTransactionHistory
import theme.actionButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(component: TransactionHistoryComponent) {

    var userInput by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {

            Row(modifier = Modifier
                .fillMaxWidth()) {
                NavigateBackTopBar("Transaction History", onClickContainer ={

                } )

            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                        .background(
                        MaterialTheme.colorScheme.background
                    )
                ) {

                    ElevatedCard() {
                        Row(modifier = Modifier
                            .fillMaxWidth()) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (focus.value != it.isFocused) {
                                            focus.value = it.isFocused
                                            if (!it.isFocused) {
                                                inputService?.hideSoftwareKeyboard()
                                            }
                                        }
                                    },
                                keyboardActions = KeyboardActions(

                                    onNext = {
                                        focusRequester.requestFocus()
                                    },
                                ),
                                // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                                onValueChange = {
                                    userInput = it
                                    //eventsViewModel.eventName = userInput
                                },
                                value = userInput,
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                leadingIcon = {
                                    Icon(

                                        Icons.Filled.Search,
                                        contentDescription = "Search Icon",
                                        modifier = Modifier
                                            .clip(shape = CircleShape)
                                            .background(color = MaterialTheme.colorScheme.background),
                                    )
                                },
                                trailingIcon = {
                                    Icon(

                                        Icons.Filled.Close,
                                        contentDescription = "Cancel icon",
                                        modifier = Modifier
                                            .clip(shape = CircleShape)
                                            .background(color = MaterialTheme.colorScheme.background),
                                    )

                                }
                            )

                        }

                    }

                }
                //Navigation Tabs
                var tabIndex by remember { mutableStateOf(0) }

                val tabs = listOf("All ", "Savings", "Loans")
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(modifier = Modifier
                        .fillMaxWidth()) {
                        TabRow(selectedTabIndex = tabIndex,
                            containerColor = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(29.dp))
                                .background(Color.Gray.copy(alpha = 0.5f)),
                            indicator = {},
                            divider ={

                            }
                        ) {
                            tabs.forEachIndexed { index, title ->

                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(43.dp)
                                    .clip(shape = RoundedCornerShape(70.dp))){

                                    Card(
                                        shape = RoundedCornerShape(70.dp),
                                        colors = CardDefaults.cardColors(containerColor = if (tabIndex == index) actionButtonColor else Color.Transparent),
                                        modifier = Modifier
                                            .padding(1.5.dp)){

                                        Tab(text = {
                                            Text(text = title)
                                        },
                                            selected = tabIndex == index,
                                            onClick = {
                                                tabIndex = index
                                            },
                                            modifier = Modifier
                                                .clip(shape= RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp, bottomStart = 70.dp, bottomEnd = 70.dp))
                                                .background(color = if (tabIndex == index) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)),
                                            selectedContentColor = Color.Black,
                                            unselectedContentColor = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                        when (tabIndex) {
                            0 -> AllTransactionHistory()
                            1 -> SavingsTransactionHistory()
                            2 -> LoansTransactionHistory()

                        }
                    }




//                    TabRow(selectedTabIndex = tabIndex,
//                        containerColor = Color.Gray.copy(alpha = 0.1f),
//                        modifier = Modifier
//                            .clip(shape = RoundedCornerShape(29.dp))
//                            .background(Color.Gray.copy(alpha = 0.1f))
//                            .align(Alignment.CenterVertically),
//                        indicator = {},
//                        divider = {
//
//                        }
//                    ) {
//                        tabs.forEachIndexed { index, title ->
//                            Row(
//                                modifier = Modifier
//                                    .background(color = Color.Gray.copy(alpha = 0.1f))
//                                    .fillMaxWidth()
//                                    .padding(top = 1.dp, bottom = 1.dp)
//                                    .clip(shape = RoundedCornerShape(70.dp)),
//                                horizontalArrangement = Arrangement.Center
//                            ) {
//                                Card(
//                                    modifier = Modifier
//                                        .background(color = Color.White.copy(alpha = 0.1f))
//                                        .padding(1.dp)
//                                        .clip(shape = RoundedCornerShape(20.dp)),
//                                    colors = CardDefaults.cardColors(
//                                        containerColor = if (tabIndex == index) actionButtonColor else Color.White.copy(
//                                            alpha = 0.1f
//                                        )
//                                    ),
//                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
//                                ) {
//
//                                    Tab(
//                                        text = {
//
//                                            Text(
//                                                text = title,
//                                                color = if (tabIndex == index) Color.White else Color.Black
//                                            )
//                                        },
//                                        selected = tabIndex == index,
//                                        onClick = {
//                                            tabIndex = index
//                                        },
//                                        modifier = Modifier
//                                            .clip(
//                                                shape = RoundedCornerShape(
//                                                    topStart = 80.dp,
//                                                    topEnd = 80.dp,
//                                                    bottomStart = 80.dp,
//                                                    bottomEnd = 80.dp
//                                                )
//                                            )
//                                            // .padding(2.dp)
//                                            .height(36.dp)
//                                            .width(100.dp)
//                                            .background(
//                                                color = if (tabIndex == index) actionButtonColor else Color.Gray.copy(
//                                                    alpha = 0.1f
//                                                )
//                                            ),
//                                        //  selectedContentColor = Black,
//                                        //unselectedContentColor = Color.DarkGray
//                                    )
//
//                                }
//
//                            }
//
//                        }
//                    }

                }

//                when (tabIndex) {
//                    0 -> AllTransactionHistory()
//                    1 -> SavingsTransactionHistory()
//                    2 -> LoansTransactionHistory()
//
//                }
            }
        }
    }
}