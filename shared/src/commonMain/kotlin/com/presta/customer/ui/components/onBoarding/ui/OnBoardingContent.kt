package com.presta.customer.ui.components.onBoarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.onBoarding.model.SelfRegistrationStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.onBoarding.OnBoardingComponent
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import com.presta.customer.ui.components.onBoarding.store.InputFields
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.CountriesSearch
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.isPhoneNumber
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun OnBoardingContent(
    state: OnBoardingStore.State,
    onEvent: (OnBoardingStore.Intent) -> Unit,
    navigate: (
        memberRefId: String?,
        phoneNumber: String,
        isTermsAccepted: Boolean,
        isActive: Boolean,
        onBoardingContext: DefaultRootComponent.OnBoardingContext,
        pinStatus: PinStatus?
    ) -> Unit,
    component: OnBoardingComponent

) {
    val scaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
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

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("Accept to ")
        }
        pushStringAnnotation(tag = "terms", annotation =  "https://support.presta.co.ke/portal/en/kb/articles/privacy-policy")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("Terms & Conditions ")
        }
        pop()
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("and ")
        }
        pushStringAnnotation(tag = "policy", annotation = "https://lending.presta.co.ke/kopesha/legal/presta-privacy-policy.html")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("Privacy Policy")
        }
        pop()
    }

    fun startLoginJourney() {
        if (!termsAccepted) {
            onEvent(OnBoardingStore.Intent.UpdateError("Kindly accept terms"))
            return
        }

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

        if (isPhoneNumber(phoneNumber.text)) {
            onEvent(OnBoardingStore.Intent.UpdateError(null))
            onEvent(
                OnBoardingStore.Intent.GetMemberDetails(
                    token = "",
                    memberIdentifier = "${state.country.code}${phoneNumber.text}",
                    identifierType = IdentifierTypes.PHONE_NUMBER,
                    tenantId = OrganisationModel.organisation.tenant_id
                )
            )
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
                DefaultRootComponent.OnBoardingContext.LOGIN,
                state.member.authenticationInfo.pinStatus
            )
            onEvent(OnBoardingStore.Intent.ClearMember(null))
        }
    }
    LaunchedEffect(state.member) {
        if (state.member !== null && state.member.refId == null && !startRegistration) {
            startRegistration = when (state.member.accountInfo.selfRegistrationStatus) {
                SelfRegistrationStatus.ENABLED -> {
                    snackbarHostState.showSnackbar(
                        "User not registered, starting registration process."
                    )
                    true
                }

                SelfRegistrationStatus.DISABLED -> {
                    snackbarHostState.showSnackbar(
                        "User not registered"
                    )
                    false
                }
            }
        }
    }
    if (
        startRegistration &&
        isPhoneNumber(phoneNumber.text) &&
        state.member !== null &&
        state.member.refId == null
    ) {
        LaunchedEffect(startRegistration) {
            startRegistration = false
            navigate(
                null,
                "${state.country.code}${phoneNumber.text}",
                false,
                false,
                DefaultRootComponent.OnBoardingContext.REGISTRATION,
                state.member.authenticationInfo.pinStatus
            )
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        when(isSystemInDarkTheme()) {
                            false -> Image(
                                painter = painterResource(state.organisation.logo),
                                contentDescription = "Logo",
                                modifier = Modifier.size(150.dp)
                            )
                            true -> Image(
                                painter = painterResource(state.organisation.logodark),
                                contentDescription = "Logo",
                                modifier = Modifier.size(150.dp)
                            )
                        }
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
                        ClickableText(text = annotatedString, style = MaterialTheme.typography.bodySmall, onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset).firstOrNull()?.let {
                                //Test  the logged url
                                component.platform.openUrl("https://support.presta.co.ke/portal/en/kb/articles/privacy-policy")
                            }
                            annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset).firstOrNull()?.let {
                                component.platform.openUrl("https://lending.presta.co.ke/kopesha/legal/presta-privacy-policy.html")
                            }
                            onEvent(OnBoardingStore.Intent.UpdateError(null))

                        })
                    }
                }
                item {
                    Row {
                        ActionButton("Submit", onClickContainer = {
                            when(state.onBoardingContext) {
                                DefaultRootComponent.OnBoardingContext.REGISTRATION -> startRegistrationJourney()
                                DefaultRootComponent.OnBoardingContext.LOGIN -> startLoginJourney()
                            }
                        }, loading = state.isLoading)
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