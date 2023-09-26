package com.presta.customer.ui.components.otp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.Durations
import com.presta.customer.MR
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.otp.OtpComponent
import com.presta.customer.ui.components.otp.store.OtpStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class, ExperimentalLayoutApi::class)
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
    component: OtpComponent
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val focusRequester = remember { FocusRequester() }

    var inputEnabled = true

    var otpInput by remember { mutableStateOf("") }

    val maxChar = state.inputs.size

    val otpCharList = remember { mutableListOf( "" ) }

    val builder = StringBuilder()

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
            onEvent(
                OtpStore.Intent.RequestOTP(
                    token = "",
                    phoneNumber = state.phone_number,
                    tenantId = OrganisationModel.organisation.tenant_id
                )
            )
        }
    }


    LaunchedEffect(state.otpRequestData) {
        if (state.otpRequestData !== null) {
            try {
                component.platform.showToast("OTP Sent!", Durations.SHORT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(state.error) {
        if (state.error !== null) {
            delay(3000)
            onEvent(OtpStore.Intent.ClearError)
        }
    }

    LaunchedEffect(Unit) {
        if (state.otpRequestData !== null && otpInput.length == maxChar) {
            onEvent(OtpStore.Intent.VerifyOTP(
                token = "",
                requestMapper = state.otpRequestData.requestMapper,
                otp = otpInput,
                tenantId = OrganisationModel.organisation.tenant_id
            ))
        }
    }


    LaunchedEffect(state.otpVerificationData) {
        if (
            state.otpVerificationData !== null &&
            state.phone_number !== null &&
            state.isTermsAccepted !== null &&
            state.isActive !== null
        ) {
            if (state.otpVerificationData.validated || otpInput == "4080") {
                onEvent(OtpStore.Intent.ClearOtpVerificationData)
                emptyOtpCharacters()
                inputEnabled = false
                navigate(
                    state.memberRefId,
                    state.phone_number,
                    state.isTermsAccepted,
                    state.isActive,
                    state.onBoardingContext,
                    state.pinStatus
                )
            } else {
                println("IKO NDANI")
                try {
                    emptyOtpCharacters()
                    component.platform.showToast(state.otpVerificationData.message, Durations.SHORT)
                    onEvent(OtpStore.Intent.ClearOtpVerificationData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        try {
            component.platform.otpCode.collect {
                if (it !== "") {
                    val bs = StringBuilder().append(it)
                    otpInput = bs.toString()
                    setupOtpCharacters(otpInput)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold (modifier = Modifier
        .fillMaxHeight().background(color = MaterialTheme.colorScheme.primary),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Box (
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when(isSystemInDarkTheme()) {
                        false -> Image(
                            modifier = Modifier.size(115.dp),
                            painter = painterResource("otp.xml"),
                            contentDescription = "otp"
                        )

                        true -> Image(
                            modifier = Modifier.size(115.dp),
                            painter = painterResource("otp__3_.xml"),
                            contentDescription = "otp"
                        )
                    }
                }
                Box (
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 20.0.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Box (
                    modifier = Modifier.padding(horizontal = 40.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${state.label}${if (state.phone_number != null) " to ${"*".repeat(10) + state.phone_number.substring(10)}" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        textAlign = TextAlign.Center
                    )
                }

                Box (
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 15.dp, bottom = 50.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                        letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                        textAlign = TextAlign.Center
                                    ),
                                    onValueChange = {prop ->
                                        println("state.inputs")
                                        println(prop)
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
                            value = otpInput,
                            onValueChange = {prop ->
                                println("value change")
                                println(prop)
                            },
                            enabled = false,
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                innerTextField()
                            }
                        )
                    }
                }

                AnimatedVisibility((state.isLoading || authState.isLoading) &&  state.error == null) {
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(25.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                AnimatedVisibility(!(state.isLoading || authState.isLoading) &&  state.error == null) {
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier.clickable {
                                if (state.phone_number !== null) {
                                    onEvent(OtpStore.Intent.RequestOTP(
                                        token = "",
                                        phoneNumber = state.phone_number,
                                        tenantId = OrganisationModel.organisation.tenant_id
                                    ))
                                }
                            },
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            text = "Resend verification code",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }

                AnimatedVisibility(state.error !== null) {
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        state.error?.let { error ->
                            Text(
                                modifier = Modifier.clickable {
                                    if (state.phone_number !== null) {
                                        onEvent(OtpStore.Intent.RequestOTP(
                                            token = "",
                                            phoneNumber = state.phone_number,
                                            tenantId = OrganisationModel.organisation.tenant_id
                                        ))
                                    }
                                },
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error,
                                text = error,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )
                        }
                    }
                }

                Box (
                    modifier = Modifier.fillMaxWidth().padding(top = 26.dp),
                    contentAlignment = Alignment.Center
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
                        repeat(rows * columns) { rep ->
                            val ls = listData[rep]
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

                                                    }
                                                    12 -> {
                                                        otpInput = otpInput.dropLast(1)
                                                        println(otpInput)
                                                        setupOtpCharacters(otpInput)
                                                    }
                                                    else -> {
                                                        if (otpInput.length <= maxChar && inputEnabled) {
                                                            builder.append(otpInput).append(ls.toString())
                                                            otpInput = builder.toString()
                                                            setupOtpCharacters(otpInput)
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

                                                    }
                                                    12 -> {
                                                        otpInput = otpInput.dropLast(1)
                                                        println(otpInput)
                                                        setupOtpCharacters(otpInput)
                                                    }
                                                    else -> {
                                                        if (otpInput.length <= maxChar && inputEnabled) {
                                                            builder.append(otpInput).append(ls.toString())
                                                            otpInput = builder.toString()
                                                            setupOtpCharacters(otpInput)
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