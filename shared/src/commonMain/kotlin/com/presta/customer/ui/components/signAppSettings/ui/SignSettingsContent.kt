package com.presta.customer.ui.components.signAppSettings.ui

import ShimmerBrush
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.signAppSettings.SignSettingsComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignSettingsContent(
    component: SignSettingsComponent,
    state: SignHomeStore.State,
    authState: AuthStore.State,
    onEvent: (SignHomeStore.Intent) -> Unit,
) {
    var firstnameLive by remember { mutableStateOf("") }
    var lastnameLive by remember { mutableStateOf("") }
    var phoneNumberLive by remember { mutableStateOf("") }
    var idNumberLive by remember { mutableStateOf("") }
    var emailLive by remember { mutableStateOf("") }
    val name = state.prestaTenantByPhoneNumber?.firstName
    if (state.prestaTenantByPhoneNumber?.refId != null) {
        firstnameLive = state.prestaTenantByPhoneNumber.firstName
        lastnameLive = state.prestaTenantByPhoneNumber.lastName
        phoneNumberLive = state.prestaTenantByPhoneNumber.phoneNumber
        idNumberLive = state.prestaTenantByPhoneNumber.idNumber
        emailLive = state.prestaTenantByPhoneNumber.email
    }
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(start = 9.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = Modifier,
                                    text = "Settings",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp
                                )
                            }
                            androidx.compose.material3.IconButton(
                                modifier = Modifier.absoluteOffset(x = 6.dp).zIndex(1f),
                                onClick = {
                                    //scopeDrawer.launch { drawerState.open() }
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Outlined.Logout,
                                        modifier = Modifier.size(25.dp),
                                        contentDescription = "Menu pending",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {

                if (state.prestaTenantByPhoneNumber?.firstName == null) {
                    items(6) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
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
                } else {
                    item {
                        listOf(
                            state.firstName,
                            state.lastName,
                            state.email,
                            state.idNumber,
                            state.introducer,
                        ).map { inputMethod ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
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
                                            onEvent(
                                                SignHomeStore.Intent.UpdateInputValue(
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
                                                            onEvent(
                                                                SignHomeStore.Intent.UpdateInputValue(
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
                                Text(
                                    modifier = Modifier.padding(horizontal = 22.dp),
                                    text = inputMethod.errorMessage,
                                    fontSize = 10.sp,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                    color = Color.Red
                                )
                            }

                        }

                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                        ) {
                            ActionButton(
                                label = "SUBMIT", onClickContainer = {
                                    authState.cachedMemberData?.let {
                                        SignHomeStore.Intent.UpdatePrestaTenantPersonalInfo(
                                            token = it.accessToken,
                                            memberRefId = state.prestaTenantByPhoneNumber.refId,
                                            firstName = state.firstName.value.text,
                                            lastName = state.lastName.value.text,
                                            phoneNumber =state.introducer.value.text,
                                            idNumber = state.idNumber.value.text,
                                            email = state.email.value.text
                                        )
                                    }?.let {
                                        onEvent(
                                            it
                                        )
                                    }

                                },
                                loading = state.isLoading
                            )
                        }

                    }

                }
                item {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 300.dp)
                    )
                }
            }
        })

}