package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore

data class MultipleVariables(val grossSalary: String, val variable2: String, val variable3: Boolean)

@Composable
fun EmployedDetails(
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
    onClickSubmit: () -> Unit,
    component: AddGuarantorsComponent,
) {
    var employernameLive by remember { mutableStateOf("") }
    var employmentNumberLive by remember { mutableStateOf("") }
    var grossSalaryDataLive by remember { mutableStateOf("") }
    var netSalaryLive by remember { mutableStateOf("") }
    var kraPinLive by remember { mutableStateOf("") }
    var businessLocationLive by remember { mutableStateOf("") }
    var businessTypeLive by remember { mutableStateOf("") }
    var employer by remember { mutableStateOf(TextFieldValue()) }
    var employMentNumber by remember { mutableStateOf(TextFieldValue()) }
    var grossSalary by remember { mutableStateOf(TextFieldValue()) }
    var netSalary by remember { mutableStateOf(TextFieldValue()) }
    var kraPin by remember { mutableStateOf(TextFieldValue()) }
    val userDetailsMap = mutableMapOf<String, String>()
    val pattern = remember { Regex("^\\d+\$") }
    val numberTextPattern = remember { Regex("^[\\p{L}\\d ]+$") }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (signHomeState.prestaTenantByPhoneNumber?.refId == null) {
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
        } else {
            signHomeState.prestaTenantByPhoneNumber.details?.map { it ->
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
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    LiveTextContainer(
                        userInput = employernameLive,
                        label = "Employer",
                        keyboardType = KeyboardType.Text,
                        pattern = numberTextPattern
                    ) {
                        val inputValue: String = TextFieldValue(it).text
                        if (inputValue != "") {
                            if (TextFieldValue(it).text !== "") {
                                employer = TextFieldValue(it)
                            } else {
                                //Throw error
                            }
                        }
                    }
                }
            }
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LiveTextContainer(
                        userInput = employmentNumberLive,
                        label = "Employment Number",
                        keyboardType = KeyboardType.Text,
                        pattern = numberTextPattern
                    ) {
                        val inputValue: String = TextFieldValue(it).text
                        if (inputValue != "") {
                            if (TextFieldValue(it).text !== "") {
                                employMentNumber = TextFieldValue(it)
                            } else {
                                //Throw error
                            }
                        }
                    }
                }
            }
            item {

                Row(modifier = Modifier.fillMaxWidth()) {
                    LiveTextContainer(
                        userInput = grossSalaryDataLive,
                        label = "Gross Salary",
                        keyboardType = KeyboardType.Number,
                        pattern = pattern
                    ) {
                        val inputValue: String = TextFieldValue(it).text
                        if (inputValue != "") {
                            if (TextFieldValue(it).text !== "") {
                                grossSalary = TextFieldValue(it)
                            } else {
                                //Throw error
                            }
                        }
                    }

                }
            }
            item {

                Row(modifier = Modifier.fillMaxWidth()) {
                    LiveTextContainer(
                        userInput = netSalaryLive,
                        label = "Net Salary",
                        keyboardType = KeyboardType.Number,
                        pattern = pattern
                    ) {
                        val inputValue: String = TextFieldValue(it).text
                        if (inputValue != "") {
                            if (TextFieldValue(it).text !== "") {
                                netSalary = TextFieldValue(it)
                            } else {
                                //Throw error
                            }
                        }
                    }
                }

            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    LiveTextContainer(
                        userInput = kraPinLive,
                        label = "KRA Pin",
                        keyboardType = KeyboardType.Text,
                        pattern = numberTextPattern
                    ) {
                        val inputValue: String = TextFieldValue(it).text
                        if (inputValue != "") {
                            if (TextFieldValue(it).text !== "") {
                                kraPin = TextFieldValue(it)
                            } else {
                                //Throw error
                            }
                        }
                    }
                }
            }
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                ) {
                    ActionButton(
                        label = "Submit",
                        onClickContainer = {
                            userDetailsMap["employer"] =
                                if (employer.text != "") employer.text else employernameLive
                            userDetailsMap["employmentNumber"] =
                                if (employMentNumber.text != "") employMentNumber.text else employmentNumberLive
                            userDetailsMap["grossSalary"] =
                                if (grossSalary.text != "") grossSalary.text else grossSalaryDataLive
                            userDetailsMap["netSalary"] =
                                if (netSalary.text != "") netSalary.text else netSalaryLive
                            userDetailsMap["kraPin"] =
                                if (kraPin.text != "") kraPin.text else kraPinLive
                            userDetailsMap["businessLocation"] = businessLocationLive
                            userDetailsMap["businessType"] = businessTypeLive
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
                            //Todo-----show failed or successful update
                            onClickSubmit()
                        },
                        enabled = true,
                        loading = signHomeState.isLoading
                    )
                }
            }
        }
    }
}