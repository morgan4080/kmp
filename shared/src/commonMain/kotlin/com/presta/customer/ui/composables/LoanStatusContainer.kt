<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoanStatusContainer.kt
package composables

=======
package com.presta.customer.ui.composables

import ShimmerBrush
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoanStatusContainer.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoanStatusContainer.kt
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
=======
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoanStatusContainer.kt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoanStatusContainer.kt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoanStatusContainer() {
    ElevatedCard(modifier = Modifier.padding(top = 10.dp)) {
        Box(
            modifier = Modifier.background(color = Color.White)
=======
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.helpers.formatDate
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun LoanStatusContainer(
    totalAmount: Double? = null,
    amountDue: Double? = null,
    dueDate: String? = null,
    loanType: String? = null,
    loanStatus: String? = null,
    loansCount: Int? = null,
    loanIndex: Int? = null
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(size = 12.dp))
        .absolutePadding(left = 2.dp, right = 2.dp, top = 10.dp, bottom = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoanStatusContainer.kt
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoanStatusContainer.kt
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
=======
                ).fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text (
                                modifier = Modifier.background(
                                    brush = ShimmerBrush(
                                        targetValue = 1300f,
                                        showShimmer = loanIndex == null || loansCount == null
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ).defaultMinSize(160.dp),
                                text = if (loanType !== null) loanType else "",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )

                            Text(
                                modifier = Modifier.background(
                                    brush = ShimmerBrush(
                                        targetValue = 1300f,
                                        showShimmer = loanIndex == null || loansCount == null
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ).defaultMinSize(30.dp),
                                text = if (loanIndex !== null && loansCount !== null)  "$loanIndex of $loansCount" else "",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )
                        }

                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text (
                            modifier = Modifier.background (
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = totalAmount == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(130.dp),
                            text = if (totalAmount !== null) "KES $totalAmount" else "",
                            fontSize = 20.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                        )

                        Text (
                            modifier = Modifier.padding(start = 10.dp).background (
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = loanStatus == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(80.dp),
                            text = if (loanStatus !== null) "($loanStatus)" else "",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 11.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {

                            Text(
                                text = "Amount Due",
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )

                            Text(
                                modifier = Modifier.background(
                                    brush = ShimmerBrush(
                                        targetValue = 1300f,
                                        showShimmer = amountDue == null
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ).defaultMinSize(80.dp),
                                text = if (amountDue !== null) "KES $amountDue" else "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 29.dp)) {

                            Text(
                                text = "Due Date",
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )

                            Text(
                                modifier = Modifier.background(
                                    brush = ShimmerBrush(
                                        targetValue = 1300f,
                                        showShimmer = dueDate == null
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ).defaultMinSize(80.dp),
                                text = if (dueDate !== null) formatDate(dueDate) else "",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                        }
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoanStatusContainer.kt
                    }
                }
            }
        }
    }
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LoanStatusContainer.kt
}
=======
}


>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LoanStatusContainer.kt
