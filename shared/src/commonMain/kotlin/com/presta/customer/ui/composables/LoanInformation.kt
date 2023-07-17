package com.presta.customer.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun LoanInformation() {
    LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Loan Type",
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                )
                Text(
                    "Normal Loan",
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }

        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 100.dp))
        }
    }
}
