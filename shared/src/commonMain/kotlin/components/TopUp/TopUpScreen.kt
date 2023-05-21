package components.TopUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import composables.ActionButton
import composables.NavigateBackTopBar
import composables.OptionsSelectionContainer
import composables.ProductSelectionCard2
import composables.TextInputContainer
import theme.actionButtonColor
import theme.labelTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  TopUpScreen(){
    var popupControl by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NavigateBackTopBar("Topup")
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Enter top up details",
                    color = labelTextColor
                )
                //show The min  and the  max allowed
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                    text = "Min. 10,000 - Max. 50,000",
                    fontSize = 10.sp
                )

                //select top up Pop up
                //Added overlay  to the po up screen
                if (popupControl){

                    Popup(alignment = Alignment.Center){

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f)),
                            verticalArrangement = Arrangement.Center) {

                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 25.dp,
                                        end = 25.dp,
                                        top = 26.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp)
                                ) {
                                    Text(
                                        text = "Set Loan Term",
                                        modifier = Modifier
                                            .padding(
                                                top = 14.dp)
                                    )

                                    Text(
                                        text = "Select Options Below",
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                    Row(modifier = Modifier.fillMaxWidth()
                                        .padding(top = 18.dp)) {

                                        OptionsSelectionContainer(
                                            "Current term",
                                            onClickContainer = {

                                            })
                                    }
                                    Row(modifier = Modifier.fillMaxWidth()) {

                                        OptionsSelectionContainer("Emergency Loan at 10%/Month", onClickContainer = {

                                        })

                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            bottom = 10.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    ElevatedCard(onClick = {
                                        popupControl = false
                                    }, modifier = Modifier
                                        .padding(start = 16.dp)) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(
                                                top = 5.dp,
                                                bottom = 5.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                            )
                                        )

                                    }
                                    ElevatedCard(
                                        onClick = {
                                            popupControl = false
                                        }, modifier = Modifier.padding(end = 16.dp),
                                        colors = CardDefaults.elevatedCardColors(containerColor = actionButtonColor)
                                    ) {
                                        Text(
                                            text = "Proceed",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(
                                                top = 5.dp,
                                                bottom = 5.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                            )
                                        )

                                    }

                                }

                            }

                        }

                    }

                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)){

                    TextInputContainer("Desired Amount","")

                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 23.dp)){
                    ProductSelectionCard2("Loan Term", onClickContainer = {

                        //pop up
                        popupControl=true

                    })

                }

                //Text input occurs  Here

                Row(modifier = Modifier.fillMaxWidth().padding(top = 44.dp)){

                    ActionButton("Proceed", onClickContainer = {
                        //Navigate  Confirmation screen

                    })

                }

            }

        }

    }

}
