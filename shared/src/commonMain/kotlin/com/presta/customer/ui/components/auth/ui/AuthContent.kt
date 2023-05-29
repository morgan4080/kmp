package com.presta.customer.ui.components.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.Contexts
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AuthContent(
    state: AuthStore.State,
    onBoardingState: OnBoardingStore.State,
    onEvent: (AuthStore.Intent) -> Unit,
    onOnBoardingEvent: (OnBoardingStore.Intent) -> Unit,
    navigate: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    val snackbarHostState = remember { SnackbarHostState() }

    var pinInput by remember { mutableStateOf("") }

    var pinToConfirm by remember { mutableStateOf("") }
    val maxChar = state.inputs.size

    val pinCharList = remember { mutableListOf( "" ) }

    state.inputs.forEach { input ->
        pinCharList.add(input.value)
    }

    fun setupPinCharacters (e: String) {
        e.forEachIndexed { index, res ->
            pinCharList[index] = res.toString()
            pinCharList[index + 1] = ""
        }
        if (e.isEmpty()) pinCharList[0] = ""
    }

    fun clearPinCharacters () {
        pinInput.forEachIndexed { index, _ ->
            pinCharList[index] = ""
            pinCharList[index + 1] = ""
        }
        pinInput = ""
    }

    LaunchedEffect(state.pinStatus) {
        if (
            state.pinStatus == PinStatus.SET
        ) {
            onEvent(AuthStore.Intent.UpdateContext(
                context = Contexts.LOGIN,
                title = "Enter pin code to login",
                label = "Login to Presta Customer using the following pin code",
                pinCreated = true,
                pinConfirmed = true,
                error = null
            ))
        }
    }

    LaunchedEffect(pinInput.length, state.phoneNumber, onBoardingState.member) {
        if (
            pinInput.length == maxChar &&
            state.phoneNumber != null &&
            onBoardingState.member != null
        ) {
            when (state.context) {
                Contexts.CREATE_PIN -> {
                    pinToConfirm = pinInput

                    clearPinCharacters()

                    onEvent(AuthStore.Intent.UpdateContext(
                        context = Contexts.CONFIRM_PIN,
                        title = "Confirm pin code",
                        label = state.label,
                        pinCreated = true,
                        pinConfirmed = state.pinConfirmed,
                        error = null
                    ))
                }
                Contexts.CONFIRM_PIN -> {

                    if (pinToConfirm == pinInput && onBoardingState.member.refId !== null) {
                        onOnBoardingEvent(OnBoardingStore.Intent.UpdateMember(
                            token = "",
                            memberRefId = onBoardingState.member.refId,
                            pinConfirmation = pinInput,
                            tenantId = OrganisationModel.organisation.tenant_id
                        ))
                    } else {
                        onEvent(AuthStore.Intent.UpdateContext(
                            context = Contexts.CREATE_PIN,
                            title = "Create pin code",
                            label = "You'll be able to login to Presta Customer using the following pin code",
                            pinCreated = state.pinCreated,
                            pinConfirmed = false,
                            error = "Pin Confirmation does not match pin"
                        ))
                    }

                    clearPinCharacters()
                }
                Contexts.LOGIN -> {
                    if (onBoardingState.member.refId !== null && onBoardingState.member.registrationFeeInfo !== null) {
                        onEvent(AuthStore.Intent.LoginUser(
                            phoneNumber = state.phoneNumber,
                            pin = pinInput,
                            tenantId = OrganisationModel.organisation.tenant_id,
                            refId = onBoardingState.member.refId,
                            registrationFees = onBoardingState.member.registrationFeeInfo.registrationFees,
                            registrationFeeStatus = onBoardingState.member.registrationFeeInfo.registrationFeeStatus.toString()
                        ))
                    }
                    clearPinCharacters()
                }
            }
        }
    }


    LaunchedEffect(onBoardingState.updateMemberResponse) {
        if (onBoardingState.updateMemberResponse !== null) {
            snackbarHostState.showSnackbar(
                "Pin Created Successfully!"
            )
            onEvent(AuthStore.Intent.UpdateContext(
                context = Contexts.LOGIN,
                title = "Enter pin code to login",
                label = "Login to Presta Customer using the following pin code",
                pinCreated = true,
                pinConfirmed = true,
                error = null
            ))
        }
    }


    LaunchedEffect(state.phoneNumber, state.loginResponse) {
        if (state.loginResponse !== null) {
            snackbarHostState.showSnackbar(
                "Login Successful!"
            )

            if (state.phoneNumber != null) {
                navigate()
            }
        }

        if (state.phoneNumber !== null) {
            onOnBoardingEvent(OnBoardingStore.Intent.GetMemberDetails(
                token = "",
                memberIdentifier = state.phoneNumber,
                identifierType = IdentifierTypes.PHONE_NUMBER,
                tenantId = OrganisationModel.organisation.tenant_id
            ))
        }
    }


    LaunchedEffect(
        state.error,
        onBoardingState.error
    ) {

        if (onBoardingState.error !== null) {
            snackbarHostState.showSnackbar(
                onBoardingState.error
            )

            clearPinCharacters()

            onOnBoardingEvent(OnBoardingStore.Intent.UpdateError(null))

            onEvent(AuthStore.Intent.UpdateContext(
                context = Contexts.CREATE_PIN,
                title = "Create pin code",
                label = "You'll be able to login to Presta Customer using the following pin code",
                pinCreated = state.pinCreated,
                pinConfirmed = false,
                error = null
            ))
        }

        if (state.error !== null) {
            if (state.pinStatus == PinStatus.SET) {
                snackbarHostState.showSnackbar(
                    state.error
                )
            } else {
                snackbarHostState.showSnackbar(
                    state.error
                )
            }

            clearPinCharacters()

            onEvent(AuthStore.Intent.UpdateError(null))

            if (state.pinStatus == PinStatus.SET) {
                snackbarHostState.showSnackbar(
                    "Please Contact admin to reset your pin"
                )
            } else {
                onEvent(AuthStore.Intent.UpdateContext(
                    context = Contexts.CREATE_PIN,
                    title = "Create pin code",
                    label = "You'll be able to login to Presta Customer using the following pin code",
                    pinCreated = state.pinCreated,
                    pinConfirmed = false,
                    error = null
                ))
            }
        }
    }


    Scaffold (modifier = Modifier
        .fillMaxHeight(1f)
        .padding(LocalSafeArea.current),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 30.dp
                )
        ) {
            Row {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                    fontSize = 20.0.sp
                )
            }
            Row {
                Text(
                    text = state.label,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
            Row(
                modifier = Modifier.padding(top = 35.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for ((index, _) in state.inputs.withIndex()) {
                    Column (modifier = Modifier
                        .weight(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .shadow(1.dp, RoundedCornerShape(10.dp))
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.88f)
                                .height(70.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            value = pinCharList[index],
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                textAlign = TextAlign.Center
                            ),
                            onValueChange = {
                                if (it.length <= maxChar) {
                                    pinCharList[index] = it
                                }
                            },
                            enabled = false,
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box (
                                    contentAlignment = Alignment.Center
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .absoluteOffset(y = -(70).dp),
            ) {
                BasicTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .height(70.dp)
                        .alpha(0.0f),
                    value = pinInput,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            setupPinCharacters(it)
                            pinInput = it
                        }
                    },
                    enabled = true,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        innerTextField()
                    }
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = 35.dp)
                    .absoluteOffset(y = -(70).dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (state.isLoading || onBoardingState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp).padding(end = 2.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    LaunchedEffect(focusRequester) {
        try {
            focusRequester.requestFocus()
        } catch (e: IllegalStateException) {
            println(e)
        }
    }
}