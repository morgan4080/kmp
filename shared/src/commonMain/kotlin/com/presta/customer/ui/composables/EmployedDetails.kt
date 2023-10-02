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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.KycInputs
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun EmployedDetails(
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
    onClickSubmit: () -> Unit,
    component: AddGuarantorsComponent,
) {
    val userDetailsMap = mutableMapOf<String, String>()
    var hasError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (signHomeState.prestaTenantByPhoneNumber?.refId == null) {
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
        } else {
            item {
                listOf(
                    signHomeState.employer,
                    signHomeState.department,
                    signHomeState.employmentNumber,
                    signHomeState.grossSalary,
                    signHomeState.netSalary,
                    signHomeState.kraPin,
                    signHomeState.employmentType,
                    //
                ).filter {formItem ->
                    // check client settings state
                    // determine which fields to exclude
                    state.prestaClientSettings?.let { settings ->
                        settings.response.details?.let { detail ->
                            if (!detail.containsKey("employment_type") && formItem.fieldType == KycInputs.EMPLOYMENTTERMS) {
                                return@filter false
                            }
                            if (!detail.containsKey("department") && formItem.fieldType == KycInputs.DEPARTMENT) {
                                return@filter false
                            }
                            if (!detail.containsKey("employer_name") && formItem.fieldType == KycInputs.EMPLOYER) {
                                return@filter false
                            }
                            if (!detail.containsKey("employment_number") && formItem.fieldType == KycInputs.EMPLOYMENTNUMBER) {
                                return@filter false
                            }
                            if (!detail.containsKey("gross_salary") && formItem.fieldType == KycInputs.GROSSSALARY) {
                                return@filter false
                            }
                            if (!detail.containsKey("net_salary") && formItem.fieldType == KycInputs.NETSALARY) {
                                return@filter false
                            }
                            if (!detail.containsKey("kra_pin") && formItem.fieldType == KycInputs.KRAPIN) {
                                return@filter false
                            }
                        }
                    }
                    true
                }.map { inputMethod ->

                    when(inputMethod.inputTypes) {
                        InputTypes.ENUM -> {
                            val (selectedOption, onOptionSelected) = remember { mutableStateOf(inputMethod.enumOptions[0]) }

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
                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp).absoluteOffset(y = (10.dp)),
                                    text = inputMethod.inputLabel,
                                    color = primaryColor,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp
                                )
                                LazyRow(
                                    modifier = Modifier.selectableGroup()
                                ) {
                                    inputMethod.enumOptions.forEach { text ->
                                        item {
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(56.dp)
                                                    .selectable(
                                                        selected = (text == selectedOption),
                                                        onClick = {
                                                            onOptionSelected(text)
                                                            if (inputMethod.enabled) {
                                                                hasError = false
                                                                onProfileEvent(
                                                                    SignHomeStore.Intent.UpdateKycValues(
                                                                        inputMethod.fieldType,
                                                                        TextFieldValue(text)
                                                                    )
                                                                )
                                                            }
                                                        },
                                                        role = Role.RadioButton
                                                    )
                                                    .padding(horizontal = 16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = (text == selectedOption),
                                                    onClick = null // null recommended for accessibility with screenreaders
                                                )
                                                Text(
                                                    text = text,
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    style = MaterialTheme.typography.bodySmall.merge(),
                                                    modifier = Modifier.padding(start = 16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
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
                                            else -> KeyboardType.Text
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
                    }
                }
            }
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                ) {
                    ActionButton(
                        label = "Submit",
                        onClickContainer = {
                            if (signHomeState.employer.value.text != "" && signHomeState.employmentNumber.value.text != "") {
                                userDetailsMap["employer"] =
                                    signHomeState.employer.value.text
                                userDetailsMap["employmentType"] =
                                    signHomeState.employmentType.value.text
                                userDetailsMap["employmentNumber"] =
                                    signHomeState.employmentNumber.value.text
                                userDetailsMap["grossSalary"] =
                                    signHomeState.grossSalary.value.text
                                userDetailsMap["netSalary"] =
                                    signHomeState.netSalary.value.text
                                userDetailsMap["kraPin"] =
                                    signHomeState.kraPin.value.text
                                userDetailsMap["businessLocation"] =
                                    signHomeState.businessLocation.value.text
                                userDetailsMap["businessType"] =
                                    signHomeState.businessType.value.text


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
}