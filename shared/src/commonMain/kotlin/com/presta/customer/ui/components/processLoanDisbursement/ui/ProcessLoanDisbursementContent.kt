package com.presta.customer.ui.components.processLoanDisbursement.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.loanRequest.model.AppraisalStatus
import com.presta.customer.network.loanRequest.model.DisbursementStatus
import com.presta.customer.ui.components.processLoanDisbursement.store.ProcessingLoanDisbursementStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessLoanDisbursementContent(
    amount: Double,
    fees: Double,
    phoneNumber: String?,
    state: ProcessingLoanDisbursementStore.State,
    navigate: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,

                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val angle by infiniteTransition.animateFloat(
                        initialValue = 0F,
                        targetValue = 360F,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing =    LinearEasing)
                        )
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(80.dp)
                            .graphicsLayer {  }
                            .background(actionButtonColor)
                            .clip(CircleShape)
                            .background(if (state.loanDisbursementStatus == DisbursementStatus.DISBURSED) Color.Green else actionButtonColor),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.loanDisbursementStatus == DisbursementStatus.DISBURSED) {
                            Icon(
                                modifier = Modifier.size(70.dp),
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "",
                                tint = Color.White
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(70.dp).graphicsLayer {
                                    rotationZ = angle
                                },
                                imageVector = Icons.Default.HourglassEmpty,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }

                if (state.loanDisbursementStatus !== null) {
                    Text(
                        text = when(state.loanDisbursementStatus) {
                            DisbursementStatus.DISBURSED -> "DISBURSEMENT SUCCEEDED"
                            DisbursementStatus.NOTDISBURSED -> "NOT DISBURSED"
                            DisbursementStatus.FAILED -> "DISBURSEMENT FAILED"
                            DisbursementStatus.INPROGRESS -> "DISBURSEMENT IN PROGRESS"
                            DisbursementStatus.INITIATED -> "DISBURSEMENT INITIATED"
                        },
                        fontSize = 4.em,
                        modifier = Modifier.padding(top = 29.dp),
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )
                } else if (state.loanAppraisalStatus !== null) {
                    Text(
                        text = when(state.loanAppraisalStatus) {
                            AppraisalStatus.APPROVED -> "REQUEST APPROVED"
                            AppraisalStatus.FAILED -> "REQUEST FAILED"
                            AppraisalStatus.DENIED -> "REQUEST DECLINED"
                            AppraisalStatus.PENDING -> "AWAITING APPROVAL"
                        },
                        fontSize = 4.em,
                        modifier = Modifier.padding(top = 29.dp),
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )
                } else {
                    Text(
                        "APPLICATION RECEIVED!",
                        fontSize = 4.em,
                        modifier = Modifier.padding(top = 29.dp),
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )
                }

                Row(modifier = Modifier
                    .padding(start = 42.dp, end = 42.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.loanDisbursementStatus !== null) {
                        Text(
                            text =  when(state.loanDisbursementStatus) {
                                DisbursementStatus.DISBURSED -> "Your loan of Kes ${formatMoney(amount)} has been successfully disbursed."
                                DisbursementStatus.NOTDISBURSED -> "We regret that your loan request of Kes ${formatMoney(amount)} could not be disbursed. Please try again later."
                                DisbursementStatus.FAILED -> "We regret that your loan request of Kes ${formatMoney(amount)} failed. Please try again later."
                                DisbursementStatus.INPROGRESS -> "Your loan of Kes ${formatMoney(amount)} is being disbursed. You will receive a notification once completed."
                                DisbursementStatus.INITIATED -> "Your loan request disbursement has been initiated."
                            },
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    } else if (state.loanAppraisalStatus !== null) {
                        Text(
                            text = when(state.loanAppraisalStatus) {
                                AppraisalStatus.APPROVED -> "We are happy to inform you that your loan of Kes ${formatMoney(amount)} has been approved."
                                AppraisalStatus.FAILED -> "We regret that your loan request of Kes ${formatMoney(amount)} could not be processed at this time. Please try again later."
                                AppraisalStatus.DENIED -> "We regret that your loan request of Kes ${formatMoney(amount)} has been declined. Please try again later."
                                AppraisalStatus.PENDING -> "Your loan of Kes ${formatMoney(amount)} is awaiting approval. You will be notified once completed."
                            },
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    } else {
                        Text(
                            text = "Loan Request of Kes ${formatMoney(amount)} has been received",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    }
                }

                if (state.transactionId !== null) {
                    Row(modifier = Modifier
                        .padding(start = 42.dp, end = 42.dp, top = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Transaction ID: " + state.transactionId,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 26.dp))

            Row(modifier = Modifier.padding(start = 20.dp, end = 25.dp, top = 70.dp)) {
                ActionButton("Done", onClickContainer = {
                    navigate()
                })
            }
        }
    }
}