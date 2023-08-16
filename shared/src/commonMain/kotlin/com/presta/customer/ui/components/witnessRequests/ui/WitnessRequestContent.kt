package com.presta.customer.ui.components.witnessRequests.ui

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.guarantorshipRequests.ui.GuarantorsRequestsView
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.witnessRequests.WitnessRequestComponent
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WitnessRequestContent(
    component: WitnessRequestComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit
) {
    var memberRefId by remember { mutableStateOf("") }
    var loanRequestRefId by remember { mutableStateOf("") }
    var witnessRequestRefId by remember { mutableStateOf("") }
    var amountToGuarantee by remember { mutableStateOf("") }
    var loanNumber by remember { mutableStateOf("") }

    if(signHomeState.prestaTenantByPhoneNumber?.refId!=null){
        memberRefId= signHomeState.prestaTenantByPhoneNumber.refId
    }
    if (memberRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            memberRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaWitnessRequests(
                    token = it.accessToken,
                    memberRefId = memberRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }

    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Witness Requests", onClickContainer = {
            component.onBackNavClicked()
        })
    }, content = { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                items(5) {
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
            } else {
                if (state.prestaWitnessRequests.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 70.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Inventory2,
                                    modifier = Modifier
                                        .size(70.dp),
                                    contentDescription = "No data",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                            Text(
                                "Whoops",
                                fontSize = 13.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )
                            Text(
                                "No Data",
                                fontSize = 10.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                            )
                        }
                    }
                } else {
                    state.prestaWitnessRequests.map { witnessRequestResponse ->
                        item {
                            Row(modifier = Modifier.padding(top = 10.dp)) {
                                GuarantorsRequestsView(
                                    applicant = "${witnessRequestResponse.applicant.firstName} ${witnessRequestResponse.applicant.lastName}",
                                    loanNumber = witnessRequestResponse.loanRequest.loanNumber,
                                    loanAmount = witnessRequestResponse.loanRequest.amount.toString(),
                                    requestsDate = witnessRequestResponse.loanRequest.loanDate,
                                    onClickContainer = {
                                        loanRequestRefId =
                                            witnessRequestResponse.loanRequest.refId
                                        witnessRequestRefId = witnessRequestResponse.memberRefId
                                        amountToGuarantee =
                                            witnessRequestResponse.loanRequest.amount.toString()
                                        loanNumber =
                                            witnessRequestResponse.loanRequest.loanNumber
                                        println("Test Data" + state.prestaLongTermLoanrequestBYRefId?.memberFirstName)
                                        //modalBottomScope.launch { modalBottomState.show() }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    })

}