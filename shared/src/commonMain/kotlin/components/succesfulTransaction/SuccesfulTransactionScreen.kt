package components.succesfulTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.unit.sp
import composables.ActionButton
import theme.actionButtonColor
import theme.transparentContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessfulTransactionScreen(component: SuccessfulTransactionComponent) {
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
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
                    .fillMaxWidth(),
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
                            .background(actionButtonColor)
                            .clip(CircleShape)
                            .background(actionButtonColor),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(70.dp)
                        )
                    }

                }

                Text(
                    "Transaction Successful!",
                    fontSize = 4.em,
                    modifier = Modifier
                        .padding(top = 29.dp),
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier
                    .padding(
                        top = 5.dp,
                    start = 30.dp,
                    end = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                    Text(
                        text = "Loan topup of Kes 30,000 transferred to Mpesa No. 0724123456",
                        fontSize = 2.4.em
                    )

                }

            }

            //share Receipt Container

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                ElevatedCard(onClick = {

                }, modifier = Modifier
                    .padding(start = 16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = transparentContainer)) {
                    Text(
                        text = "+ Share Receipt",
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
            Row(modifier = Modifier
                .padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 70.dp)) {

                ActionButton("Done", onClickContainer = {

                })
            }
        }
    }
}