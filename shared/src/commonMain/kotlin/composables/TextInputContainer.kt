package composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import theme.primaryColor

@Composable
fun TextInputContainer(label: String, inputValue: String, enabled: Boolean = true, imageUrl: String? = null, callback: () -> Unit = {}){

    var userInput by remember { mutableStateOf(inputValue) }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                callback()
            }
    ) {

        Row(modifier = Modifier
            .padding(16.dp)
        ){
            BasicTextField(
                modifier = Modifier
                    .focusRequester(focusRequester).padding(top = 5.dp)
                    .onFocusChanged {

                    },
                value = userInput,
                onValueChange = {
                    userInput = it
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                    letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                ),
                enabled = enabled,
                decorationBox = { innerTextField ->
                    if (userInput.isEmpty()) {
                        Text(
                            modifier = Modifier.alpha(.3f),
                            text = label,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    AnimatedVisibility(
                        visible = userInput.isNotEmpty(),
                        modifier = Modifier.absoluteOffset(y = -(18).dp),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically(),
                    ) {
                        Text(
                            text = label,
                            color = primaryColor,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (imageUrl !== null) {
                            AsyncImage(
                                imageUrl,
                                "Country Flag",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                            Spacer(modifier = Modifier.width(width = 5.dp))
                        }

                        innerTextField()
                    }
                }
            )
        }

        LaunchedEffect(Unit) {

        }
    }
}