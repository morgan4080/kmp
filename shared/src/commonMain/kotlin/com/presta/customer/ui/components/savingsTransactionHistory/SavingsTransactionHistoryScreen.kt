package com.presta.customer.ui.components.savingsTransactionHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.SavingsTransactionHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsTransactionHistoryScreen(component: SavingsTransactionHistoryComponent) {

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
                NavigateBackTopBar("Savings Transaction History", onClickContainer = {

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
                    ElevatedCard {
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

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SavingsTransactionHistory()
                    }
                }
            }
        }
    }
}