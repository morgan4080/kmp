package com.presta.customer.ui.components.processLoanDisbursement.ui

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
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.presta.customer.MR
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
    state: ProcessingLoanDisbursementStore.State
) {

//    val infiniteTransition = rememberInfiniteTransition()
//    val angle by infiniteTransition.animateFloat(
//        initialValue = 0F,
//        targetValue = 360F,
//        animationSpec = infiniteRepeatable(
//            animation = tween(2000, easing = LinearEasing)
//        )
//    )

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

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(80.dp)
                            .graphicsLayer {  }
                            .background(actionButtonColor)
                            .clip(CircleShape)
                            .background(actionButtonColor),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }

                Text(
                    "PENDING APPROVAL!",
                    fontSize = 4.em,
                    modifier = Modifier.padding(top = 29.dp),
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                )

                Row(modifier = Modifier
                    .padding(start = 42.dp, end = 42.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Loan Request of Kes ${formatMoney(amount)} has been received , its is pending  approval",
                        fontSize = 2.4.em,
                        textAlign = TextAlign.Center
                    )

                }
            }

            Spacer(modifier = Modifier.padding(top = 26.dp))

            Row(modifier = Modifier.padding(start = 25.dp, end = 25.dp, top = 70.dp)) {

                ActionButton("Check  Status", onClickContainer = {

                })
            }
        }
    }
}