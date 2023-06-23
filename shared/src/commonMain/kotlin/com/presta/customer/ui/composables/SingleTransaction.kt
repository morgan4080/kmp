package com.presta.customer.ui.composables

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.profile.model.PostingType
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse
import com.presta.customer.ui.helpers.formatDate
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun singleTransaction(transactionHistory:  List<PrestaTransactionHistoryResponse>?) {
    if (transactionHistory !== null) {
        transactionHistory.map { transaction ->
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
                                .background(
                                    if (transaction.postingType == PostingType.CR)
                                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                    else
                                        Color(0xFF9deeff).copy(alpha = 0.4f)
                                )
                                .size(30.dp),
                            onClick = {

                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.OpenInNew,
                                    modifier = if (transaction.postingType == PostingType.CR) Modifier.size(15.dp)
                                        .rotate(180F) else Modifier.size(15.dp),
                                    contentDescription = null,
                                    tint = if (transaction.postingType == PostingType.CR)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        Color(0XFF62a5b3)
                                )
                            }
                        )
                    }
                    Column {
                        Text(
                            modifier = Modifier.background(
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = transaction.purpose === null
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                                .defaultMinSize(minHeight = 8.dp, minWidth = 100.dp),
                            text = if (transaction.purpose !== null) "${transaction.purpose}" else "",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )

                        Text(
                            text = transaction.transactionReference,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }

                Column {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = formatMoney(transaction.amount),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )
                    Text(
                        text = formatDate(transaction.created),
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
            }
        }
        if (transactionHistory.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "You don't have transaction history",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        listOf(0, 1, 2).map {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "",
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .defaultMinSize(minHeight = 20.dp, minWidth = 150.dp)
                            .background(brush = ShimmerBrush(true, 800f))
                    )

                    Text(
                        text = "",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .defaultMinSize(minHeight = 20.dp, minWidth = 100.dp)
                            .background(brush = ShimmerBrush(true, 800f))
                    )
                }
            }
        }
    }
}