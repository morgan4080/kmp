package com.presta.customer.ui.components.modeofDisbursement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ProductSelectionCard2
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ModeOfDisbursementContent(
    component: ModeOfDisbursementComponent
) {
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {

            Row(modifier = Modifier.fillMaxWidth()) {
                NavigateBackTopBar("Disbursement Method", onClickContainer = {
                    component.onBackNavSelected()

                })

            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
            ) {

                Text(
                    modifier = Modifier,
                    text = "Select Disbursement Method",
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    fontSize = 14.sp,

                    )
                Spacer(modifier = Modifier.padding(top = 25.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    ProductSelectionCard2("Mpesa", onClickContainer = {
                        //Business  Logic
                        //Navigate to processing Transaction
                        //show failed or success based on the  state
                        component.onMpesaSelected()

                    })

                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 11.dp)) {

                    ProductSelectionCard2("Bank", onClickContainer = {
                        //Business  Logic
                        //Navigate to bank disbursement
                        component.onBankSelected()
                    })

                }
            }
        }
    }
}