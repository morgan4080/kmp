package com.presta.customer.ui.components.tenant.ui

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moriatsushi.insetsx.systemBars
import com.presta.customer.AppContext
import com.presta.customer.MR
import com.presta.customer.Platform
import com.presta.customer.SharedStatus
import com.presta.customer.ui.components.tenant.TenantComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantContent(
    component: TenantComponent,
    connectivityStatus: SharedStatus?,
    onTenantEvent: (TenantStore.Intent) -> Unit,
    state: TenantStore.State
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }
    var isError by remember { mutableStateOf(false) }



    LaunchedEffect(connectivityStatus) {
        if (connectivityStatus !== null) {
            connectivityStatus.current.collect {
                if (!it) {
                    snackBarHostState.showSnackbar(
                        message = "No internet connection",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    LaunchedEffect(state.tenantData) {
        if (state.tenantData !== null) {
            component.onSubmitClicked(state.tenantData.tenantId)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxHeight(1f)
            .padding(LocalSafeArea.current),
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 10.dp),
                hostState = snackBarHostState
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            ) {
                Text(
                    text = "Key in your Organization tenant id",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Metropolis.semiBold),
                    fontSize = 20.0.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Kindly note that your organization account number or account name serves as your tenant ID.",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Metropolis.light)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 31.dp)
            ) {
                listOf(state.tenantField).map { inputMethod ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                                .absoluteOffset(y = 2.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            value = inputMethod.value,
                            onValueChange = {
                                onTenantEvent(TenantStore.Intent.UpdateField(it))
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
                                                onTenantEvent(
                                                    TenantStore.Intent.UpdateField(
                                                        TextFieldValue()
                                                    )
                                                )
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


                    Text(modifier = Modifier
                        .padding(top = 22.dp)
                        .clickable {
                            component.platform.openUrl("https://support.presta.co.ke/portal/en/kb/articles/how-to-access-account-tenant-id")

                        },
                        text = "How to get tenant ID?",
                    fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular),
                        color = actionButtonColor,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline
                    )

                    if (inputMethod.errorMessage !== "") {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = inputMethod.errorMessage,
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular),
                            color = Color.Red
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 70.dp),
            ) {
                ActionButton(
                    "Submit", onClickContainer = {
                        onTenantEvent(
                            TenantStore.Intent.GetClientById(
                                searchTerm = state.tenantField.value.text
                            )
                        )
                        isError = state.error != null
                    },
                    loading = state.isLoading,
                    enabled = state.tenantField.value.text != ""
                )
            }
        }
    }
}