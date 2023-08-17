package com.presta.customer.ui.components.guarantorshipRequests.ui

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.guarantorshipRequests.GuarantorshipRequestComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GuarantorShipRequestsContent(
    component: GuarantorshipRequestComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    val skipHalfExpanded by remember { mutableStateOf(true) }
    val modalBottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    val modalBottomScope = rememberCoroutineScope()
    var loanRequestRefId by remember { mutableStateOf("") }
    var memberRefId by remember { mutableStateOf("") }
    var guarantorshipRequestRefId by remember { mutableStateOf("") }
    var amountToGuarantee by remember { mutableStateOf("") }
    var loanNumber by remember { mutableStateOf("") }
    if(signHomeState.prestaTenantByPhoneNumber?.refId!=null){
        memberRefId= signHomeState.prestaTenantByPhoneNumber.refId
    }
    if (loanRequestRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            loanRequestRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoanRequestByRefId(
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
    if (memberRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            memberRefId

        ) {
            //Todo------test
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaGuarantorshipRequests(
                    token = it.accessToken,
                    memberRefId ="CQ4uQ8vfcXF712SD" //memberRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Contacts,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = CircleShape)
                            .border(width = 1.dp, color = actionButtonColor),
                        tint = actionButtonColor
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .background(
                            brush = ShimmerBrush(
                                targetValue = 1300f,
                                showShimmer = state.prestaLongTermLoanrequestBYRefId?.memberFirstName == null
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ).defaultMinSize(200.dp),
                    text = if (state.prestaLongTermLoanrequestBYRefId?.memberFirstName !== null) "${state.prestaLongTermLoanrequestBYRefId.memberFirstName.uppercase()} ${state.prestaLongTermLoanrequestBYRefId.memberLastName.uppercase()}" else "",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                    textAlign = TextAlign.Center,

                    )
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .background(
                            brush = ShimmerBrush(
                                targetValue = 1300f,
                                showShimmer = authState.authUserResponse?.companyName == null
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ).defaultMinSize(200.dp),
                    text = if (authState.authUserResponse !== null) authState.authUserResponse.companyName else "",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Kindly accept my  request to add you ",
                        modifier = Modifier
                            .padding(top = 20.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        " as a guarantor for this loan product valued:",
                        modifier = Modifier,
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .background(
                            brush = ShimmerBrush(
                                targetValue = 1300f,
                                showShimmer = state.prestaLongTermLoanrequestBYRefId?.loanAmount == null
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ).defaultMinSize(200.dp),
                    text = if (state.prestaLongTermLoanrequestBYRefId?.loanAmount !== null) "KES " + formatMoney(
                        state.prestaLongTermLoanrequestBYRefId.loanAmount
                    ) else "",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clickable {
//                                    authState.cachedMemberData?.let {
//                                        ApplyLongTermLoansStore.Intent.GetGuarantorAcceptanceStatus(
//                                            token = it.accessToken,
//                                            guarantorshipRequestRefId = guarantorshipRequestRefId,
//                                            isAccepted = true
//                                        )
//                                    }?.let {
//                                        onEvent(
//                                            it
//                                        )
//                                    }
                                    component.onAcceptSelected(
                                        loanNumber = loanNumber,
                                        amount = if (amountToGuarantee != "") amountToGuarantee.toDouble() else 0.0,
                                        loanRequestRefId = loanRequestRefId
                                    )
                                    modalBottomScope.launch { modalBottomState.hide() }
                                }
                                .clip(shape = CircleShape),
                            tint = Color.Green.copy(alpha = 0.5f)
                        )
                        Text(
                            "Accept ",
                            modifier = Modifier
                                .padding(top = 10.dp),
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            color = Color.Green.copy(alpha = 0.5f)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clickable {

                                    //Request Declined
//                                    authState.cachedMemberData?.let {
//                                        ApplyLongTermLoansStore.Intent.GetGuarantorAcceptanceStatus(
//                                            token = it.accessToken,
//                                            guarantorshipRequestRefId = guarantorshipRequestRefId,
//                                            isAccepted = false
//                                        )
//                                    }?.let {
//                                        onEvent(
//                                            it
//                                        )
//                                    }
                                    modalBottomScope.launch { modalBottomState.hide() }
                                }
                                .clip(shape = CircleShape),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                        Text(
                            "Decline ",
                            modifier = Modifier
                                .padding(top = 10.dp),
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
            NavigateBackTopBar("Guarantorship Requests", onClickContainer = {
                component.onBackNavClicked()
            })
        }, content = { innerPadding ->
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
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
                        if (state.prestaGuarontorshipRequests.isEmpty()) {
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
                            state.prestaGuarontorshipRequests.map { guarantorRequests ->
                                item {
                                    Row(modifier = Modifier.padding(top = 10.dp)) {
                                        GuarantorsRequestsView(
                                            applicant = "${guarantorRequests.applicant.firstName} ${guarantorRequests.applicant.lastName}",
                                            loanNumber = guarantorRequests.loanRequest.loanNumber,
                                            loanAmount = guarantorRequests.loanRequest.amount.toString(),
                                            requestsDate = guarantorRequests.loanRequest.loanDate,
                                            onClickContainer = {
                                                loanRequestRefId =
                                                    guarantorRequests.loanRequest.refId
                                                guarantorshipRequestRefId = guarantorRequests.refId
                                                amountToGuarantee =
                                                    guarantorRequests.loanRequest.amount.toString()
                                                loanNumber =
                                                    guarantorRequests.loanRequest.loanNumber
                                                println("Test Data" + state.prestaLongTermLoanrequestBYRefId?.memberFirstName)
                                                modalBottomScope.launch { modalBottomState.show() }
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

            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorsRequestsView(
    applicant: String,
    loanNumber: String,
    loanAmount: String,
    requestsDate: String,
    onClickContainer: () -> Unit
) {
    ElevatedCard(
        onClick = onClickContainer,
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Contacts,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(shape = CircleShape)
                        .padding(start = 10.dp),
                    tint = actionButtonColor
                )
                Column(modifier = Modifier.padding(start = 30.dp)) {
                    Text(
                        "${applicant.uppercase()} requested you  to",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        color = actionButtonColor,
                        fontSize = 12.sp
                    )
                    Text(
                        "guarantee their loan $loanNumber of",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp
                    )
                    Text(
                        loanAmount,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        color = actionButtonColor,
                        fontSize = 12.sp
                    )
                    Text(
                        requestsDate,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
