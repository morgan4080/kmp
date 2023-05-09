package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun textInputContainer(label:String,UserInput:String){

    var userInput by remember { mutableStateOf("") }


    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
        ) {


            Row(modifier = Modifier.padding(start = 14.dp)){
                Text(
                    text=label,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 14.sp
                )

            }

            Row(modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 9.dp)){

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (focus.value != it.isFocused) {
                                focus.value = it.isFocused
                                if (!it.isFocused) {
                                    inputService?.hideSoftwareKeyboard()
                                }
                            }
                        },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester.requestFocus()
                        }
                    ),

                    onValueChange = {
                        userInput = it
                        //eventsViewModel.eventName = userInput
                    },
                    value = userInput,
                    singleLine = true
                )
            }


            LaunchedEffect("") {
                delay(300)
                inputService?.showSoftwareKeyboard()
                focusRequester.requestFocus()
            }
        }



}