package components.banKDisbursement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import composables.ActionButton
import composables.NavigateBackTopBar
import composables.OptionsSelectionContainer
import composables.ProductSelectionCard2
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.actionButtonColor

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BankDisbursementScreen(component: BankDisbursementComponent) {
    var launchPopUp by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier
                .fillMaxWidth()) {
                NavigateBackTopBar("Disbursement Method", onClickContainer = {

                })
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
            ) {

                Text(
                    modifier = Modifier,
                    text = "Select Disbursement Method"
                )
                Spacer(modifier = Modifier
                    .padding(top = 25.dp))

                //Select  banks  from the pop up
                if (launchPopUp) {
                    Popup() {
                        // Composable to select The bank
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f)),
                            verticalArrangement = Arrangement.Center) {
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 26.dp, end = 26.dp, top = 26.dp, bottom = 70.dp),
                                colors = CardDefaults
                                    .elevatedCardColors(containerColor = MaterialTheme.colorScheme.background )
                            ) {

                                Column(modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)) {

                                    Text(
                                        "Select Bank",
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                    )
                                    Text(
                                        "Select Options Below",
                                        modifier = Modifier
                                            .padding(start = 16.dp),
                                        fontSize = 10.sp
                                    )
                                    Column(modifier = Modifier
                                        .height(300.dp)) {

                                        Column(
                                            modifier = Modifier
                                                .verticalScroll(rememberScrollState())
                                                .wrapContentHeight()
                                        ) {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {


                                                })

                                            }

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer(
                                                    "Cooperative Bank",
                                                    onClickContainer = {


                                                    }
                                                )

                                            }

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {

                                                })

                                            }

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {


                                                })
                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {

                                                })

                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {

                                                })

                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {

                                                })

                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                OptionsSelectionContainer("KCB", onClickContainer = {

                                                })

                                            }

                                        }

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
                                        launchPopUp = false
                                    }, modifier = Modifier
                                        .padding(start = 16.dp)) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                            modifier = Modifier
                                                .padding(
                                                    top = 5.dp,
                                                    bottom = 5.dp,
                                                    start = 20.dp,
                                                    end = 20.dp
                                                )
                                        )

                                    }

                                    ElevatedCard(
                                        onClick = {
                                            launchPopUp = false
                                        }, modifier = Modifier
                                            .padding(end = 16.dp),
                                        colors = CardDefaults.elevatedCardColors(containerColor = actionButtonColor )
                                    ) {

                                        Text(
                                            text = "Proceed",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            modifier = Modifier
                                                .padding(
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

                Row(modifier = Modifier
                    .fillMaxWidth()){
                    ProductSelectionCard2("Select Bank", onClickContainer = {
                        //Business  Logic
                        //banK details pop up card
                        launchPopUp = true

                    })

                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)){
                    ProductSelectionCard2("Account Number", onClickContainer = {
                        //Business  Logic
                    })
                }
                Spacer(modifier = Modifier
                    .padding(top = 60.dp))
                ActionButton("Proceed", onClickContainer = {
                    //Navigate to process The  Transaction and show  success or  failed Transaction
                    component.onConfirmSelected()

                })

            }

        }

    }

}