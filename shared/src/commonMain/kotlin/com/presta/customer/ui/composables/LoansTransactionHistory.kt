<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoansTransactionHistory.kt
package composables
=======
package com.presta.customer.ui.composables
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoansTransactionHistory.kt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoansTransactionHistory.kt
=======
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoansTransactionHistory.kt

@Composable
fun LoansTransactionHistory() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(top = 20.dp)) {
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoansTransactionHistory.kt
            Text(text = "Loans")
=======
            Text(text = "Loans",
            fontSize = 14.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoansTransactionHistory.kt
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            items(30) {

                TransactionHistoryContainer()

            }
            //creates a space to view all the scrollable items Below the Bottom App bar

            item {

                Spacer(modifier = Modifier.padding(bottom = 100.dp))

            }

        }

    }
}