package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
=======
import androidx.compose.ui.graphics.Color
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
fun TransactionHistoryContainer2() {
=======
fun TransactionHistoryContainer() {
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt

    data class Transactions(
        val label: String,
        val credit: Boolean,
        val code: String,
        val amount: String,
        val date: String,
        val icon: ImageVector
    )

    Row(modifier = Modifier.fillMaxWidth()) {


        val transactionList = mutableListOf(
            Transactions(
                "Savings Deposit",
                false,
                "TRGHJK123LL",
                "15,000",
                "12 May 2020, 12:23 PM",
                Icons.Filled.OpenInNew
            )
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt


=======
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
        )


        transactionList.forEach { transaction ->
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt


=======
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Column(
                        modifier = Modifier.padding(end = 12.dp),
                    ) {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
                                .background(if (transaction.credit) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer)
=======
                                .background(
                                    if (transaction.credit)
                                        MaterialTheme.colorScheme.secondaryContainer
                                    else
                                        Color(0xFF9deeff)
                                )
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
                                .size(30.dp),
                            onClick = {

                            },
                            content = {
                                Icon(
                                    imageVector = transaction.icon,
                                    modifier = if (transaction.credit) Modifier.size(15.dp)
                                        .rotate(180F) else Modifier.size(15.dp),
                                    contentDescription = null,
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
                                    tint = if (transaction.credit) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
=======
                                    tint = if (transaction.credit)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        Color(0XFF62a5b3)
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
                                )
                            }
                        )
                    }
                    Column {
                        Text(
                            text = transaction.label,
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
=======
                            color = MaterialTheme.colorScheme.onBackground,
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = transaction.code,
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
=======
                            color = MaterialTheme.colorScheme.onBackground,
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                    }
                }

                Column {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = transaction.amount,
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
=======
                        color = MaterialTheme.colorScheme.onBackground,
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = transaction.date,
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                }
            }
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TransactionHistoryContainer.kt

=======
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TransactionHistoryContainer.kt
        }


    }

}