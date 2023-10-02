package com.presta.customer.ui.components.signGuarantorForm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signGuarantorForm.SignGuarantorFormComponent
import com.presta.customer.ui.components.signLoanForm.ui.Base64ToImage
import com.presta.customer.ui.components.signLoanForm.ui.ViewWebView
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun SignGuarantorFormContent(
    component: SignGuarantorFormComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    onDocumentSigned: (
        loanNumber: String,
        amount: Double
    ) -> Unit,
    loanNumber: String,
    amount: Double
) {
    var webViewDisposed by remember { mutableStateOf(false) }
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        refreshing = false
    }
    if (state.isLoading) {
        refreshScope.launch {
            refreshing = true
            delay(1500)
            refreshing = false
        }
    }
    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    LaunchedEffect(
        authState.cachedMemberData,
        component.loanRequestRefId,
        state.prestaGuarontorshipRequests
    ) {
        if (component.loanRequestRefId != "") {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLoanByLoanRequestRefId(
                    token = it.accessToken,
                    loanRequestRefId = component.loanRequestRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }

    LaunchedEffect(state.prestaLoanByLoanRequestRefId, webViewDisposed) {
        val filteredResponse: PrestaGuarantorResponse? =
            state.prestaGuarontorshipRequests.find { loadedData ->
                loadedData.loanRequest.loanNumber == component.loanNumber
            }
        println("This is filted::::::::;;;;;" + filteredResponse)
        if (filteredResponse != null) {
            if (filteredResponse.isSigned == true) {
                onDocumentSigned(loanNumber, amount)
            }else{
                if (webViewDisposed &&  filteredResponse.isSigned == false) {
                    println("SHOW DOCUMENT NOT SIGNED")
                }
            }
        }
    }

    if (state.prestaZohoSignUrl !== null) {
        ViewWebView(state.prestaZohoSignUrl.signURL, onEvent, disposed = {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLoanByLoanRequestRefId(
                    token = it.accessToken,
                    loanRequestRefId = component.loanRequestRefId
                )
            }?.let {
                onEvent(
                    it
                )

                webViewDisposed = true
            }
        }, created = {
            webViewDisposed = false
        })
    } else {
        Scaffold(topBar = {
            NavigateBackTopBar("Sign Document", onClickContainer = {
                component.onBackNavClicked()
            })
        }, content = { innerPadding ->
            Box(Modifier.consumeWindowInsets(innerPadding).pullRefresh(refreshState)) {
                if (state.prestaLoanByLoanRequestRefId?.pdfThumbNail == null) {
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(6) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
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
                    }

                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.85f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    "Your guarantorship " + component.loanNumber + " of KES " + component.amount + " has been confirmed",
                                    fontSize = 16.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                "Proceed Below to sign your form",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            //Form Image
                            if (state.prestaLoanByLoanRequestRefId.pdfThumbNail != "") {
                                Base64ToImage(state.prestaLoanByLoanRequestRefId.pdfThumbNail)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            ActionButton(
                                label = "SIGN DOCUMENT",
                                onClickContainer = {
                                    authState.cachedMemberData?.let {
                                        ApplyLongTermLoansStore.Intent.GetZohoSignUrl(
                                            token = it.accessToken,
                                            loanRequestRefId = component.loanRequestRefId,
                                            actorRefId = component.memberRefId,
                                            actorType = ActorType.GUARANTOR
                                        )
                                    }?.let {
                                        onEvent(
                                            it
                                        )
                                    }
                                },
                                loading = state.isLoading
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing, refreshState,
                    Modifier
                        .padding(innerPadding)
                        .align(Alignment.TopCenter).zIndex(1f),
                    contentColor = actionButtonColor
                )
            }
        })
    }
}