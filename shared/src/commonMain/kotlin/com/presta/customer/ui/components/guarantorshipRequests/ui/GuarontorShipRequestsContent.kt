package com.presta.customer.ui.components.guarantorshipRequests.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.guarantorshipRequests.GuarantorshipRequestComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    var guarantorRefIdRefId by remember { mutableStateOf("") }
    var amountToGuarantee by remember { mutableStateOf("") }
    var loanNumber by remember { mutableStateOf("") }
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memberRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
    val filtered =
        state.prestaLoanByLoanRequestRefId?.guarantorList?.filter { guarantorDataResponse ->
            guarantorDataResponse.memberRefId == memberRefId
        }
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
    if (memberRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            memberRefId

        ) {
            //Todo------test
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaGuarantorshipRequests(
                    token = it.accessToken,
                    memberRefId = memberRefId, //memberRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    //Todo-- if  the guarantorship Request is  Accepted proceed to sign
    if (loanRequestRefId != "") {
        LaunchedEffect(
            state.prestaGuarontorAcceptanceStatus
        ) {
            if (loanRequestRefId == state.prestaGuarontorAcceptanceStatus?.loanRequest?.refId) {
                if (state.prestaGuarontorAcceptanceStatus.isAccepted) {
                    signHomeState.prestaTenantByPhoneNumber?.refId?.let {
                        component.onAcceptSelected(
                            loanNumber = loanNumber,
                            amount = if (amountToGuarantee != "") amountToGuarantee.toDouble() else 0.0,
                            loanRequestRefId = loanRequestRefId,
                            memberRefId = it,
                            guarantorRefId = guarantorRefIdRefId
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
                    .fillMaxHeight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp)
                ) {
                    Text(
                        text = "Guarantorship Request",
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
                    labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                    valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
                RequestsDataListing(
                    label = "Applicant",
                    showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                    value = if (state.prestaLoanByLoanRequestRefId?.memberFirstName !== null) "${state.prestaLoanByLoanRequestRefId.memberFirstName.uppercase()} ${state.prestaLoanByLoanRequestRefId.memberLastName.uppercase()}" else "",
                    labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                    valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
                RequestsDataListing(
                    label = "Application Amount",
                    showShimmer = state.prestaLoanByLoanRequestRefId?.memberFirstName == null,
                    value = if (state.prestaLoanByLoanRequestRefId?.memberFirstName !== null) state.prestaLoanByLoanRequestRefId.loanAmount.toString() else "",
                    labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                    valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
                filtered?.map { filteredResponse ->
                    RequestsDataListing(
                        label = "Commited Amount",
                        showShimmer = false,
                        value = filteredResponse.committedAmount.toString(),
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                    RequestsDataListing(
                        label = "Date Requested",
                        showShimmer = false,
                        value = state.prestaLoanByLoanRequestRefId.loanDate,
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                    (filteredResponse.dateAccepted)?.let {
                        RequestsDataListing(
                            label = "Date  Accepted",
                            showShimmer = false,
                            value = it,
                            labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                            valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                    RequestsDataListing(
                        label = "Loan Status",
                        showShimmer = false,
                        value = if (state.prestaLoanByLoanRequestRefId.isActive) "Active" else "InActive",
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                    RequestsDataListing(
                        label = "Acceptance Status",
                        showShimmer = false,
                        icon = if (filteredResponse.isAccepted == true) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        iconColor = if (filteredResponse.isAccepted == true) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.error.copy(
                            alpha = 0.8f
                        ),
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )

                }
                filtered?.map { filteredResponse ->
                    RequestsDataListing(
                        label = "Signing Status",
                        showShimmer = false,
                        icon = if (filteredResponse.isSigned == true) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        iconColor = if (filteredResponse.isSigned == true) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.error.copy(
                            alpha = 0.8f
                        ),
                        labelFontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        valueFontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }

                filtered?.map { filteredResponse ->
                    if (filteredResponse.isSigned == false) {

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
                                text = "Kindly click below to sign Guarantorship Request",
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
                filtered?.map { filteredResponse ->
                    if (filteredResponse.isSigned == false) {
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
                                            ApplyLongTermLoansStore.Intent.GetGuarantorAcceptanceStatus(
                                                token = it.accessToken,
                                                guarantorshipRequestRefId = guarantorshipRequestRefId,
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
                                            ApplyLongTermLoansStore.Intent.GetGuarantorAcceptanceStatus(
                                                token = it.accessToken,
                                                guarantorshipRequestRefId = guarantorshipRequestRefId,
                                                isAccepted = false
                                            )
                                        }?.let {
                                            onEvent(
                                                it
                                            )
                                        }
                                        modalBottomScope.launch { modalBottomState.hide() }

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
                                                guarantorRefIdRefId = guarantorRequests.refId
                                                println("Test Data" + state.prestaLongTermLoanrequestBYRefId?.memberFirstName)
                                                modalBottomScope.launch { modalBottomState.show() }
                                            },
                                            action = "guarantee"
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
    onClickContainer: () -> Unit,
    action: String
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
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(shape = CircleShape)
                        .padding(start = 10.dp),
                    tint = MaterialTheme.colorScheme.outline.copy(0.5f)
                )
                Column(modifier = Modifier.padding(start = 30.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "${applicant.uppercase()}  ",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            color = actionButtonColor,
                            fontSize = 12.sp
                        )
                        Text(
                            "requested you  to",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Text(
                        "$action their loan $loanNumber of",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        "Kshs $loanAmount",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        color = actionButtonColor,
                        fontSize = 12.sp
                    )
                    Text(
                        requestsDate,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun RequestsDataListing(
    label: String,
    showShimmer: Boolean,
    value: String? = null,
    icon: ImageVector? = null,
    iconColor: Color? = null,
    labelFontFamily: FontFamily,
    valueFontFamily: FontFamily
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row {
            Column {
                Text(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 8.dp, minWidth = 150.dp),
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = labelFontFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Column {
            if (icon != null) {

                if (iconColor != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape),
                        tint = iconColor
                    )
                }
            } else {
                if (value != null) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                            .background(
                                brush = ShimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = showShimmer
                                ),
                                shape = RoundedCornerShape(12.dp),
                            ),
                        text = value,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = valueFontFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomButton(
    label: String,
    onClickContainer: () -> Unit,
    loading: Boolean = false,
    enabled: Boolean = true,
    color: Color
) {
    ElevatedCard(
        onClick = { if (!loading && enabled) onClickContainer() },
        modifier = Modifier.fillMaxWidth().alpha(if (!loading && enabled) 1f else 0.5f),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 11.dp, bottom = 11.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp).padding(end = 2.dp),
                    color = Color.White
                )
            }
            Text(
                text = label,
                modifier = Modifier
                    .padding(start = 5.dp),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                    letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                ),
                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
            )
        }
    }
}



