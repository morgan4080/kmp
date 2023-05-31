package com.presta.customer.ui.components.transactionHistory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.transactionHistory.store.TransactionHistoryStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.singleTransaction
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionHistoryContent(
    authState: AuthStore.State,
    state: TransactionHistoryStore.State,
    onEvent: (TransactionHistoryStore.Intent) -> Unit,
    onBack: () -> Unit,
    onMappingChange: (mapping: List<String>) -> Unit
) {
    var userInput by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }


    var tabIndex by remember { mutableStateOf(0) }

    val tabs = remember { mutableMapOf("0" to "All") }

    LaunchedEffect(state.transactionMapping) {
        if (state.transactionMapping !== null) {
            state.transactionMapping.forEach {
                tabs[it.key] = it.value
            }
        }
    }


    Surface(
        modifier = Modifier
            .background(color = MaterialTheme
                .colorScheme.background)
            .padding(LocalSafeArea.current),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier
                .fillMaxWidth()
            ) {
                NavigateBackTopBar("Transaction History", onClickContainer = {
                    onBack()
                })
            }

            Column(
                modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                        .padding(horizontal = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                ) {

                    ElevatedCard {
                        Row(
                            modifier = Modifier
                            .fillMaxWidth()
                        ) {

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
                                onValueChange = {
                                    userInput = it
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

                LazyRow (modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(LocalSafeArea.current)
                    .padding(top = 25.dp)
                ) {
                    tabs.forEach {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp)
                                    .padding(end = 2.dp)
                                    .height(43.dp)
                            ) {
                                Card (
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .fillMaxWidth()
                                        .defaultMinSize(70.dp),
                                    shape = RoundedCornerShape(70.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (tabIndex == it.key.toInt()) actionButtonColor else actionButtonColor.copy(
                                        alpha = 0.2f
                                    )),
                                ) {

                                    Tab(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        text = {
                                            Text(text = it.value,
                                                fontSize = 12.sp,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                                color = if (tabIndex == it.key.toInt()) Color.White else MaterialTheme.colorScheme.onBackground,
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        selected = tabIndex == it.key.toInt(),
                                        onClick = {
                                            tabIndex = it.key.toInt()
                                            if (state.transactionMapping !== null) {
                                                println("::::::changing")
                                                if (it.key == "0") {
                                                    onMappingChange(state.transactionMapping.keys.toList())
                                                } else {
                                                    onMappingChange(listOf(it.key))
                                                }
                                            }
                                        },
                                        selectedContentColor = Color.Black,
                                        unselectedContentColor = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text (
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = if (tabs.isNotEmpty()) {
                            "${tabs["$tabIndex"]}"
                        } else "",
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    item {
                        singleTransaction(state.transactionHistory)
                    }
                }
            }
        }
    }
}