package com.presta.customer.ui.components.applyLoan.ui

import ApplyLoanComponent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ProductSelectionCard2
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ApplyLoanScreen(component: ApplyLoanComponent, innerPadding: PaddingValues){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState)},
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

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top=10.dp)){
                                ProductSelectionCard2("Long Term Loan",
                                    onClickContainer = {
                                        //Coming soon
                                        //Show a  snack Bar
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Coming Soon"
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
}



