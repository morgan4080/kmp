package com.presta.customer.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun UserInformation() {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        var lastName by remember { mutableStateOf(TextFieldValue()) }
        LazyColumn(){
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        LiveTextContainer(
                            userInput = "Murungi",
                            label = "first Name"
                        ) {
                            val inputValue: String = TextFieldValue(it).text
                            if (inputValue != "") {
                                if (TextFieldValue(it).text !== "") {
                                    lastName = TextFieldValue(it)

                                } else {

                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "Mutugi",
                            label = "last name"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "200",
                            label = "ID number"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "0796387377",
                            label = "Phone Number"
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LiveTextContainer(
                            userInput = "morganmurungi@live.com",
                            label = "email"
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        ModeSelectionCard(label = "Disbursement Mode", onClickContainer = {
                            //launch POP Up

                        })
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        ModeSelectionCard(label = "Repayment Mode", onClickContainer = {
                            //launch POP Up
                        })
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        ActionButton(
                            label = "Submit  Loan Request", onClickContainer = {
                                println("Name is :::::::")
                                println(lastName.text)
                            },
                            enabled = true
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    }
}