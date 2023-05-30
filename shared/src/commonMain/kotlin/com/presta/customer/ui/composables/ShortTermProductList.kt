package com.presta.customer.ui.composables

import ProductSelectionCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ShortTermProductList(
    component: ShortTermLoansComponent,
    state: ShortTermLoansStore.State,

    ) {
    //update
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Select Loan Product",
            modifier = Modifier.padding(top = 25.dp),
            fontSize = 14.sp,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)

        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    ProductSelectionCard(state.prestaShortTermProductList?.name.toString(),
                        state.prestaShortTermProductList?.interestPeriodCycle ,
                        onClickContainer = {
                        //Navigate  to  EmergencyLoan Screen
                        //component.onEmergencyLoanSelected()
                        //Todo -Configure components
                        component.onSelected("em")

                    })
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Mkopo Halisi Loan ", "Interest 12%", onClickContainer = {
                        //Tests for  Loan payments-To be deleted
                        component.onSelecte2("pay")

                    })
                }
            }

            item {

                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Shule Loan ", "Interest 12%", onClickContainer = {

                    })
                }
            }

            item {

                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Mfanisi Loan ", "Interest 12%", onClickContainer = {
                    })
                }

            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Normal Loan ", "Interest 12%", onClickContainer = {

                    })
                }
            }
        }
    }
}

