package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun CurrentSavingsContainer(){
    ElevatedCard(modifier = Modifier
        .padding(top = 10.dp)
        .border( BorderStroke(1.dp,Color.White), shape = RoundedCornerShape(size = 12.dp))
    ) {
        Box (modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column (
                modifier = Modifier
                    .padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
                )
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Total Savings",
                        color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp)
                }
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes, 1000000,983.32",
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .fillMaxWidth())
                }
                Row (modifier = Modifier
                    .padding(top = 11.dp)
                    .fillMaxWidth()
                ) {
                    Column(){
                        Text(
                            text = "Current Savings ",
                            color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 10.sp)

                        Text(
                            text = "50,000",
                            color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,)
                    }

                    Spacer(modifier = Modifier.padding(start = 42.dp))
                    Column(){
                        Text(
                            text = "Current Shares",
                            color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 10.sp)
                        Text(
                            text = "10(kes 50,000)",
                            color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,)
                    }
                }
            }
        }
    }
}
