package com.presta.customer.ui.components.witnessRequests.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.guarantorshipRequests.ui.CustomButton
import com.presta.customer.ui.components.guarantorshipRequests.ui.GuarantorsRequestsView
import com.presta.customer.ui.components.guarantorshipRequests.ui.RequestsDataListing
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.witnessRequests.WitnessRequestComponent
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    val skipHalfExpanded by remember { mutableStateOf(true) }
    val modalBottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    val modalBottomScope = rememberCoroutineScope()

    if (loanRequestRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            loanRequestRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLoanByLoanRequestRefId(
                    token = it.accessToken,
                    loanRequestRefId = loanRequestRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memberRefId = signHomeState.prestaTenantByPhoneNumber.refId
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
    val filtered = state.prestaWitnessRequests.filter { witnessResponse ->
        witnessResponse.loanRequest.refId == loanRequestRefId
    }
    if (loanRequestRefId != "") {
        LaunchedEffect(
            state.prestaWitnessAcceptanceStatus,
            state.isLoading
        ) {
            if (loanRequestRefId == state.prestaWitnessAcceptanceStatus?.loanRequest?.refId) {
                if (state.prestaWitnessAcceptanceStatus.witnessAccepted) {
                    signHomeState.prestaTenantByPhoneNumber?.refId?.let {
                        component.onAcceptSelected(
                            loanNumber = loanNumber,
                            amount = if (amountToGuarantee != "") amountToGuarantee.toDouble() else 0.0,
                            loanRequestRefId = loanRequestRefId,
                            memberRefId = it,
                            witnessRefId = witnessRequestRefId
                        )

                    }
                    modalBottomScope.launch { modalBottomState.hide() }

                }
            }
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp)
                ) {
                    Text(
                        text = "Witness Request",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
                RequestsDataListing(
                    label = "Loan Date",
                    showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                    value = if (state.prestaLoanByLoanRequestRefId?.memberFirstName !== null) state.prestaLoanByLoanRequestRefId.loanDate else "",
                    labelFontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
                RequestsDataListing(
                    label = "Applicant",
                    showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                    value = if (state.prestaLoanByLoanRequestRefId?.memberFirstName !== null) "${state.prestaLoanByLoanRequestRefId.memberFirstName.uppercase()} ${state.prestaLoanByLoanRequestRefId.memberLastName.uppercase()}" else "",
                    labelFontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
                RequestsDataListing(
                    label = "Application Amount",
                    showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                    value = if (state.prestaLoanByLoanRequestRefId?.memberFirstName !== null) state.prestaLoanByLoanRequestRefId.loanAmount.toString() else "",
                    labelFontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
                filtered.map { filteredResponse ->

                    RequestsDataListing(
                        label = "Loan Status",
                        showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                        value = if (state.prestaLoanByLoanRequestRefId?.isActive == true) "Active" else "InActive",
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )

                    RequestsDataListing(
                        label = "Acceptance Status",
                        showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                        icon = if (filteredResponse.witnessAccepted) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        iconColor = if (filteredResponse.witnessAccepted) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.error.copy(
                            alpha = 0.8f
                        ),
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )

                }
                filtered.map { filteredResponse ->
                    RequestsDataListing(
                        label = "Signing Status",
                        showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                        icon = if (filteredResponse.witnessSigned) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        iconColor = if (filteredResponse.witnessSigned) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.error.copy(
                            alpha = 0.8f
                        ),
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }

                filtered.map { filteredResponse ->
                    if (!filteredResponse.witnessSigned) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(shape = CircleShape),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Kindly click below to sign Witness Request",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light), // Replace with your font resource
                                lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp)
                            )
                        }
                    }
                }
                filtered.map { filteredResponse ->
                    if (!filteredResponse.witnessSigned) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CustomButton(
                                    label = "Accept",
                                    onClickContainer = {
                                        authState.cachedMemberData?.let {
                                            ApplyLongTermLoansStore.Intent.GetWitnessAcceptanceStatus(
                                                token = it.accessToken,
                                                loanRequestRefId = loanRequestRefId,
                                                isAccepted = true
                                            )
                                        }?.let {
                                            onEvent(
                                                it
                                            )
                                        }

                                    },
                                    loading = state.isLoading,
                                    enabled = true,
                                    color = MaterialTheme.colorScheme.tertiaryContainer
                                )
                                Spacer(modifier = Modifier.padding(top = 10.dp))
                                CustomButton(
                                    label = "Decline",
                                    onClickContainer = {
                                        authState.cachedMemberData?.let {
                                            ApplyLongTermLoansStore.Intent.GetWitnessAcceptanceStatus(
                                                token = it.accessToken,
                                                loanRequestRefId = loanRequestRefId,
                                                isAccepted = false
                                            )
                                        }?.let {
                                            onEvent(
                                                it
                                            )
                                        }

                                    },
                                    loading = false,
                                    enabled = true,
                                    color = MaterialTheme.colorScheme.error
                                )

                            }
                        }

                    }
                }

            }
        }
    ) {
        Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
            NavigateBackTopBar("Witness Requests", onClickContainer = {
                component.onBackNavClicked()
            })
        }, content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
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
                                            requestsDate = witnessRequestResponse.loanDate,
                                            onClickContainer = {
                                                loanRequestRefId =
                                                    witnessRequestResponse.loanRequest.refId
                                                witnessRequestRefId =
                                                    witnessRequestResponse.memberRefId
                                                amountToGuarantee =
                                                    witnessRequestResponse.loanRequest.amount.toString()
                                                loanNumber =
                                                    witnessRequestResponse.loanRequest.loanNumber
                                                println("Test Data" + state.prestaLongTermLoanrequestBYRefId?.memberFirstName)
                                                modalBottomScope.launch { modalBottomState.show() }
                                            },
                                            action = "witness"
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
            }
        })
    }

}