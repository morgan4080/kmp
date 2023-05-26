package com.presta.customer.ui.components.onBoarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import com.presta.customer.ui.components.onBoarding.store.InputFields
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.CountriesSearch
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.TextInputContainer
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.painterResource
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.isPhoneNumber
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
    navigate: (memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean,  onBoardingContext: DefaultRootComponent.OnBoardingContext) -> Unit
) {
    val scaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val listState = rememberLazyListState()
    var phoneNumber by remember {
        mutableStateOf(TextFieldValue())
    }
    var termsAccepted by remember { mutableStateOf(false) }
    var startRegistration by remember { mutableStateOf(false) }
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

    fun startLoginJourney() {
        if (!termsAccepted) {
            onEvent(OnBoardingStore.Intent.UpdateError("Kindly accept terms"))
            return
        }

        // TODO IF NOT OPEN TO PUBLIC authState.access_token !== null
        if (isPhoneNumber(phoneNumber.text)) {
            onEvent(OnBoardingStore.Intent.UpdateError(null))
            onEvent(OnBoardingStore.Intent.GetMemberDetails(
                token = "",
                memberIdentifier = "${state.country.code}${phoneNumber.text}",
                identifierType = IdentifierTypes.PHONE_NUMBER,
                tenantId = OrganisationModel.organisation.tenant_id
            ))
        } else {
            onEvent(OnBoardingStore.Intent.UpdateError("Please enter a valid phone number"))
        }
    }

    fun startRegistrationJourney() {
        if (!termsAccepted) {
            onEvent(OnBoardingStore.Intent.UpdateError("Kindly accept terms"))
            return
        }
        // TODO authState.access_token !== null
        if (isPhoneNumber(phoneNumber.text)) {
            onEvent(OnBoardingStore.Intent.UpdateError(null))
            onEvent(OnBoardingStore.Intent.GetMemberDetails(
                token = "",
                memberIdentifier = "${state.country.code}${phoneNumber.text}",
                identifierType = IdentifierTypes.PHONE_NUMBER,
                tenantId = OrganisationModel.organisation.tenant_id
            ))
        } else {
            onEvent(OnBoardingStore.Intent.UpdateError("Please enter a valid phone number"))
        }
    }

    if (state.member !== null && state.member.refId !== null && isPhoneNumber(phoneNumber.text)) {
        LaunchedEffect(state.member) {
            navigate(
                state.member.refId,
                "${state.country.code}${phoneNumber.text}",
                true,
                true,
                DefaultRootComponent.OnBoardingContext.LOGIN
            )
            onEvent(OnBoardingStore.Intent.ClearMember(null))
        }
    }

    if (state.member !== null && state.member.refId == null) {
        LaunchedEffect(state.member) {
            snackbarHostState.showSnackbar(
                "User not registered proceeding to registration"
            )

            startRegistration = true
        }
    }

    if (
        startRegistration &&
        isPhoneNumber(phoneNumber.text) &&
        state.member !== null
    ) {
        LaunchedEffect(startRegistration) {
            startRegistration = false
            navigate(
                null,
                "${state.country.code}${phoneNumber.text}",
                false,
                false,
                DefaultRootComponent.OnBoardingContext.REGISTRATION
            )
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
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) {
            LazyColumn (
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight()
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
                            modifier = Modifier.clickable {
                                termsAccepted = !termsAccepted
                                onEvent(OnBoardingStore.Intent.UpdateError(null))
                            },
                            text = "Accept to Terms & Conditions and Privacy Policy.",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row {
                        ActionButton("Submit", onClickContainer = {
                            when(state.onBoardingContext) {
                                DefaultRootComponent.OnBoardingContext.REGISTRATION -> startRegistrationJourney()
                                DefaultRootComponent.OnBoardingContext.LOGIN -> startLoginJourney()
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