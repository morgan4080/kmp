package com.presta.customer.ui.composables

import ProductSelectionCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f)) {
        Text(
            text = "Select Loan Product",
            modifier = Modifier.padding(top = 22.dp),
            fontSize = 14.sp,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding( bottom = 5.dp)
                .fillMaxHeight(0.9f)
        ) {
            state.prestaShortTermProductList.map { shortTermProduct ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        ProductSelectionCard(shortTermProduct.name!!,
                            if( shortTermProduct.interestRate!=null) shortTermProduct.interestRate.toString() else "" ,
                            onClickContainer = {
                                //Navigate  to  EmergencyLoan Screen
                                //component.onEmergencyLoanSelected()
                                //Todo -Configure components
                                component.onSelected("em")
                            })
                    }
                }
            }
            //spacer to show items obove the common BottomAppbar
            item {
                Spacer(modifier = Modifier.padding(top = 50.dp))
            }
        }
    }
}

