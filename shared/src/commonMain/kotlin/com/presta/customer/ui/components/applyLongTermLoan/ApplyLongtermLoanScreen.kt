package com.presta.customer.ui.components.applyLongTermLoan

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLongTermLoanScreen(component: ApplyLongTermLoanComponent) {
    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        topBar = {
          NavigateBackTopBar("Apply Loan", onClickContainer = {

          })
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
                }
            }
        })

}













