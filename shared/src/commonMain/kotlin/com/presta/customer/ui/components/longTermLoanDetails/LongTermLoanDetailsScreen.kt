package com.presta.customer.ui.components.longTermLoanDetails

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanDetailsScreen(component: LongTermLoanDetailsComponent) {
    val focusRequester = remember { FocusRequester() }
    var desiredPeriod by remember { mutableStateOf(TextFieldValue()) }
    val emptyDesiredPeriod by remember { mutableStateOf(TextFieldValue()) }
    val pattern = remember { Regex("^\\d+\$") }
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Enter Loan Details", onClickContainer = {
            component.onBackNavClicked()

        })
    }, content = { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    Text(
                        "Normal Loan",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 10.dp),
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                    Text(
                        "Interest 12%",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                    Text(
                        "Max Period 96(Months)",
                        modifier = Modifier
                            .padding(top = 10.dp),
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        TextInputContainer(
                            "Desired amount",
                            "",
                            inputType = InputTypes.NUMBER
                        ) {
                            val inputValue: Double? = TextFieldValue(it).text.toDoubleOrNull()
                            if (inputValue != null) {

                            } else {

                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
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
                                enabled = false,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                value = desiredPeriod,
                                onValueChange = {
                                    if (it.text.isEmpty() || it.text.matches(pattern)) {
                                        desiredPeriod = it
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
                                    if (desiredPeriod.text.isEmpty()
                                    ) {
                                        Text(
                                            modifier = Modifier.alpha(.3f),
                                            text = "Desired Period(Months)",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = desiredPeriod.text.isNotEmpty(),
                                        modifier = Modifier.absoluteOffset(y = -(16).dp),
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically(),
                                    ) {
                                        Text(
                                            text = "Desired Period(Months) ",
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
                                            visible = desiredPeriod.text.isNotEmpty(),
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically(),
                                        ) {

                                            IconButton(
                                                modifier = Modifier.size(18.dp),
                                                onClick = { desiredPeriod = emptyDesiredPeriod },
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
                    Text(
                        "Select Desired Period (EG 1-96 Months)",
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        ActionButton(label = "Confirm", onClickContainer = {
                            component.onConfirmSelected()

                        }, enabled = true)

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Cancel",
                            fontSize = 14.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    })
}













