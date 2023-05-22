package components.otp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import components.auth.store.AuthStore
import components.onBoarding.store.OnBoardingStore
import components.otp.store.OtpStore
import dev.icerock.moko.resources.compose.fontFamilyResource
import helpers.LocalSafeArea
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpContent(
    state: OtpStore.State,
    authState: AuthStore.State,
    onBoardingState: OnBoardingStore.State,
    onOnBoardingEvent: (OnBoardingStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    onEvent: (OtpStore.Intent) -> Unit,
    navigate: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    var otpInput by remember { mutableStateOf("") }

    val maxChar = state.inputs.size

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

    if (authState.access_token !== null && state.phone_number !== null) {
        LaunchedEffect(authState.access_token, onBoardingState.member) {
            onEvent(OtpStore.Intent.RequestOTP(
                token = authState.access_token,
                phoneNumber = state.phone_number
            ))
            scope.launch {
                snackbarHostState.showSnackbar(
                    "OTP Sent!"
                )
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
                    text = "${state.label}${if (state.phone_number != null) " to ${state.phone_number}" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
            Row(
                modifier = Modifier.padding(top = 35.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for ((index, input) in state.inputs.withIndex()) {
                    Column (modifier = Modifier
                        .weight(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        BasicTextField(
                            modifier = Modifier
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
                                    otpCharList[index] = it
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
                        .fillMaxWidth()
                        .height(70.dp)
                        .alpha(0.0f),
                    value = otpInput,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            setupOtpCharacters(it)
                            otpInput = it
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
                Text(
                    modifier = Modifier.clickable {
                        if (authState.access_token !== null && state.phone_number !== null) {
                            onEvent(OtpStore.Intent.RequestOTP(
                                token = authState.access_token,
                                phoneNumber = state.phone_number
                            ))

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "OTP Sent!"
                                )
                            }
                        }
                    },
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    text = "Resend verification code",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
            Row (
                modifier = Modifier
                    .padding(top = 35.dp)
                    .absoluteOffset(y = -(70).dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (state.isLoading || authState.isLoading || onBoardingState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp).padding(end = 2.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}