package com.presta.customer.ui.components.otp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.Durations
import com.presta.customer.MR
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.otp.store.OtpStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpContent(
    state: OtpStore.State,
    authState: AuthStore.State,
    onEvent: (OtpStore.Intent) -> Unit,
    navigate: (
        memberRefId: String?,
        phoneNumber: String,
        isTermsAccepted: Boolean,
        isActive: Boolean,
        onBoardingContext: DefaultRootComponent.OnBoardingContext,
        pinStatus: PinStatus?
    ) -> Unit,
    platform: Platform
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val focusRequester = remember { FocusRequester() }

    var otpInput by remember { mutableStateOf("") }

    val builder = StringBuilder()

    val maxChar = state.inputs.size

    var inputEnabled = true

    val otpCharList = remember { mutableListOf( "" ) }

    state.inputs.forEach { input ->
        otpCharList.add(input.value)
    }

    fun setupOtpCharacters (e: String) {
        e.forEachIndexed { index, res ->
            otpCharList[index] = res.toString()
            otpCharList[index + 1] = ""
        }
        if (e.isEmpty()) otpCharList[0] = ""
    }

    fun emptyOtpCharacters () {
        otpInput = ""
        otpCharList.clear()
        state.inputs.forEach { input ->
            otpCharList.add(input.value)
        }
    }


    LaunchedEffect(state.phone_number) {
        if (
            state.phone_number !== null
        ) {
            if ( OrganisationModel.organisation.tenant_id!=null){
                onEvent(
                    OtpStore.Intent.RequestOTP(
                        token = "",
                        phoneNumber = state.phone_number,
                        tenantId = OrganisationModel.organisation.tenant_id!!
                    )
                )
            }
        }
    }


    LaunchedEffect(state.otpRequestData) {
        if (state.otpRequestData !== null) {
            platform.showToast("OTP Sent!", Durations.SHORT)
        }
    }

    LaunchedEffect(state.error) {
        if (state.error !== null) {
            snackBarHostState.showSnackbar(
                state.error
            )

            onEvent(OtpStore.Intent.ClearError)
        }
    }

    LaunchedEffect(otpInput) {
        if (state.otpRequestData !== null && otpInput.length == maxChar) {
            if (OrganisationModel.organisation.tenant_id!=null){
                onEvent(OtpStore.Intent.VerifyOTP(
                    token = "",
                    requestMapper = state.otpRequestData.requestMapper,
                    otp = otpInput,
                    tenantId = OrganisationModel.organisation.tenant_id!!
                ))
            }

        }
    }


    LaunchedEffect(state.otpVerificationData) {
        if (
            state.otpVerificationData !== null &&
            state.phone_number !== null &&
            state.isTermsAccepted !== null &&
            state.isActive !== null
        ) {
            platform.showToast(state.otpVerificationData.message, Durations.SHORT)
            if (state.otpVerificationData.validated) {
                inputEnabled = false
                navigate(
                    state.memberRefId,
                    state.phone_number,
                    state.isTermsAccepted,
                    state.isActive,
                    state.onBoardingContext,
                    state.pinStatus
                )
                onEvent(OtpStore.Intent.ClearOtpVerificationData)
            } else {
                emptyOtpCharacters()
            }
        }
    }

    LaunchedEffect(platform.otpCode) {
        platform.otpCode.collect {
            if (it !== "") {
                val bs = StringBuilder().append(it)
                otpInput = bs.toString()
                setupOtpCharacters(otpInput)
            }
        }
    }

    Scaffold (modifier = Modifier
        .fillMaxHeight()
        .padding(LocalSafeArea.current),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    vertical = 30.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row (
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold),
                        fontSize = 20.0.sp
                    )
                }
                Row (
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "${state.label}${if (state.phone_number != null) " to ${state.phone_number}" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    for ((index, _) in state.inputs.withIndex()) {
                        Column (modifier = Modifier
                            .weight(0.2f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            BasicTextField (
                                modifier = Modifier
                                    .shadow(1.dp, RoundedCornerShape(10.dp))
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.88f)
                                    .height(70.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                value = otpCharList[index],
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold),
                                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                    letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                    textAlign = TextAlign.Center
                                ),
                                onValueChange = {
                                    println("state.inputs")
                                    println(it)
                                },
                                enabled = false,
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    Column (
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
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
                        .padding(horizontal = 16.dp)
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
                        value = otpInput,
                        onValueChange = {
                            println("value change")
                            println(it)
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
                        .padding(top = 25.dp, start = 16.dp, end = 16.dp)
                        .absoluteOffset(y = -(70).dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            if (OrganisationModel.organisation.tenant_id!=null){
                                if (state.phone_number !== null) {
                                    onEvent(OtpStore.Intent.RequestOTP(
                                        token = "",
                                        phoneNumber = state.phone_number,
                                        tenantId = OrganisationModel.organisation.tenant_id!!
                                    ))
                                }
                            }
                        },
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        text = "Resend verification code",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light)
                    )
                }

                Row (
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .padding(horizontal = 16.dp)
                        .absoluteOffset(y = -(70).dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp)
                            .alpha(if (state.isLoading || authState.isLoading) 0.8f else 0.0f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Column (modifier = Modifier.absoluteOffset(y = -(30).dp)) {
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

                                    }
                                    12 -> {
                                        otpInput = otpInput.dropLast(1)
                                        setupOtpCharacters(otpInput)
                                        println(otpInput)
                                    }
                                    else -> {
                                        if (otpInput.length <= maxChar  && inputEnabled) {
                                            builder.append(otpInput).append(it.toString())
                                            otpInput = builder.toString()
                                            setupOtpCharacters(otpInput)
                                        }
                                    }
                                }
                            }
                        ) {
                            when(it) {
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
                                        text = it.toString(),
                                        style = TextStyle(
                                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold),
                                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                            letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
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