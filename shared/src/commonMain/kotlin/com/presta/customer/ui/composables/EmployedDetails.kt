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
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore

@Composable
fun EmployedDetails(
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
    onClickContainer: () -> Unit
) {
    var lastName by remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            signHomeState.prestaTenantByPhoneNumber?.let {
                LiveTextContainer(
                    userInput = it.firstName,
                    label = "firstname"
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
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            signHomeState.prestaTenantByPhoneNumber?.let {
                LiveTextContainer(
                    userInput = it.memberNumber,
                    label = "Employment Number"
                )
            }
        }
        signHomeState.prestaTenantByPhoneNumber?.details?.map { it ->
            Row(modifier = Modifier.fillMaxWidth()) {
                it.value.value?.let { it1 ->
                    LiveTextContainer(
                        userInput = it1,
                        label = it.key
                    )
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
                onClickContainer = onClickContainer,
                enabled = true
            )
        }
    }
}