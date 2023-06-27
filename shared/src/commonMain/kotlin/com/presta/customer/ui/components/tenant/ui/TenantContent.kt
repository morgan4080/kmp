package com.presta.customer.ui.components.tenant.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.moriatsushi.insetsx.ExperimentalSoftwareKeyboardApi
import com.moriatsushi.insetsx.imePadding
import com.presta.customer.MR
import com.presta.customer.SharedStatus
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.tenant.TenantComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSoftwareKeyboardApi::class)
@Composable
fun TenantContent(
    component: TenantComponent,
    connectivityStatus: SharedStatus?,
    onTenantEvent: (TenantStore.Intent) -> Unit,
    state: TenantStore.State,
    ) {
    val model by component.model.subscribeAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }
    var tenantId by remember { mutableStateOf(TextFieldValue()) }
    val pattern = remember { Regex("^\\d+\$") }
    val emptyTenantId by remember { mutableStateOf(TextFieldValue()) }
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
                // component.onEvent(AuthStore.Intent.UpdateOnlineState(it))
            }
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
                    .padding(top = 70.dp)
            ) {
                Text(
                    text = "Key in your Organization tenant id",
                    fontSize = 20.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            ) {
                Text(
                    text = "Kindly note that your organization's Account No. on your web account, under the account Profile section, serves as your tenant ID.",
                    fontSize = 13.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                )
            }
//                if (authState.isLoading || authState.cachedMemberData?.session_id !== "" || connectivityStatus == null || !authState.isOnline) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(25.dp).padding(end = 2.dp),
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 31.dp)
            ) {
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
                            .imePadding()
                            .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                            .absoluteOffset(y = 2.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        value = tenantId,
                        onValueChange = {
                            //validate  id   from server
                           tenantId=it

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

                            if (tenantId.text.isEmpty()
                            ) {
                                Text(
                                    modifier = Modifier.alpha(.3f),
                                    text = "Enter Tenant Id",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            AnimatedVisibility(
                                visible = tenantId.text.isNotEmpty(),
                                modifier = Modifier.absoluteOffset(y = -(16).dp),
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                Text(
                                    text = "Enter Tenant Id",
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
                                    visible = tenantId.text.isNotEmpty(),
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically(),
                                ) {

                                    IconButton(
                                        modifier = Modifier.size(18.dp),
                                        onClick = { tenantId = emptyTenantId },
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
            }
            if (isError) {
                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                    text = "Enter a  valid Tenant id" ,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                    color = Color.Red
                )

            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 70.dp),
            ) {

                ActionButton(
                    "Submit", onClickContainer = {
                        //  component.onSignInClicked()
                        //proceed to Register//Login
                        //submit  tenant id and validate
                        // if validated proceed to register

                        onTenantEvent(TenantStore.Intent.GetClientById(
                            searchTerm = tenantId.text
                        ))
                        if (state.prestaTenantById?.tenantId ==null){
                            isError=true

                        }

                    },
                    enabled = tenantId.text != ""
                )
            }
        }
    }
}