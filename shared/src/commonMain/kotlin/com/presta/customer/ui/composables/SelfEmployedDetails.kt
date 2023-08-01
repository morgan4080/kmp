package com.presta.customer.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SelfEmployedDetails(
    onValueChanged: (String) -> Unit,
    onBusinessTypeChanged: (String) -> Unit,
    onKRAPinChanged: (String) -> Unit,
) {
    var businessData by remember { mutableStateOf(TextFieldValue("data entered")) }
    var businessType by remember { mutableStateOf(TextFieldValue("Whole sale")) }
    var kraPin by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            LiveTextContainer(
                userInput = "Nairobi",
                label = "Business Location"

            ) {
                val inputValue: String = TextFieldValue(it).text
                if (inputValue != "") {
                    if (TextFieldValue(it).text !== "") {
                        businessData = TextFieldValue(it)
                        onValueChanged(it)
                    } else {
                        //Throw error
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            LiveTextContainer(
                userInput = "",
                label = "Business Type"
            ) {
                val inputValue: String = TextFieldValue(it).text
                if (inputValue != "") {
                    if (TextFieldValue(it).text !== "") {
                        businessType = TextFieldValue(it)
                        onBusinessTypeChanged(it)
                    } else {
                        //Throw error
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            LiveTextContainer(
                userInput = "12345",
                label = "KRA pin"
            ) {
                val inputValue: String = TextFieldValue(it).text
                if (inputValue != "") {
                    if (TextFieldValue(it).text !== "") {
                        kraPin = TextFieldValue(it)
                        onKRAPinChanged(it)
                    } else {
                        //Throw error
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            ActionButton(
                label = "Submit", onClickContainer = {
                    //Suspend the  view
                    onValueChanged(businessData.text)
                    onBusinessTypeChanged(businessType.text)
                    onKRAPinChanged(kraPin.text)
                },
                enabled = true
            )
        }
    }
}