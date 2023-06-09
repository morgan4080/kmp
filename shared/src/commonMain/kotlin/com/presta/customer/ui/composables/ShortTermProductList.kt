package com.presta.customer.ui.composables

import ProductSelectionCard
import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
    ) {
        Text(
            text = "Select Loan Product",
            modifier = Modifier.padding(top = 22.dp),
            fontSize = 14.sp,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
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
                            if (shortTermProduct.interestRate != null) "Interest " + shortTermProduct.interestRate.toString() +"%" else "",
                            onClickContainer = {
                               val loanName=shortTermProduct.name
                                component.onProductSelected(shortTermProduct.refId.toString(),loanName, referencedLoanRefId = component.referencedLoanRefId)
                            })
                    }
                }
            }
            //DefaultLoading  shimmer
           if (state.isLoading){
               items(4){
                   ElevatedCard(modifier = Modifier
                       .fillMaxWidth()
                       .padding(top=30.dp, bottom = 30.dp)){
                       Box(modifier = Modifier
                           .defaultMinSize(40.dp,40.dp)
                           .background(
                               ShimmerBrush(
                                   targetValue = 1300f,
                                   showShimmer = true
                               )
                           )
                           .fillMaxWidth()){
                       }
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


