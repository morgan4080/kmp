package com.presta.customer.ui.components.applyLoan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.authDevice.model.PrestaServices
import com.presta.customer.network.authDevice.model.ServicesActivity
import com.presta.customer.network.authDevice.model.TenantServicesResponse
import com.presta.customer.ui.components.applyLoan.ApplyLoanComponent
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ProductSelectionCard2
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun  ApplyLoanScreen(component: ApplyLoanComponent, innerPadding: PaddingValues){
    val authState by component.authState.collectAsState()
    Scaffold(
        topBar = {
            NavigateBackTopBar("Apply Loan", onClickContainer = {
                component.onBack()
            })
        }
    ) {
            LazyColumn(modifier = Modifier
                .padding(it)
                .fillMaxHeight(1f)
                .background(color = MaterialTheme.colorScheme.background),
            ) {
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                    ) {
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 16.dp, end = 16.dp)
                        ) {
                            Text(modifier = Modifier,
                                text = "Select Loan Type",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top=16.dp)){
                                ProductSelectionCard2("Short Term Loan", onClickContainer = {
                                    //Navigate to short term Loan screen
                                    component.onShortTermSelected()
                                })
                            }

                            if (authState.tenantServices.contains(TenantServicesResponse(PrestaServices.EGUARANTORSHIP, ServicesActivity.ACTIVE))) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top=10.dp)
                                ) {
                                    ProductSelectionCard2("Long Term Loan",
                                        onClickContainer = { component.onLongTermSelected() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}



