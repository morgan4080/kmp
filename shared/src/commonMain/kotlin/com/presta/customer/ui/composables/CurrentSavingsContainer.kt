package com.presta.customer.ui.composables
import ShimmerBrush
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun CurrentSavingsContainer (
    savingsBalances: PrestaSavingsBalancesResponse?,
) {

    var savingsTotalAmount: Double? = null
    var savingsBalance: Double? = null
    var sharesBalance: Double? = null
    var sharePrice: String? = null

    if (savingsBalances !== null) {
        savingsTotalAmount = savingsBalances.savingsTotalAmount
        savingsBalance = savingsBalances.savingsBalance
        sharesBalance = savingsBalances.sharesBalance
        sharePrice = savingsBalances.pricePerShare
    }

    ElevatedCard(modifier = Modifier
        .padding(top = 10.dp)
        .border( BorderStroke(1.dp,Color.White), shape = RoundedCornerShape(size = 12.dp))
    ) {
        Box (modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
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
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.background(
                            brush = ShimmerBrush(
                                targetValue = 1300f,
                                showShimmer = savingsTotalAmount == null
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ).defaultMinSize(150.dp),
                        text = if (savingsTotalAmount !== null) {
                            savingsTotalAmount.toString() +
                                    " KES"
                        } else "",
                        color= MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Current Savings ",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )

                        Text(
                            modifier = Modifier.background(
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = savingsBalance == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(150.dp),
                            text = if (savingsBalance !== null) savingsBalance.toString() else "",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )
                    }

                    Spacer(modifier = Modifier.padding(start = 30.dp))

                    Column {
                        Text(
                            text = "Current Shares",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )

                        Text(
                            modifier = Modifier.background (
                                brush = ShimmerBrush (
                                    targetValue = 1300f,
                                    showShimmer = sharesBalance == null || sharePrice == null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ).defaultMinSize(150.dp),
                            text = if (sharesBalance !== null && sharePrice !== null) "$sharePrice (${sharesBalance})" else "",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )
                    }
                }
            }
        }
    }
}
