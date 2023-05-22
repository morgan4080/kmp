package components.failedTransaction

import FailedTransactionComponent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import composables.NavigateBackTopBar
import theme.actionButtonColor
import theme.labelTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FailedTransactionScreen(component: FailedTransactionComponent) {
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                NavigateBackTopBar("", onClickContainer = {

                })

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Phone Number")
                    Text(
                        text = "+254 724 482 047",
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "KSH 5000")
                    Text(
                        text = "FES 30",
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )

                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,

                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //check box for  successful Transaction

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(70.dp)
                                .background(MaterialTheme.colorScheme.error)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.error),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(70.dp)
                            )
                        }

                    }

                    Text(
                        "Your Transaction Failed",
                        fontSize = 4.em,
                        modifier = Modifier
                            .padding(top = 29.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                //Padding To align with  The Bottom App Bar
                Column(
                    modifier = Modifier
                        .padding(
                            start = 25.dp,
                            end = 25.dp,
                            bottom = 120.dp
                        )
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Wrong Pin  Please try Again",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(bottom = 34.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        //Retry and  close  Button
                        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                            onClick = {

                            }

                        ) {

                            Text(
                                text = "Close",
                                color = labelTextColor,
                                modifier = Modifier
                                    .padding(
                                        start = 40.dp,
                                        end = 40.dp,
                                        top = 11.dp,
                                        bottom = 11.dp)
                            )
                        }

                        ElevatedCard(
                            colors = CardDefaults
                                .elevatedCardColors(containerColor = actionButtonColor),
                            onClick = {

                            }) {

                            Text(
                                text = "Retry",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(
                                        start = 40.dp,
                                        end = 40.dp,
                                        top = 11.dp,
                                        bottom = 11.dp)
                            )
                        }

                    }

                }

            }

        }

    }
}