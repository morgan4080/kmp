package components.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
import helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(component: OtpComponent) {
    val model by component.model.subscribeAsState()

    var otpInput by remember { mutableStateOf("") }

    val maxChar = model.inputs.size

    val otpCharList = remember { mutableListOf( "" ) }

    model.inputs.forEach { input ->
        otpCharList.add(input.value)
    }

    fun setupOtpCharacters (e: String) {
        e.forEachIndexed { index, res ->
            otpCharList[index] = res.toString()
            otpCharList[index + 1] = ""
        }
        if (e.isEmpty()) otpCharList[0] = ""
    }

    LaunchedEffect(otpInput) {
        kotlin.run {
            if (otpInput.length == maxChar) {
                println("Check validity of otp code")
                println("if success")
                println("navigate to auth screens")
                println("if failed")
                println("clear fields otpChar1 otpChar2 otpChar3 otpChar4 otpInput")
                component.onValid()
            }
        }
    }

    Scaffold (modifier = Modifier
        .fillMaxHeight(1f)
        .padding(LocalSafeArea.current)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 30.dp
                )
        ) {
            Row {
                Text(
                    text = model.label,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
            Row {
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                )
            }
            Row(
                modifier = Modifier.padding(top = 35.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for ((index, input) in model.inputs.withIndex()) {
                    Column (modifier = Modifier
                        .weight(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.88f)
                                .height(70.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            value = otpCharList[index],
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                                letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                textAlign = TextAlign.Center
                            ),
                            onValueChange = {
                                if (it.length <= maxChar) {
                                    otpCharList[index] = it
                                }
                            },
                            enabled = false,
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box (
                                    contentAlignment = Alignment.Center
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .absoluteOffset(y = -(70).dp),
            ) {
                BasicTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .alpha(0.0f),
                    value = otpInput,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            setupOtpCharacters(it)
                            otpInput = it
                        }
                    },
                    enabled = true,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        innerTextField()
                    }
                )
            }
            Row (
                modifier = Modifier
                    .padding(top = 35.dp)
                    .absoluteOffset(y = -(70).dp)
            ) {
                Text(
                    modifier = Modifier.clickable {
                             // resend otp
                    },
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    text = "Resend verification code${if (model.phone_number != null) " to ${model.phone_number}" else ""}",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
        }
    }
}