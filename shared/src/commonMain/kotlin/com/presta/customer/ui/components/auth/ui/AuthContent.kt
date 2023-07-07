package com.presta.customer.ui.components.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.Contexts
import com.presta.customer.ui.components.onBoarding.store.IdentifierTypes
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthContent(
    state: AuthStore.State,
    onBoardingState: OnBoardingStore.State,
    onEvent: (AuthStore.Intent) -> Unit,
    onOnBoardingEvent: (OnBoardingStore.Intent) -> Unit,
    navigate: () -> Unit,
    platform: Platform
) {
    val focusRequester = remember { FocusRequester() }

    val snackbarHostState = remember { SnackbarHostState() }

    var inputEnabled = true

    var pinInput by remember { mutableStateOf("") }

    var pinToConfirm by remember { mutableStateOf("") }

    val maxChar = state.inputs.size

    val pinCharList = remember { mutableListOf( "" ) }

    val builder = StringBuilder()

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
            state.pinStatus == PinStatus.SET && onBoardingState.member?.authenticationInfo?.pinStatus == PinStatus.SET
        ) {
            onEvent(AuthStore.Intent.UpdateContext(
                context = Contexts.LOGIN,
                title = "Enter pin code to login",
                label = if (state.authUserResponse?.companyName!=null ) "Login to" + state.authUserResponse.companyName + "App using the following pin code" else "",
                pinCreated = true,
                pinConfirmed = true,
                error = null
            ))
        }
    }

    LaunchedEffect(pinInput.length, state.phoneNumber, onBoardingState.member, state.cachedMemberData) {
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

                        if (state.cachedMemberData !== null && state.cachedMemberData.tenantId !== "") {
                            onOnBoardingEvent(OnBoardingStore.Intent.UpdateMember(
                                token = "",
                                memberRefId = onBoardingState.member.refId,
                                pinConfirmation = pinInput,
                                tenantId = state.cachedMemberData.tenantId
                            ))
                        }
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
                        if (state.cachedMemberData !== null && state.cachedMemberData.tenantId !== "") {
                            onEvent(AuthStore.Intent.LoginUser(
                                phoneNumber = state.phoneNumber,
                                pin = pinInput,
                                tenantId = state.cachedMemberData.tenantId,
                                refId = onBoardingState.member.refId,
                                registrationFees = onBoardingState.member.registrationFeeInfo.registrationFees,
                                registrationFeeStatus = onBoardingState.member.registrationFeeInfo.registrationFeeStatus.toString()
                            ))
                        }

                    }
                    clearPinCharacters()
                }
            }
        }
    }


    LaunchedEffect(onBoardingState.updateMemberResponse) {
        if (onBoardingState.updateMemberResponse !== null) {
            platform.showToast("Pin Created Successfully!")
            onEvent(AuthStore.Intent.UpdateContext(
                context = Contexts.LOGIN,
                title = "Enter pin code to login",
                label = "Login to" +state.authUserResponse?.companyName+"App using the following pin code",
                pinCreated = true,
                pinConfirmed = true,
                error = null
            ))
        }
    }


    LaunchedEffect(state.phoneNumber, state.loginResponse, state.cachedMemberData) {
        if (state.loginResponse !== null && state.phoneNumber != null) {
            inputEnabled = false
            platform.showToast("Login Successful!")
            navigate()
        }

        if (state.phoneNumber !== null) {
            if (state.cachedMemberData !== null && state.cachedMemberData.tenantId !== "") {

                onOnBoardingEvent(OnBoardingStore.Intent.GetMemberDetails(
                    token = "",
                    memberIdentifier = state.phoneNumber,
                    identifierType = IdentifierTypes.PHONE_NUMBER,
                    tenantId = state.cachedMemberData.tenantId
                ))

            }
        }
    }


    LaunchedEffect(
        state.error,
        onBoardingState.error
    ) {
        if (onBoardingState.error !== null) {
            clearPinCharacters()

            if (state.pinStatus !== PinStatus.SET) {
                onEvent(
                    AuthStore.Intent.UpdateContext(
                        context = Contexts.CREATE_PIN,
                        title = "Create pin code",
                        label = "You'll be able to login to Presta Customer using the following pin code",
                        pinCreated = state.pinCreated,
                        pinConfirmed = false,
                        error = null
                    )
                )
            }
        }

        if (state.error !== null) {

            clearPinCharacters()

            if (state.pinStatus == PinStatus.SET) {
                platform.showToast("Please Contact Admin")
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
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
        ) {
            Row (
                modifier = Modifier
                    .padding(top = 30.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                    fontSize = 20.0.sp
                )
            }

            Row (
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp)
            ) {
                Text(
                    text = state.label,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }

            state.error?.let { it1 ->
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                ) {
                    Text(
                        text = it1,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
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
                            visualTransformation = if (state.pinStatus == PinStatus.SET) PasswordVisualTransformation('\u2731') else VisualTransformation.None,
                            value = pinCharList[index],
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                textAlign = TextAlign.Center
                            ),
                            onValueChange = { value ->
                                if (value.length <= maxChar) {
                                    pinCharList[index] = value
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
                    .padding(horizontal = 16.dp)
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
                    onValueChange = { value ->
                        if (value.length <= maxChar) {
                            setupPinCharacters(value)
                            pinInput = value
                        }
                    },
                    enabled = false,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        innerTextField()
                    }
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = 25.dp)
                    .padding(horizontal = 16.dp)
                    .absoluteOffset(y = -(50).dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp).padding(end = 2.dp).alpha(if (state.isLoading || onBoardingState.isLoading || !inputEnabled) 1f else 0.0f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row  (
                modifier = Modifier.absoluteOffset(y = -(50).dp),
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.Top,
                    contentPadding = PaddingValues(
                        start = 2.dp,
                        top = 5.dp,
                        end = 2.dp,
                        bottom = 5.dp
                    )
                ) {
                    items(listOf(1,2,3,4,5,6,7,8,9,10,0,12)) {
                        Button(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onBackground,
                            ),
                            onClick = {
                                when(it) {
                                    10 -> {
                                        // enable finger print
                                    }
                                    12 -> {
                                        pinInput = pinInput.dropLast(1)
                                        setupPinCharacters(pinInput)
                                    }
                                    else -> {
                                        if (pinInput.length <= maxChar && inputEnabled) {
                                            builder.append(pinInput).append(it.toString())
                                            pinInput = builder.toString()
                                            setupPinCharacters(pinInput)
                                        }
                                    }
                                }
                            }
                        ) {
                            when(it) {
                                10 -> {
                                    /*Icon(
                                        modifier = Modifier
                                            .padding(vertical = 12.dp)
                                            .size(30.dp)
                                            .align(Alignment.CenterVertically),
                                        imageVector = Icons.Filled.Fingerprint,
                                        contentDescription = "Finger Print",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )*/
                                }
                                12 -> {
                                    Icon(
                                        modifier = Modifier
                                            .padding(vertical = 12.dp)
                                            .size(30.dp)
                                            .align(Alignment.CenterVertically),
                                        imageVector = Icons.Outlined.Backspace,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                else -> {
                                    Text(
                                        textAlign = TextAlign.Center,
                                        text = it.toString(),
                                        style = TextStyle(
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                            letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                            textAlign = TextAlign.Center
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}