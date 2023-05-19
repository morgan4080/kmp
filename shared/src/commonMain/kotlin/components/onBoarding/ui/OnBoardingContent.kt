package components.onBoarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import components.auth.store.AuthStore
import components.countries.Country
import components.onBoarding.OnBoardingComponent
import components.onBoarding.store.IdentifierTypes
import components.onBoarding.store.InputFields
import components.onBoarding.store.OnBoardingStore
import composables.ActionButton
import composables.InputTypes
import composables.TextInputContainer
import dev.icerock.moko.resources.compose.fontFamilyResource
import helpers.LocalSafeArea
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingContent(
    state: OnBoardingStore.State,
    authState: AuthStore.State,
    onEvent: (OnBoardingStore.Intent) -> Unit
) {
    val country: Country = Json.decodeFromString(state.country)
    var phoneNumber by remember { mutableStateOf("") }

    Scaffold (modifier = Modifier.fillMaxHeight(1f).padding(LocalSafeArea.current)) {
        LazyColumn (
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp)
        ) {
            item {
                Row {
                    Text(
                        text = state.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
            }
            item {
                Row {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.displaySmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )
                }
            }
            item {
                Column (
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    loop@ for (input in state.inputs) {
                        if (input.fieldType == InputFields.ORGANISATION) {
                            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                TextInputContainer(
                                    label = input.inputLabel,
                                    inputValue = state.organisation.tenant_name,
                                    enabled = false,
                                )
                            }
                            break@loop
                        }
                    }
                    Row(modifier = Modifier.padding(bottom = 20.dp).fillMaxWidth()) {
                        loop@ for (input in state.inputs) {
                            if (input.fieldType == InputFields.ORGANISATION) {
                                continue@loop
                            }

                            if (input.fieldType == InputFields.COUNTRY) {
                                Column (modifier = Modifier.fillMaxWidth(0.36f).padding(end = 10.dp)) {
                                    TextInputContainer(
                                        label = input.inputLabel,
                                        inputValue = "+${country.code}",
                                        imageUrl = "https://flagcdn.com/28x21/${country.alpha2Code.lowercase()}.png",
                                        enabled = false
                                    )
                                }
                            }

                            if (input.fieldType == InputFields.PHONE_NUMBER) {
                                Column (modifier = Modifier.fillMaxWidth()) {
                                    TextInputContainer(
                                        label = input.inputLabel,
                                        inputValue = "",
                                        inputType = InputTypes.NUMBER,
                                        callback = {
                                            phoneNumber = it
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                Row (modifier = Modifier.padding(bottom = 350.dp)) {
                    ActionButton("Continue", onClickContainer = {
                        onEvent(OnBoardingStore.Intent.GetMemberDetails(
                            token = authState.access_token,
                            memberIdentifier = phoneNumber,
                            identifierType = IdentifierTypes.PHONE_NUMBER
                        ))
                    }, loading = state.isLoading)
                }
            }
        }
    }
}