package com.presta.customer.ui.composables

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import kotlinx.coroutines.delay

enum class InputTypes {
    ENUM,
    STRING,
    NUMBER,
    PHONE,
    URI,
    EMAIL,
    PASSWORD,
    NUMBER_PASSWORD,
    DECIMAL
}
@Composable
fun TextInputContainer(
    label: String,
    inputValue: String,
    enabled: Boolean = true,
    inputType: InputTypes = InputTypes.STRING,
    imageUrl: String? = null,
    icon:  ImageVector? = null,
    callingCode: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    enumOptions: List<String> = emptyList(),
    callback: (userInput: String) -> Unit = {},
) {
    var userInput by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }

    if (inputValue.isNotEmpty() ) {
        userInput = TextFieldValue(inputValue)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(0.5.dp, RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                if (!enabled) callback("launch")
            }
    ) {
        when(inputType) {
            InputTypes.ENUM -> {
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(enumOptions[0]) }

                Column(modifier = Modifier.selectableGroup()) {
                    enumOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                        userInput = TextFieldValue(text)
                                        if (enabled) callback(userInput.text)
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
                                style = MaterialTheme.typography.bodySmall.merge(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
            else -> {
                BasicTextField(
                    modifier = Modifier
                        .height(65.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (focus.value != it.isFocused) {
                                focus.value = it.isFocused
                                if (!it.isFocused) {
                                    inputService?.hideSoftwareKeyboard()
                                }
                            }
                        }
                        .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .absoluteOffset(y = 2.dp),
                    keyboardOptions = KeyboardOptions(keyboardType =
                    when (inputType) {
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
                    value = userInput,
                    onValueChange = {
                        userInput = it
                        if (enabled) callback(userInput.text)
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
                    enabled = enabled,
                    decorationBox = { innerTextField ->
                        if (userInput.text.isEmpty() && callingCode == null) {
                            Text(
                                modifier = Modifier.alpha(.3f).padding(start = if (icon !== null) 50.dp else 0.dp, top = if (icon !== null) 5.dp else 0.dp),
                                text = label,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        AnimatedVisibility(
                            visible = userInput.text.isNotEmpty() || callingCode !== null,
                            modifier = Modifier.absoluteOffset(y = -(18).dp),
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            Text (
                                text = label,
                                color = primaryColor,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp
                            )
                        }


                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (imageUrl !== null) {
                                    AsyncImage(
                                        imageUrl,
                                        "Country Flag",
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(width = 8.dp))
                                }

                                if (callingCode !== null) {
                                    Text(
                                        modifier = Modifier.padding(end = 10.dp),
                                        text = "+$callingCode",
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                                            fontSize = 13.sp,
                                            fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                            letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                            fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                                        )
                                    )

                                    Divider(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.padding(end = 10.dp)
                                            .height(15.dp)
                                            .width(1.dp)
                                    )
                                }

                                if (icon !== null) {
                                    Icon(
                                        modifier = Modifier.size(40.dp).padding(end = 10.dp),
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = actionButtonColor
                                    )

                                    Spacer(modifier = Modifier.width(width = 8.dp))
                                }

                                innerTextField()
                            }
                            if (callingCode !== null || icon !== null) {
                                IconButton(
                                    modifier = Modifier.size(18.dp),
                                    onClick = {
                                        userInput = TextFieldValue()
                                        if (enabled) callback(userInput.text)
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
                            if (imageUrl !== null) {
                                Icon(
                                    modifier = Modifier.size(18.dp).alpha(0.4f).rotate(270F),
                                    imageVector = Icons.Filled.ArrowBackIosNew,
                                    contentDescription = null,
                                    tint = actionButtonColor
                                )
                            }
                        }
                    }
                )
            }
        }

        LaunchedEffect("") {
            delay(300)
            inputService?.showSoftwareKeyboard()
            focusRequester.requestFocus()
        }
    }
}