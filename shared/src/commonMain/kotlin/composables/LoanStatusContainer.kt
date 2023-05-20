package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoanStatusContainer() {
    ElevatedCard(modifier = Modifier.padding(top = 10.dp)) {
        Box(
            modifier = Modifier.background(color = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Emergency Loan",
                        color = Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp
                    )

                    Text(text = "1/3")

                }
                Row(
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes, 50,000",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 11.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {

                        Text(
                            text = "Amount Due ",
                            color = Color(0xFF8F8F8F.toInt()), // #002C56
                        )

                        Text(
                            text = "Kes 30,000",
                            color = MaterialTheme.colorScheme.error, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column() {

                        Text(
                            text = "Due",
                            color = Color(0xFF8F8F8F.toInt()), // #002C56
                        )

                        Text(
                            text = "12 May, 2023",
                            color = MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }


                    Column() {

                        Text(
                            text = "Status",
                            color = Color(0xFF8F8F8F.toInt()), // #002C56
                        )

                        Text(
                            text = "Performing",
                            color = MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}