package com.presta.customer.ui.components.payLoanPropmpt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.composables.NavigateBackTopBar
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun PayLoanPromptScreen(component: PayLoanPromptComponent){
    Column(modifier = Modifier.fillMaxWidth()
        .fillMaxHeight()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            NavigateBackTopBar("Pay Loan", onClickContainer ={

            } )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(70.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.then(Modifier.size(60.dp)),

                        )
                }

            }
            Row(modifier = Modifier.padding(top = 22.dp, start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.Center) {

                Text(
                    text = "Your will receive shortly a prompt to enter your PIN from your payment provider",
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    modifier = Modifier.align(Alignment.CenterVertically)

                )

            }

        }

    }

}