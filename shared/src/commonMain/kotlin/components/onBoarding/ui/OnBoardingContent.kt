package components.onBoarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import components.auth.store.AuthStore
import components.countries.Country
import components.onBoarding.store.IdentifierTypes
import components.onBoarding.store.InputFields
import components.onBoarding.store.OnBoardingStore
import components.root.DefaultRootComponent
import composables.ActionButton
import composables.CountriesSearch
import composables.InputTypes
import composables.TextInputContainer
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.readTextAsState
import helpers.LocalSafeArea
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingContent(
    state: OnBoardingStore.State,
    authState: AuthStore.State,
    onEvent: (OnBoardingStore.Intent) -> Unit
) {
    val country: Country = Json.decodeFromString(state.country)
    var phoneNumber by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var searchingInput by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val countriesJson: String? by state.countriesJSON.readTextAsState()
    var countriesObject: List<Country> = remember { mutableListOf(
        Country("Kenya", "254", "KE", "404"),
        Country("Tanzania", "255", "TZ", "834")
    )}

    if (countriesJson != null) {
        countriesObject = Json.decodeFromString("$countriesJson")
    }

    LaunchedEffect(searchingInput, countriesJson) {
        kotlin.run {
            countriesObject = Json.decodeFromString("$countriesJson")
            countriesObject = countriesObject.filter { country: Country -> country.name.lowercase().indexOf(searchingInput) > -1 }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxHeight().padding(LocalSafeArea.current),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CountriesSearch(
                countries = countriesObject
            )
        },
        sheetShape =
    ) {
        LazyColumn (
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 30.dp)
        ) {
            item {
                Row (
                    modifier = Modifier.padding(72.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val painter: Painter = painterResource(state.organisation.logo)

                    Image(
                        painter = painter,
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                Row {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                    )
                }
            }
            item {
                Row {
                    Text(
                        text = state.label + " " + if (state.onBoardingContext === DefaultRootComponent.OnBoardingContext.LOGIN) "login" else "sign up",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
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
                                        enabled = false,
                                        callback = {
                                            scope.launch {
                                                if (scaffoldState.bottomSheetState.isCollapsed) {
                                                    scaffoldState.bottomSheetState.expand()
                                                } else {
                                                    scaffoldState.bottomSheetState.collapse()
                                                }
                                            }
                                        }
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
                Row (
                    modifier = Modifier.padding(bottom = 50.dp),
                    verticalAlignment = Alignment.Bottom
                ){
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = {
                            termsAccepted = !termsAccepted
                        }
                    )
                    Text(
                        text = "By clicking the box yo agree to Terms and Conditions & Privacy Policy.",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                }
            }
            item {
                Row (modifier = Modifier.padding(bottom = 250.dp)) {
                    ActionButton("Submit", onClickContainer = {
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