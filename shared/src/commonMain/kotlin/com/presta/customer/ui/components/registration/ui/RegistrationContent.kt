package com.presta.customer.ui.components.registration.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.otp.store.OtpStore
import com.presta.customer.ui.components.registration.store.RegistrationStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationContent(
    state: RegistrationStore.State,
    authState: AuthStore.State,
    onEvent: (RegistrationStore.Intent) -> Unit,
    navigate: (
        memberRefId: String,
        phoneNumber: String,
        isTermsAccepted: Boolean,
        isActive: Boolean,
        onBoardingContext: DefaultRootComponent.OnBoardingContext,
        pinStatus: PinStatus?
    ) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val focusRequester = remember { FocusRequester() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(state.error) {
        if (state.error !== null) {
            snackbarHostState.showSnackbar(
                state.error
            )

            // clear error
            onEvent(RegistrationStore.Intent.ClearError)
        }
    }

    fun checkIfValid(): Boolean {

        var valid = true

        listOf(
            state.firstName,
            state.lastName,
            state.email,
            state.idNumber,
            state.introducer,
        ).map{inputMethod ->
            if (inputMethod.required && inputMethod.value.text.isEmpty()) valid = false
        }

        return valid
    }

    fun doRegistration() {
        if (state.phoneNumber !== null) {

            if (OrganisationModel.organisation.tenant_id!=null){

                onEvent(RegistrationStore.Intent.CreateMember(
                    token = "",
                    firstName = state.firstName.value.text,
                    lastName = state.lastName.value.text,
                    phoneNumber = state.phoneNumber,
                    idNumber = state.idNumber.value.text,
                    tocsAccepted = state.isTermsAccepted,
                    tenantId = OrganisationModel.organisation.tenant_id
                ))
            }

        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Error: Record Missing Phone Number"
                )
            }
        }
    }

    LaunchedEffect(state.registrationResponse) {
        if (
            state.registrationResponse !== null &&
            state.phoneNumber !== null
        ) {
            snackbarHostState.showSnackbar(
                "Member Registration Successful!"
            )

            navigate(
                state.registrationResponse.refId,
                state.phoneNumber,
                state.isTermsAccepted,
                state.isActive,
                state.onBoardingContext,
                state.pinStatus
            )
        }
    }

    Scaffold (
        modifier = Modifier
            .fillMaxHeight()
            .padding(LocalSafeArea.current),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            LazyColumn {
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 30.dp)
                    ) {
                        Text(
                            text = state.title,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            style = TextStyle(
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                color = MaterialTheme.typography.headlineSmall.color,
                                fontSize = 20.sp,
                                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                                letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                            ),
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                            fontSize = 20.0.sp
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 15.dp)
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.5f).fillMaxWidth(0.9f),
                            text = state.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                    }
                }

                listOf(
                    state.firstName,
                    state.lastName,
                    state.email,
                    state.idNumber,
                    state.introducer,
                ).map { inputMethod ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .shadow(0.5.dp, RoundedCornerShape(10.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                        ) {
                            BasicTextField (
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .height(65.dp)
                                    .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                                    .absoluteOffset(y = 2.dp),
                                keyboardOptions = KeyboardOptions(keyboardType =
                                    when (inputMethod.inputTypes) {
                                        InputTypes.NUMBER -> KeyboardType.Number
                                        InputTypes.STRING -> KeyboardType.Text
                                        InputTypes.PHONE -> KeyboardType.Phone
                                        InputTypes.URI -> KeyboardType.Uri
                                        InputTypes.EMAIL -> KeyboardType.Email
                                        InputTypes.PASSWORD -> KeyboardType.Password
                                        InputTypes.NUMBER_PASSWORD -> KeyboardType.NumberPassword
                                        InputTypes.DECIMAL -> KeyboardType.Decimal
                                    }
                                ),
                                value = inputMethod.value,
                                onValueChange = {
                                    onEvent(RegistrationStore.Intent.UpdateInputValue(inputMethod.fieldType, it))
                                },
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                                    fontSize = 13.sp,
                                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                    letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                                ),
                                decorationBox = { innerTextField ->

                                    if (inputMethod.value.text.isEmpty()
                                    ) {
                                        Text(
                                            modifier = Modifier.alpha(.3f),
                                            text = inputMethod.inputLabel,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = inputMethod.value.text.isNotEmpty(),
                                        modifier = Modifier.absoluteOffset(y = -(16).dp),
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically(),
                                    ) {
                                        Text(
                                            text = inputMethod.inputLabel,
                                            color = primaryColor,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 11.sp
                                        )
                                    }


                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {

                                            innerTextField()
                                        }

                                        AnimatedVisibility(
                                            visible = inputMethod.value.text.isNotEmpty(),
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically(),
                                        ) {

                                            IconButton(
                                                modifier = Modifier.size(18.dp),
                                                onClick = { onEvent(RegistrationStore.Intent.UpdateInputValue(inputMethod.fieldType, TextFieldValue())) },
                                                content = {
                                                    Icon(
                                                        modifier = Modifier.alpha(0.4f),
                                                        imageVector = Icons.Filled.Cancel,
                                                        contentDescription = null,
                                                        tint = actionButtonColor
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        if (inputMethod.errorMessage !== "") {
                            Text(
                                modifier = Modifier.padding(horizontal = 22.dp),
                                text = inputMethod.errorMessage,
                                fontSize = 10.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                color = Color.Red
                            )
                        }
                    }
                }

                item {
                    Row (
                        modifier = Modifier
                            .padding(start = 16.dp, top = 30.dp, bottom = 16.dp, end = 16.dp)
                    ) {
                        ActionButton(
                            "Submit",
                            onClickContainer = {
                                doRegistration()
                            },
                            loading = state.isLoading || authState.isLoading,
                            enabled = checkIfValid()
                        )
                    }
                }
            }

        }
    }
}