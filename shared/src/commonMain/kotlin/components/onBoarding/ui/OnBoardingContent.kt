package components.onBoarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import components.auth.store.AuthStore
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
import helpers.LocalSafeArea
import helpers.isPhoneNumber
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun OnBoardingContent(
    state: OnBoardingStore.State,
    authState: AuthStore.State,
    onEvent: (OnBoardingStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    navigate: (phoneNumber: String) -> Unit
) {
    val scaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val listState = rememberLazyListState()
    var phoneNumber by remember {
        mutableStateOf(TextFieldValue())
    }
    var termsAccepted by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val flagImg = "https://flagcdn.com/h24/${state.country.alpha2Code.lowercase()}.png"
    val kc = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    authState.error?.let {
        LaunchedEffect(authState.error) {
            snackbarHostState.showSnackbar(
                it
            )
            onAuthEvent(AuthStore.Intent.UpdateError(null))
        }
    }

    if (state.member !== null) {
        LaunchedEffect(state.member) {
            navigate(state.member.phoneNumber)
            onEvent(OnBoardingStore.Intent.ClearMember(null))
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.padding(LocalSafeArea.current),
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetContent = { CountriesSearch(state.countries, scaffoldState, onEvent) },
        sheetState = scaffoldState
    ) {
        Scaffold (
            modifier = Modifier.padding(LocalSafeArea.current),
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) {
            LazyColumn (
                state = listState,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 30.dp
                    )
            ) {
                item {
                    Row (
                        modifier = Modifier.padding(top = 30.dp, bottom = 50.dp),
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
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                            fontSize = 20.0.sp
                        )
                    }
                }
                item {
                    Row {
                        Text(
                            text = state.label
                                    + " "
                                    + if (state.onBoardingContext == DefaultRootComponent.OnBoardingContext.LOGIN)
                                "login"
                            else
                                "sign up",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Column (
                        modifier = Modifier.padding(vertical = 20.dp)
                    ) {
                        for (input in state.inputs) {
                            if (input.fieldType == InputFields.ORGANISATION) {
                                Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                    TextInputContainer(
                                        label = input.inputLabel,
                                        inputValue = state.organisation.tenant_name,
                                        enabled = false,
                                    )
                                }
                            }
                            if (input.fieldType == InputFields.COUNTRY) {
                                Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                    TextInputContainer(
                                        label = input.inputLabel,
                                        inputValue = state.country.name,
                                        imageUrl = flagImg,
                                        enabled = false,
                                        callback = {
                                            kc?.hide()
                                            scope.launch {
                                                scaffoldState.show()
                                            }
                                        }
                                    )
                                }
                            }
                            if (input.fieldType == InputFields.PHONE_NUMBER) {
                                Row {
                                    TextInputContainer(
                                        label = input.inputLabel,
                                        inputValue = "",
                                        callingCode = state.country.code,
                                        inputType = InputTypes.PHONE,
                                        callback = {
                                            /*scope.launch {
                                                listState.animateScrollToItem(5)
                                            }*/
                                            if (!isPhoneNumber(it) && it.isNotEmpty()) {
                                                onEvent(OnBoardingStore.Intent.UpdateError("Please enter a valid phone number"))
                                            } else {
                                                onEvent(OnBoardingStore.Intent.UpdateError(null))
                                            }

                                            phoneNumber = TextFieldValue(it)
                                        }
                                    )
                                }
                                state.error?.let {
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Row (
                        modifier = Modifier.padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = {
                                termsAccepted = !termsAccepted
                                onEvent(OnBoardingStore.Intent.UpdateError(null))
                            }
                        )
                        Text(
                            text = "Accept to Terms & Conditions and Privacy Policy.",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row {
                        ActionButton("Submit", onClickContainer = {
                            if (!termsAccepted) {
                                onEvent(OnBoardingStore.Intent.UpdateError("Kindly accept terms"))
                                return@ActionButton
                            }
                            println("phoneNumber.text")
                            println(phoneNumber.text)
                            println(isPhoneNumber(phoneNumber.text))
                            if (isPhoneNumber(phoneNumber.text) && authState.access_token !== null) {
                                onEvent(OnBoardingStore.Intent.UpdateError(null))
                                onEvent(OnBoardingStore.Intent.GetMemberDetails(
                                    token = authState.access_token,
                                    memberIdentifier = "${state.country.code}${phoneNumber.text}",
                                    identifierType = IdentifierTypes.PHONE_NUMBER
                                ))
                            } else {
                                onEvent(OnBoardingStore.Intent.UpdateError("Please enter a valid phone number"))
                            }
                        }, loading = state.isLoading || authState.isLoading)
                    }
                }

                item {
                    Row (modifier = Modifier.padding(bottom = 350.dp)) {

                    }
                }
            }
        }
    }
}