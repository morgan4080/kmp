package components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.update
import com.presta.customer.MR
import components.auth.store.AuthStore
import dev.icerock.moko.resources.compose.fontFamilyResource
import helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(component: AuthComponent) {
    val model by component.model.subscribeAsState()

    val focusRequester = remember { FocusRequester() }

    var pinInput by remember { mutableStateOf("") }

    val maxChar = model.inputs.size

    val pinCharList = remember { mutableListOf( "" ) }

    model.inputs.forEach { input ->
        pinCharList.add(input.value)
    }

    fun setupPinCharacters (e: String) {
        e.forEachIndexed { index, res ->
            pinCharList[index] = res.toString()
            pinCharList[index + 1] = ""
        }
        if (e.isEmpty()) pinCharList[0] = ""
    }

    fun clearPinCharacters () {
        pinInput.forEachIndexed { index, _ ->
            pinCharList[index] = ""
            pinCharList[index + 1] = ""
        }
        pinInput = ""
    }

    if (pinInput.length == maxChar) {
        when (model.context) {
            Contexts.CREATE_PIN -> {
                component.models.update {
                    it.copy(
                        title = "Confirm pin code",
                        pinCreated = true,
                        context = Contexts.CONFIRM_PIN
                    )
                }
                clearPinCharacters()
            }
            Contexts.CONFIRM_PIN -> {
                component.models.update {
                    it.copy(
                        label = "Login to Presta Customer using the following pin code",
                        title = "Enter pin code",
                        pinConfirmed = true,
                        termsAccepted = true,
                        context = Contexts.LOGIN
                    )
                }
                clearPinCharacters()
            }
            Contexts.LOGIN -> {
                /*component.onEvent(AuthStore.Intent.LoginUser(
                    phoneNumber = "",
                    pin = "",
                    tenant = "",
                    clientSecret = ""
                ))*/
                clearPinCharacters()
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
                    text = model.title,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                )
            }
            Row {
                Text(
                    text = model.label,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
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
                            value = pinCharList[index],
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
                                    pinCharList[index] = it
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
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .height(70.dp)
                        .alpha(0.0f),
                    value = pinInput,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            setupPinCharacters(it)
                            pinInput = it
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
                    .absoluteOffset(y = -(70).dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (model.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp).padding(end = 2.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    LaunchedEffect(focusRequester) {
        kotlin.run {
            try {
                focusRequester.requestFocus()
            } catch (e: IllegalStateException) {
                println(e)
            }
        }
    }

}