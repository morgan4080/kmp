package com.presta.customer.ui.components.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthContent(
    state: AuthStore.State,
    onBoardingState: OnBoardingStore.State,
    onEvent: (AuthStore.Intent) -> Unit,
    onOnBoardingEvent: (OnBoardingStore.Intent) -> Unit,
    navigate: () -> Unit,
    platform: Platform
) {

    println(state.cachedMemberData)

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

    LaunchedEffect(state.pinStatus, state.cachedMemberData?.firstName, state.cachedMemberData?.lastName) {
        if (
            state.pinStatus == PinStatus.SET && onBoardingState.member?.authenticationInfo?.pinStatus == PinStatus.SET
        ) {
            val title: String = if (state.cachedMemberData !== null) {
                if (state.cachedMemberData.firstName !== null && state.cachedMemberData.lastName !== null) {
                    "${state.cachedMemberData.firstName} ${state.cachedMemberData.lastName}"
                }
                "log in"
            } else {
                "log in"
            }
            onEvent(AuthStore.Intent.UpdateContext(
                context = Contexts.LOGIN,
                title = title,
                label = if (state.authUserResponse?.companyName != null ) state.authUserResponse.companyName else "",
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

                    onEvent(AuthStore.Intent.UpdateContext(
                        context = Contexts.CONFIRM_PIN,
                        title = "Confirm pin code",
                        label = state.label,
                        pinCreated = true,
                        pinConfirmed = state.pinConfirmed,
                        error = null
                    ))

                    clearPinCharacters()
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
                            label = "CREATE ACCOUNT PIN",
                            pinCreated = state.pinCreated,
                            pinConfirmed = false,
                            error = "Pin Confirmation does not match pin"
                        ))
                    }
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
                }
            }
        }
    }
    LaunchedEffect(onBoardingState.updateMemberResponse, state.cachedMemberData?.firstName, state.cachedMemberData?.lastName) {
        if (onBoardingState.updateMemberResponse !== null) {
            platform.showToast("Pin Created Successfully!")

            if (onBoardingState.member?.refId !== null && onBoardingState.member.registrationFeeInfo !== null) {
                if (state.cachedMemberData !== null && state.cachedMemberData.tenantId !== "" && state.cachedMemberData.phoneNumber !== "") {
                    onEvent(AuthStore.Intent.LoginUser(
                        phoneNumber = state.cachedMemberData.phoneNumber,
                        pin = pinInput,
                        tenantId = state.cachedMemberData.tenantId,
                        refId = onBoardingState.member.refId,
                        registrationFees = onBoardingState.member.registrationFeeInfo.registrationFees,
                        registrationFeeStatus = onBoardingState.member.registrationFeeInfo.registrationFeeStatus.toString()
                    ))
                } else if (state.phoneNumber !== null && state.cachedMemberData !== null && state.cachedMemberData.tenantId !== "") {
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
                        label = "CREATE ACCOUNT PIN",
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
                    label = "CREATE ACCOUNT PIN",
                    pinCreated = state.pinCreated,
                    pinConfirmed = false,
                    error = null
                ))
            }
        }
    }

    Scaffold (modifier = Modifier.fillMaxHeight(1f),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if ((state.isLoading || onBoardingState.isLoading) && pinInput.isEmpty()) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(25.dp).padding(end = 2.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(1f),
                ) {
                    Column (
                        modifier = Modifier.fillMaxHeight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row (
                                modifier = Modifier
                                    .padding(top = 50.dp, start = 16.dp, end = 16.dp)
                            ) {
                                Text(
                                    text = state.title.uppercase(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                    fontSize = 14.0.sp
                                )
                            }
                            Row (
                                modifier = Modifier
                                    .padding(top = 5.dp, start = 16.dp, end = 16.dp)
                            ) {
                                state.cachedMemberData.let { it1 ->
                                    Text(
                                        text = if (it1 !== null && it1.phoneNumber !== "") it1.phoneNumber else "${state.phoneNumber}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                        fontSize = 14.0.sp
                                    )
                                }
                            }
                        }

                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Column (
                                modifier = Modifier.padding(bottom = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 5.dp
                                        )
                                ) {
                                    Text(
                                        text = state.label.uppercase(),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                        fontSize = 12.0.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                AnimatedVisibility(
                                    visible = state.error == null,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 16.dp
                                            )
                                    ) {
                                        Text(
                                            text = "ENTER PIN",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                            fontSize = 12.0.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                AnimatedVisibility(
                                    visible = state.error !== null,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 16.dp
                                            )
                                    ) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(.6f),
                                            text = if (state.error !== null) state.error.uppercase() else "",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                            fontSize = 10.0.sp,
                                            color = Color.Red.copy(alpha = 0.6f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            Column (
                                modifier = Modifier.padding(top = 5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    val transition = rememberInfiniteTransition()
                                    val translateAnimation = transition.animateFloat(
                                        initialValue = 0f,
                                        targetValue = 400f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(800), repeatMode = RepeatMode.Restart
                                        )
                                    )
                                    for ((index, _) in state.inputs.withIndex()) {
                                        BasicTextField(
                                            modifier = Modifier
                                                .shadow(1.dp, CircleShape)
                                                .graphicsLayer {
                                                    // translateAnimation.value
                                                }
                                                .width(50.dp)
                                                .height(50.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                                    shape = CircleShape
                                                ).border(
                                                    border = BorderStroke(
                                                        0.2.dp,
                                                        if (state.error !== null && pinInput.isEmpty()) Color.Red else MaterialTheme.colorScheme.outline
                                                    ),
                                                    shape = CircleShape
                                                ).padding(horizontal = 10.dp),
                                            visualTransformation = PasswordVisualTransformation('\u2731'),
                                            value = pinCharList[index],
                                            textStyle = TextStyle(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
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

                                Row (
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth(),
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
                            }
                        }
                    }

                    Column (
                        modifier = Modifier.fillMaxHeight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        val rows = 3
                        val columns = 4
                        val listData = listOf(1,2,3,4,5,6,7,8,9,10,0,12)
                        FlowRow(
                            modifier = Modifier.padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            maxItemsInEachRow = rows
                        ) {
                            val itemModifier = Modifier
                                .padding(4.dp)
                                .height(80.dp)
                                .weight(1f)
                                .clip(CircleShape)
                            repeat(rows * columns) {
                                val ls = listData[it]
                                Box(
                                    modifier = itemModifier,
                                    contentAlignment = Alignment.Center
                                ) {
                                    when(ls) {
                                        10 -> {
                                            // finger print
                                        }
                                        12 -> {
                                            Button(
                                                modifier = Modifier
                                                    .padding(vertical = 5.dp, horizontal = 5.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                                ),
                                                onClick = {
                                                    when(ls) {
                                                        10 -> {
                                                            // enable finger print
                                                        }
                                                        12 -> {
                                                            pinInput = pinInput.dropLast(1)
                                                            setupPinCharacters(pinInput)
                                                        }
                                                        else -> {
                                                            if (pinInput.length <= maxChar && inputEnabled) {
                                                                builder.append(pinInput).append(ls.toString())
                                                                pinInput = builder.toString()
                                                                setupPinCharacters(pinInput)
                                                            }
                                                        }
                                                    }
                                                }
                                            ) {
                                                when(ls) {
                                                    10 -> {

                                                    }
                                                    12 -> {
                                                        Icon(
                                                            modifier = Modifier
                                                                .padding(vertical = 12.dp)
                                                                .size(25.dp)
                                                                .align(Alignment.CenterVertically),
                                                            imageVector = Icons.Outlined.Backspace,
                                                            contentDescription = "Back Space"
                                                        )
                                                    }
                                                    else -> {
                                                        Text(
                                                            textAlign = TextAlign.Center,
                                                            text = ls.toString(),
                                                            style = TextStyle(
                                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                                                fontSize = 25.sp,
                                                                textAlign = TextAlign.Center
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        else -> {
                                            Button(
                                                modifier = Modifier
                                                    .padding(vertical = 5.dp, horizontal = 5.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                                ),
                                                onClick = {
                                                    when(ls) {
                                                        10 -> {
                                                            // enable finger print
                                                        }
                                                        12 -> {
                                                            pinInput = pinInput.dropLast(1)
                                                            setupPinCharacters(pinInput)
                                                        }
                                                        else -> {
                                                            if (pinInput.length <= maxChar && inputEnabled) {
                                                                builder.append(pinInput).append(ls.toString())
                                                                pinInput = builder.toString()
                                                                setupPinCharacters(pinInput)
                                                            }
                                                        }
                                                    }
                                                }
                                            ) {
                                                when(ls) {
                                                    10 -> {

                                                    }
                                                    12 -> {
                                                        Icon(
                                                            modifier = Modifier
                                                                .padding(vertical = 12.dp)
                                                                .size(30.dp)
                                                                .align(Alignment.CenterVertically),
                                                            imageVector = Icons.Outlined.Backspace,
                                                            contentDescription = "Back Space",
                                                            tint = MaterialTheme.colorScheme.onBackground
                                                        )
                                                    }
                                                    else -> {
                                                        Text(
                                                            textAlign = TextAlign.Center,
                                                            text = ls.toString(),
                                                            style = TextStyle(
                                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                                                fontSize = 25.sp,
                                                                textAlign = TextAlign.Center
                                                            )
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
                }
            }
        }
    }
}