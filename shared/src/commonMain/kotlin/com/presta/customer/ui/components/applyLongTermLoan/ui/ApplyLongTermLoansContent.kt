package com.presta.customer.ui.components.applyLongTermLoan.ui

import ProductSelectionCard
import ShimmerBrush
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ApplyLongTermLoansContent(
    component: ApplyLongTermLoanComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State
) {
    var launchHandleLoanRequestPopUp by remember { mutableStateOf(false) }
    var launchContinue by remember { mutableStateOf(false) }
    var memberRefId by remember { mutableStateOf("") }
    var productRefId by remember { mutableStateOf("") }
    var productsRefId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val loanScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            BottomSheetValue.Collapsed
        )
    )
    var isSheetContentVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memberRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState),
        sheetPeekHeight = if (isSheetContentVisible) 100.dp else 0.dp,
        sheetContentColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            if (isSheetContentVisible) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("This loan is in Progress")
                    Row(modifier = Modifier.fillMaxWidth()) {

                    }
                }
            }
        }) { innerPadding1 ->
        Scaffold(modifier = Modifier.padding(innerPadding1),
            topBar = {
                NavigateBackTopBar("Select Loan Product",
                    onClickContainer = {
                        component.onBackNavClicked()
                    })
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                    ) {

                        if (state.prestaLongTermLoanProducts?.total == null) {
                            items(6) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, bottom = 10.dp)
                                        .background(color = MaterialTheme.colorScheme.background),
                                ) {
                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = MaterialTheme.colorScheme.background),
                                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .defaultMinSize(40.dp, 40.dp)
                                                .background(
                                                    ShimmerBrush(
                                                        targetValue = 1300f,
                                                        showShimmer = true
                                                    )
                                                )
                                                .fillMaxWidth()
                                        ) {
                                        }
                                    }
                                }
                            }
                        } else {
                            state.prestaLongTermLoanProducts.list?.map { longTermLoanResponse ->
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                    ) {
                                        ProductSelectionCard(longTermLoanResponse.name.toString(),
                                            description = longTermLoanResponse.interestRate.toString(),
                                            onClickContainer = {
                                                productRefId = longTermLoanResponse.refId.toString()
                                                authState.cachedMemberData?.let {
                                                    ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsSpecificProduct(
                                                        token = it.accessToken,
                                                        productRefId = productRefId,
                                                        memberRefId = memberRefId
                                                    )

                                                }?.let {
                                                    onEvent(
                                                        it
                                                    )
                                                }
                                                if (!state.prestaLongTermLoansRequestsSpecificProduct?.content.isNullOrEmpty()) {
                                                    scope.launch { bottomSheetState.expand() }
                                                    isSheetContentVisible = true

                                                } else {
                                                    isSheetContentVisible = false
                                                    scope.launch { bottomSheetState.collapse() }
                                                    component.onProductSelected(
                                                        loanRefId = productRefId
                                                    )
                                                }
                                                println("The product Refid::  " + productRefId)

                                            }
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(bottom = 100.dp))
                        }

                        //Pop up-- check if a loan has been previously applied, proceed or resolve
                        item {
                            if (launchHandleLoanRequestPopUp) {
                                Popup {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .background(color = Color.Black.copy(alpha = 0.7f)),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        ElevatedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    start = 26.dp,
                                                    end = 26.dp,
                                                    top = 40.dp,
                                                    bottom = 90.dp
                                                ),
                                            colors = CardDefaults
                                                .elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(start = 16.dp, end = 16.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = "Loan is in progress",
                                                        modifier = Modifier.padding(top = 20.dp),
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                                    )
                                                    Text(
                                                        text = state.error.toString(),
                                                        modifier = Modifier.padding(top = 10.dp),
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                                    )
                                                }
                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        top = 20.dp,
                                                        bottom = 10.dp,
                                                    ),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {

                                                OutlinedButton(
                                                    border = BorderStroke(
                                                        width = 1.dp,
                                                        color = MaterialTheme.colorScheme.primary
                                                    ),
                                                    onClick = {
                                                        launchHandleLoanRequestPopUp = false
                                                    },
                                                    modifier = Modifier
                                                        .padding(start = 16.dp)
                                                        .height(30.dp),
                                                ) {

                                                    Text(
                                                        text = "Proceed",
                                                        fontSize = 11.sp,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier.align(Alignment.CenterVertically),
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                                    )

                                                }
                                                OutlinedButton(
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        containerColor = actionButtonColor
                                                    ),
                                                    border = BorderStroke(
                                                        width = 0.dp,
                                                        color = actionButtonColor
                                                    ),
                                                    onClick = {
                                                        launchHandleLoanRequestPopUp = false
                                                        //Todo---navigate to loan requests to resolve
                                                    },
                                                    modifier = Modifier
                                                        .padding(end = 16.dp)
                                                        .height(30.dp),
                                                ) {

                                                    Text(
                                                        text = "Resolve",
                                                        color = Color.White,
                                                        fontSize = 11.sp,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier.align(Alignment.CenterVertically),
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}


