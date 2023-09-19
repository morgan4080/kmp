package com.presta.customer.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun SelfEmployedDetails(
    signHomeState: SignHomeStore.State,
    authState: AuthStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
    onClickSubmit: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val userDetailsMap = mutableMapOf<String, String>()
    var hasError by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (signHomeState.prestaTenantByPhoneNumber?.refId == null) {
            LazyColumn() {
                items(6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp)
                            .background(color = MaterialTheme.colorScheme.background),
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(40.dp, 40.dp)
                                    .background(
                                        ShimmerBrush(
                                            targetValue = 1300f,
                                            showShimmer = true
                                        )
                                    )
                                    .fillMaxWidth()
                            ) {
                            }
                        }
                    }
                }
            }
        } else {
            listOf(
                signHomeState.businessLocation,
                signHomeState.businessType,
                signHomeState.kraPin,
            ).map { inputMethod ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(0.5.dp, RoundedCornerShape(10.dp))
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = RoundedCornerShape(10.dp)
                        ),
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .height(65.dp)
                            .padding(
                                top = 20.dp,
                                bottom = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                            .absoluteOffset(y = 2.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
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
                        enabled = inputMethod.enabled,
                        onValueChange = {
                            if (inputMethod.enabled) {
                                hasError = false
                                onProfileEvent(
                                    SignHomeStore.Intent.UpdateKycValues(
                                        inputMethod.fieldType,
                                        it
                                    )
                                )
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

                            Row(
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
                                        onClick = {
                                            if (inputMethod.enabled) {
                                                onProfileEvent(
                                                    SignHomeStore.Intent.UpdateKycValues(
                                                        inputMethod.fieldType,
                                                        TextFieldValue()
                                                    )
                                                )
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
                        }
                    )
                }
                if (inputMethod.errorMessage !== "") {
                    hasError = true
                    Text(
                        modifier = Modifier.padding(horizontal = 22.dp),
                        text = inputMethod.errorMessage,
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                        color = Color.Red
                    )
                } else {
                    hasError = false
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                ActionButton(
                    label = "Submit",
                    onClickContainer = {
                        if (signHomeState.businessLocation.value.text != "" && signHomeState.businessType.value.text != "" && signHomeState.kraPin.value.text != "") {
                            userDetailsMap["employer"] = signHomeState.employer.value.text
                            userDetailsMap["employmentNumber"] =
                                signHomeState.employmentNumber.value.text
                            userDetailsMap["grossSalary"] = signHomeState.grossSalary.value.text
                            userDetailsMap["netSalary"] = signHomeState.netSalary.value.text
                            userDetailsMap["kraPin"] = signHomeState.kraPin.value.text
                            userDetailsMap["businessLocation"] =
                                signHomeState.businessLocation.value.text
                            userDetailsMap["businessType"] = signHomeState.businessType.value.text
                            authState.cachedMemberData?.let {
                                SignHomeStore.Intent.UpdatePrestaTenantDetails(
                                    token = it.accessToken,
                                    memberRefId = signHomeState.prestaTenantByPhoneNumber.refId,
                                    details = userDetailsMap
                                )
                            }?.let {
                                onProfileEvent(
                                    it
                                )
                            }
                            //Todo-----show failed or successful update
                            onClickSubmit()
                        }

                    },
                    enabled = true,
                    loading = signHomeState.isLoading
                )
            }
        }
    }
}