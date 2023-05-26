package com.presta.customer.ui.components.registration.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.registration.store.InputFields
import com.presta.customer.ui.components.registration.store.RegistrationStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.AsyncImage
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.TextInputContainer
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
    navigate: (memberRefId: String,phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    var firstName by remember {
        mutableStateOf(TextFieldValue())
    }
    var lastName by remember {
        mutableStateOf(TextFieldValue())
    }
    var email by remember {
        mutableStateOf(TextFieldValue())
    }
    var idNumber by remember {
        mutableStateOf(TextFieldValue())
    }
    var gender by remember {
        mutableStateOf(TextFieldValue())
    }
    var introducer by remember {
        mutableStateOf(TextFieldValue())
    }

    LaunchedEffect(state.error) {
        if (state.error !== null) {
            snackbarHostState.showSnackbar(
                state.error
            )
        }
    }

    fun doRegistration() {
        if (state.phoneNumber !== null) {
            onEvent(RegistrationStore.Intent.CreateMember(
                token = "",
                firstName = firstName.text,
                lastName = lastName.text,
                phoneNumber = state.phoneNumber,
                idNumber = idNumber.text,
                tocsAccepted = state.isTermsAccepted,
                tenantId = OrganisationModel.organisation.tenant_id
            ))
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

            println(":::::::::::::::::Navigate with ref id")

            println(state.registrationResponse.refId)

            navigate(
                state.registrationResponse.refId,
                state.phoneNumber,
                state.isTermsAccepted,
                state.isActive,
                state.onBoardingContext
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
        Column (modifier = Modifier.padding(horizontal = 16.dp)) {
            Row (
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                    fontSize = 20.0.sp
                )
            }
            Row(modifier = Modifier.padding(bottom = 29.dp)) {
                Text(
                    text = state.label,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }

            LazyColumn {
                state.inputs.forEach { inputMethod ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                        ) {
                            BasicTextField(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                                value = when(inputMethod.fieldType) {
                                        InputFields.FIRST_NAME -> firstName
                                        InputFields.LAST_NAME -> lastName
                                        InputFields.EMAIL -> email
                                        InputFields.ID_NUMBER -> idNumber
                                        InputFields.GENDER -> gender
                                        InputFields.INTRODUCER -> introducer
                                },
                                onValueChange = {
                                    when(inputMethod.fieldType) {
                                        InputFields.FIRST_NAME -> firstName = it
                                        InputFields.LAST_NAME -> lastName = it
                                        InputFields.EMAIL -> email = it
                                        InputFields.ID_NUMBER -> idNumber = it
                                        InputFields.GENDER -> gender = it
                                        InputFields.INTRODUCER -> introducer = it
                                    }
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

                                    if (when(inputMethod.fieldType) {
                                            InputFields.FIRST_NAME -> firstName
                                            InputFields.LAST_NAME -> lastName
                                            InputFields.EMAIL -> email
                                            InputFields.ID_NUMBER -> idNumber
                                            InputFields.GENDER -> gender
                                            InputFields.INTRODUCER -> introducer
                                        }.text.isEmpty()) {
                                        Text(
                                            modifier = Modifier.alpha(.3f),
                                            text = inputMethod.inputLabel,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = when(inputMethod.fieldType) {
                                            InputFields.FIRST_NAME -> firstName.text.isNotEmpty()
                                            InputFields.LAST_NAME -> lastName.text.isNotEmpty()
                                            InputFields.EMAIL -> email.text.isNotEmpty()
                                            InputFields.ID_NUMBER -> idNumber.text.isNotEmpty()
                                            InputFields.GENDER -> gender.text.isNotEmpty()
                                            InputFields.INTRODUCER -> introducer.text.isNotEmpty()
                                        },
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

                                        IconButton(
                                            modifier = Modifier.size(18.dp),
                                            onClick = {
                                                when(inputMethod.fieldType) {
                                                    InputFields.FIRST_NAME -> firstName = TextFieldValue()
                                                    InputFields.LAST_NAME -> lastName = TextFieldValue()
                                                    InputFields.EMAIL -> email = TextFieldValue()
                                                    InputFields.ID_NUMBER -> idNumber = TextFieldValue()
                                                    InputFields.GENDER -> gender = TextFieldValue()
                                                    InputFields.INTRODUCER -> introducer = TextFieldValue()
                                                }
                                            },
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
                            )
                        }
                    }
                }
            }

            Row (modifier = Modifier.padding(top = 50.dp)) {
                ActionButton("Submit", onClickContainer = {
                    doRegistration()
                }, loading = state.isLoading || authState.isLoading)
            }
        }
    }
}