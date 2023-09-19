package com.presta.customer.ui.components.applyLongTermLoan.ui

import ProductSelectionCard
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
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
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.network.longTermLoans.model.LoanApplicationStatus
import com.presta.customer.network.longTermLoans.model.LoanRequestListData
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomSnackBar(
    override val message: String,
    val isError: Boolean
) : SnackbarVisuals {
    override val actionLabel: String
        get() = if (isError) "Error" else "OK"
    override val withDismissAction: Boolean
        get() = false
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Indefinite
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun ApplyLongTermLoansContent(
    component: ApplyLongTermLoanComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State
) {
    var launchHandleLoanRequestPopUp by remember { mutableStateOf(false) }
    var memberRefId by remember { mutableStateOf("") }
    var productRefId by remember { mutableStateOf("") }
    var loanName by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var filteredResponse: List<LoanRequestListData> = emptyList()
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    val contentList = state.prestaLongTermLoansRequestsSpecificProduct?.content
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memberRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
    if (productRefId != "") {
        LaunchedEffect(
            state.prestaLongTermLoansRequestsSpecificProduct,
            productRefId,
            refreshing
        ) {
            if (state.prestaLongTermLoansRequestsSpecificProduct?.content?.isEmpty() == false) {

                println("The value of the list is;;;;;;; " + contentList)
                snackbarHostState.currentSnackbarData?.dismiss()
                filteredResponse =
                    state.prestaLongTermLoansRequestsSpecificProduct.content.filter { existingLoan ->
                        existingLoan.loanProductRefId.contains(productRefId)
                    }
                filteredResponse.map { filteredResponse ->
                    if (filteredResponse.applicationStatus == LoanApplicationStatus.COMPLETED) {
                        component.onProductSelected(
                            loanRefId = productRefId
                        )

                    }
                    if (filteredResponse.applicationStatus == LoanApplicationStatus.INPROGRESS) {
                        snackBarScope.launch {
                            snackbarHostState.showSnackbar(
                                CustomSnackBar(
                                    "${filteredResponse.loanProductName} of ${filteredResponse.loanAmount} is in progress from: ${filteredResponse.loanDate}",
                                    isError = true
                                )
                            )
                        }

                    }
                }
            } else {
                if (contentList!=null && contentList.isEmpty() && filteredResponse.isEmpty()) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    component.onProductSelected(
                        loanRefId = productRefId
                    )
                }
            }
        }
    }

    fun refresh() = refreshScope.launch {
        refreshing = true
        component.reloadModels()
        delay(1500)
        refreshing = false
    }

    if (state.isLoading){
        refreshScope.launch {
            refreshing = true
            delay(1500)
            refreshing = false
        }
    }
    val refreshState = rememberPullRefreshState(refreshing , ::refresh)

    Scaffold(modifier = Modifier.padding(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                val isError = (data.visuals as? CustomSnackBar)?.isError ?: false
                Snackbar(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .wrapContentHeight(),
                    containerColor = MaterialTheme.colorScheme.primary,
                    action = {
                        Text(text = "Resolve",
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .clickable {
                                    if (isError) data.dismiss() else data.performAction()
                                    launchHandleLoanRequestPopUp = true
                                }
                                .padding(end = 5.dp))
                    }
                ) {
                    Column() {
                        Text(
                            data.visuals.message,
                            modifier = Modifier
                                .padding(bottom = 10.dp, top = 10.dp, start = 5.dp)
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            fontSize = 12.sp
                        )
                    }

                }
            }
        },
        topBar = {
            NavigateBackTopBar("Select Loan Product",
                onClickContainer = {
                    component.onBackNavClicked()
                })
        },
        content = { innerPadding ->
            Box(Modifier.consumeWindowInsets(innerPadding).pullRefresh(refreshState)) {
                Column {
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
                                                text = "Resolve $loanName",
                                                modifier = Modifier.padding(top = 20.dp),
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                            )
                                            Text(
                                                text = "You can proceed to create a new loan request or resolve existing created on",
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
                                                //Proceed to re Apply the loan
                                                launchHandleLoanRequestPopUp = false
                                                component.onProductSelected(
                                                    loanRefId = productRefId
                                                )
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
                                                component.onResolveLoanSelected(loanRefId = productRefId)

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
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                        ) {

                            if (state.prestaLongTermLoanProducts?.total == null || state.isLoading) {
                                items(6) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
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
                                                .padding(top = 10.dp, bottom = 5.dp)
                                        ) {
                                            ProductSelectionCard(longTermLoanResponse.name.toString(),
                                                description = longTermLoanResponse.interestRate.toString(),
                                                onClickContainer = {
                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                    productRefId = ""
                                                    productRefId = longTermLoanResponse.refId.toString()
                                                    loanName = longTermLoanResponse.name.toString()
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
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.padding(bottom = 100.dp))
                            }
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing , refreshState,
                    Modifier
                        .padding(innerPadding)
                        .align(Alignment.TopCenter).zIndex(1f),
                    contentColor = actionButtonColor
                )

            }

        })

}