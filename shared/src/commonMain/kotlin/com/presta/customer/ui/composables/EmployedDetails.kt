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
    //onKRAPinChanged: (String) -> Unit,
    onClickSubmit: (MultipleVariables) -> Unit,
) {
    var lastName by remember { mutableStateOf(TextFieldValue()) }
    var grossSalary by remember { mutableStateOf(TextFieldValue()) }
    var employer by remember { mutableStateOf("") }
    var grossSalaryData by remember { mutableStateOf("") }
    var netSalary by remember { mutableStateOf("") }
    var kraPin by remember { mutableStateOf("") }
    var employmentNumber by remember { mutableStateOf("") }
    val liveName: String? = signHomeState.prestaTenantByPhoneNumber?.firstName
    val employeename by remember { mutableStateOf(TextFieldValue(liveName.toString())) }
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
                    employer = it.value.value.toString()
                }
                if (it.key.contains("GROSS")) {
                    grossSalaryData = it.value.value.toString()
                }
                if (it.key.contains("net")) {
                    netSalary = it.value.value.toString()
                }
                if (it.key.contains("kra")) {
                    kraPin = it.value.value.toString()
                }
                if (it.key.contains("employment")) {
                    employmentNumber = it.value.value.toString()
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    LiveTextContainer(
                        userInput = employer,
                        label = "Employer"
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LiveTextContainer(
                        userInput = employmentNumber,
                        label = "Employment Number"
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
                        userInput = grossSalaryData,
                        label = "Gross Salary"
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
                        userInput = netSalary,
                        label = "Net Salary"
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
                        userInput = kraPin,
                        label = "KRA Pin"
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                ) {
                    val multipleVariables =
                        MultipleVariables(grossSalary.text, employeename.text, true)
                    ActionButton(
                        label = "Submit",
                        onClickContainer = {
                            onClickSubmit(multipleVariables)
                            println("test groos $grossSalaryData")
                        },
                        enabled = true
                    )
                }
            }
        }
    }
}