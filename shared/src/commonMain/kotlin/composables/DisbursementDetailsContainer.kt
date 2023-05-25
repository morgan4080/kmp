package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource


@Composable
fun DisbursementDetailsContainer() {

    ElevatedCard(
        modifier = Modifier
            .padding(top = 10.dp)
            .border(BorderStroke(0.5.dp, Color.White), shape = RoundedCornerShape(size = 12.dp))
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp,
                )
                    .fillMaxHeight(0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Disbursement Amount ",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes 30,000",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                    )
                }
                //data Rows
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()){
                    items(10){
                        disbursementDetailsRow("Requested Amount", "Kes 30,000")

                    }

                }


            }
        }

    }

}

@Composable
fun disbursementDetailsRow(label: String, data: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
        )

        Text(
            text = data,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)

        )

    }
}

