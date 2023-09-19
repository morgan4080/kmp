package com.presta.customer.ui.composables

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore

@Composable
fun SelfEmployedDetails(
    signHomeState: SignHomeStore.State,
    authState: AuthStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
    onClickSubmit: (errorrOccured: Boolean) -> Unit,
) {
    var employernameLive by remember { mutableStateOf("") }
    var employmentNumberLive by remember { mutableStateOf("") }
    var grossSalaryDataLive by remember { mutableStateOf("") }
    var netSalaryLive by remember { mutableStateOf("") }
    var kraPinLive by remember { mutableStateOf("") }
    var businessLocationLive by remember { mutableStateOf("") }
    var businessTypeLive by remember { mutableStateOf("") }
    val userDetailsMap = mutableMapOf<String, String>()
    val pattern = remember { Regex("^\\d+\$") }
    val numberTextPattern = remember { Regex("^[\\p{L}\\d ]+$") }
    var hasError by remember { mutableStateOf(false) }

    signHomeState.prestaTenantByPhoneNumber?.details?.map { it ->
        if (it.key.contains("employer")) {
            employernameLive = it.value.value.toString()
        }
        if (it.key.contains("gross")) {
            grossSalaryDataLive = it.value.value.toString()
        }
        if (it.key.contains("net")) {
            netSalaryLive = it.value.value.toString()
        }
        if (it.key.contains("kra")) {
            kraPinLive = it.value.value.toString()
        }
        if (it.key.contains("employment")) {
            employmentNumberLive = it.value.value.toString()
        }
        if (it.key.contains("businessLocation")) {
            businessLocationLive = it.value.value.toString()
        }
        if (it.key.contains("businessType")) {
            businessTypeLive = it.value.value.toString()
        }
    }
    var businessLocation by remember { mutableStateOf(TextFieldValue(businessLocationLive)) }
    var kraPin by remember { mutableStateOf(TextFieldValue(kraPinLive)) }
    var businessType by remember { mutableStateOf(TextFieldValue(businessTypeLive)) }
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
                    label = "Business Location",
                    keyboardType = KeyboardType.Text,
                    pattern = numberTextPattern,
                    callback2 = { errorcalled ->
                        hasError = errorcalled
                    }

                ) {
                    val inputValue: String = TextFieldValue(it).text
                    if (inputValue != "") {
                        if (TextFieldValue(it).text !== "") {
                            businessLocation = TextFieldValue(it)
                        } else {
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                LiveTextContainer(
                    userInput = businessType.text,
                    label = "Business Type",
                    keyboardType = KeyboardType.Text,
                    pattern = numberTextPattern,
                    callback2 = { errorcalled ->
                        hasError = errorcalled
                    }
                ) {
                    val inputValue: String = TextFieldValue(it).text
                    if (inputValue != "") {
                        if (TextFieldValue(it).text !== "") {
                            businessType = TextFieldValue(it)
                        } else {

                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                LiveTextContainer(
                    userInput = kraPinLive,
                    label = "KRA pin",
                    keyboardType = KeyboardType.Text,
                    pattern = numberTextPattern,
                    callback2 = { errorcalled ->
                        hasError = errorcalled
                    }
                ) {
                    val inputValue: String = TextFieldValue(it).text
                    if (inputValue != "") {
                        if (TextFieldValue(it).text !== "") {
                            kraPin = TextFieldValue(it)

                        } else {

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
                    label = "Submit",
                    onClickContainer = {
                        if (!hasError) {
                            userDetailsMap["employer"] = employernameLive
                            userDetailsMap["employmentNumber"] = employmentNumberLive
                            userDetailsMap["grossSalary"] = grossSalaryDataLive
                            userDetailsMap["netSalary"] = netSalaryLive
                            userDetailsMap["kraPin"] =
                                if (kraPin.text != "") kraPin.text else kraPinLive
                            userDetailsMap["businessLocation"] =
                                if (businessLocation.text != "") businessLocation.text else businessLocationLive
                            userDetailsMap["businessType"] =
                                if (businessType.text != "") businessType.text else businessTypeLive
                            authState.cachedMemberData?.let {
                                SignHomeStore.Intent.UpdatePrestaTenantDetails(
                                    token = it.accessToken,
                                    memberRefId = signHomeState.prestaTenantByPhoneNumber.refId,
                                    details = userDetailsMap
                                )
                            }?.let {
                                onProfileEvent(
                                    it
                                )
                            }
                            onClickSubmit(false)

                        }

                    },
                    enabled = true,
                    loading = signHomeState.isLoading
                )
            }
        }
    }


}