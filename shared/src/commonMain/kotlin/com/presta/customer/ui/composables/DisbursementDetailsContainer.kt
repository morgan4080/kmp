package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun DisbursementDetailsContainer(){

    ElevatedCard(modifier = Modifier.padding(top = 10.dp)) {
        Box (modifier = Modifier.background(color = Color.White)
        ) {
            Column (
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp,
                )
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Disbursement Amount ",
                        color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp
                    )


                }
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes 30,000",
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                //data Rows
                Spacer(modifier = Modifier.height(19.dp))
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")


            }
        }

    }

}

@Composable
fun disbursementDetailsRow(label: String,data: String){

    Row(modifier = Modifier.fillMaxWidth()
        .padding(top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween){

        Text(
            text = label,
            fontSize = 10.sp)

        Text(
            text = data,
            fontSize = 12.sp,
            color= MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Black)

    }


}

