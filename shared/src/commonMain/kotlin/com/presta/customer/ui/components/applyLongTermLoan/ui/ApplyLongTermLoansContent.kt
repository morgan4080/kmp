package com.presta.customer.ui.components.applyLongTermLoan.ui

import ProductSelectionCard
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ApplyLongTermLoansContent(
    component: ApplyLongTermLoanComponent,
    state: ApplyLongTermLoansStore.State){

    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Select Loan Product", onClickContainer = {
            component.onBackNavClicked()

        })
    }, content = { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    ProductSelectionCard("Normal  Loan",
                        description = "Interest 12 %",
                        onClickContainer = {
                            component.onProductSelected()

                        })
                }

            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    ProductSelectionCard("Fosa Loans",
                        description = "Interest 14.5 %",
                        onClickContainer = {

                        })
                }

            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    })

}