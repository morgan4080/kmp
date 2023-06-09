<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/SavingsTransactionHistory.kt
package composables
=======
package com.presta.customer.ui.composables
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/SavingsTransactionHistory.kt

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
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/SavingsTransactionHistory.kt
=======
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/SavingsTransactionHistory.kt

@Composable

fun SavingsTransactionHistory() {
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/SavingsTransactionHistory.kt
=======
    //map the  data from  the api and add on the container
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/SavingsTransactionHistory.kt

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(top = 20.dp)) {

<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/SavingsTransactionHistory.kt
            Text(text = "Savings")
=======
            Text(text = "Savings",
            fontSize = 14.sp,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/SavingsTransactionHistory.kt
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            items(30) {
                TransactionHistoryContainer()
            }
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/SavingsTransactionHistory.kt
            //creates a space to view all the scrollable items Below the Bottom App bar
=======

>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/SavingsTransactionHistory.kt
            item {

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                )
            }

        }

    }

}