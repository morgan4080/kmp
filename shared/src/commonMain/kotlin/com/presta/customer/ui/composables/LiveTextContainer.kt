package com.presta.customer.ui.composables

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
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun LiveTextContainer(
    callback3: (Boolean) -> Unit = { newError ->
    },
    callback2: (errorrOccured: Boolean) -> Unit = {},
    keyboardType: KeyboardType,
    pattern: Regex,
    userInput: String,
    label: String,
    callback: (userInput: String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val emptyTextContainer by remember { mutableStateOf(TextFieldValue()) }
    var userInputs by remember { mutableStateOf(TextFieldValue(userInput)) }
    callback2(userInputs.text == "")
    callback3(userInputs.text == "")
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column() {
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
                        .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .absoluteOffset(y = 2.dp),
                    enabled = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType
                    ),
                    value = userInputs,
                    onValueChange = {
                        if (it.text.isEmpty() || it.text.matches(pattern)) {
                            userInputs = it
                            callback(userInputs.text)
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
                        if (userInputs.text.isEmpty()
                        ) {
                            Text(
                                modifier = Modifier.alpha(.3f),
                                text = label,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        AnimatedVisibility(
                            visible = userInputs.text.isNotEmpty(),
                            modifier = Modifier.absoluteOffset(y = -(16).dp),
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            Text(
                                text = label,
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
                                visible = userInputs.text.isNotEmpty(),
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                IconButton(
                                    modifier = Modifier.size(18.dp),
                                    onClick = { userInputs = emptyTextContainer },
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
            if (userInputs.text == "") {
                callback3(true)
                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                    text = "Required",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                    color = Color.Red
                )

            } else {
                callback3(false)
            }

        }

    }
}