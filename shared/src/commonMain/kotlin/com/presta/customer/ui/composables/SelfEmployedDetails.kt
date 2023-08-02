package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore

@Composable
fun SelfEmployedDetails(
    signHomeState: SignHomeStore.State,
    onBusinessLocationChanged: (String) -> Unit,
    onBusinessTypeChanged: (String) -> Unit,
    onKRAPinChanged: (String) -> Unit,
) {
    var businessLocationData by remember { mutableStateOf("") }
    var businessTypeData by remember { mutableStateOf("") }
    var kraPinData by remember { mutableStateOf("") }
    signHomeState.prestaTenantByPhoneNumber?.details?.map { it ->
        if (it.key.contains("businessL")) {
            businessLocationData = it.value.value.toString()
        }
        if (it.key.contains("businessT")) {
            businessTypeData = it.value.value.toString()
        }
        if (it.key.contains("kra")) {
            kraPinData = it.value.value.toString()
        }
    }
    var businessLocation by remember { mutableStateOf(TextFieldValue(businessLocationData)) }
    var kraPin by remember { mutableStateOf(TextFieldValue(kraPinData)) }
    var businessType by remember { mutableStateOf(TextFieldValue(businessTypeData)) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (signHomeState.prestaTenantByPhoneNumber?.refId == null) {
            LazyColumn() {
                items(6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp)
                            .background(color = MaterialTheme.colorScheme.background),
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
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
                }
            }
        } else {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                LiveTextContainer(
                    userInput = businessLocation.text,
                    label = "Business Location"

                ) {
                    val inputValue: String = TextFieldValue(it).text
                    if (inputValue != "") {
                        if (TextFieldValue(it).text !== "") {
                            businessLocation = TextFieldValue(it)
                            onBusinessLocationChanged(it)
                        } else {
                            //Throw error
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                LiveTextContainer(
                    userInput = businessType.text,
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
                    userInput = kraPin.text,
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
                        onBusinessLocationChanged(businessLocation.text)
                        onBusinessTypeChanged(businessType.text)
                        onKRAPinChanged(kraPin.text)
                    },
                    enabled = true
                )
            }
        }
    }


}