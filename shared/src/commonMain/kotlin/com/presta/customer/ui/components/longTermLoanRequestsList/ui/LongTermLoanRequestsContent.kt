package com.presta.customer.ui.components.longTermLoanRequestsList.ui

import ShimmerBrush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.longTermLoanRequestsList.LongTermLoanRequestsComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanRequestsContent(
    component: LongTermLoanRequestsComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State
) {
    val skipHalfExpanded by remember { mutableStateOf(true) }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    var memBerRefId by remember { mutableStateOf("") }
    var loanRequestRefId by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableStateOf(-1) }
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memBerRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
    val scope = rememberCoroutineScope()
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        LaunchedEffect(
            authState.cachedMemberData,
            memBerRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsList(
                    token = it.accessToken,
                    memberRefId = memBerRefId
                )

            }?.let {
                onEvent(
                    it
                )
            }
        }
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
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.95f)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 100.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                Icons.Filled.Cancel,
                                contentDescription = "Cancel  Arrow",
                                tint = backArrowColor,
                                modifier = Modifier.clickable {
                                    scope.launch { modalBottomSheetState.hide() }
                                }
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (state.prestaLoanByLoanRequestRefId?.loanProductName != null) state.prestaLoanByLoanRequestRefId.loanProductName + " " + formatMoney(
                                    state.prestaLoanByLoanRequestRefId.loanAmount
                                ) else "",
                                fontSize = 14.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )

                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = if (state.prestaLoanByLoanRequestRefId?.loanRequestProgress != null) state.prestaLoanByLoanRequestRefId.loanRequestProgress.toString() + "% DONE" else "",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )

                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "GUARANTORS  STATUS",
                                fontSize = 13.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )
                        }
                        //guarantor Listing
                        state.prestaLoanByLoanRequestRefId?.guarantorList?.mapIndexed { index, guarantorDataResponse ->
                            GuarantorListingContainer(
                                guarantorName = guarantorDataResponse.firstName.uppercase() + " " + guarantorDataResponse.lastName.uppercase(),
                                index = index,
                                onClick = { indexed: Int ->
                                    selectedIndex = if (selectedIndex == index) -1 else indexed
                                },
                                expandContent = selectedIndex == index,
                                committedAmount = formatMoney(guarantorDataResponse.committedAmount),
                                eligibilityStatus = guarantorDataResponse.eligibilityMessage,
                                guarantorShipStatus = if (guarantorDataResponse.isActive) "Active" else "Not Active",
                                signatureStatus = if (guarantorDataResponse.isSigned) "Signed" else "Pending",
                                acceptanceStatus = if (guarantorDataResponse.isAccepted) "Accepted " else "Pending",
                                onClickReplaceGuarantor = {
                                    component.navigateToReplaceGuarantor()
                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.padding(LocalSafeArea.current),
            topBar = {
                NavigateBackTopBar(label = "Loan Requests", onClickContainer = {
                    //navigate to profile
                    component.navigateToHome()
                })
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    //Requests List
                    item {
                        Spacer(modifier = Modifier.padding(innerPadding))
                    }
                    if (state.prestaLongTermLoansRequestsList?.content.isNullOrEmpty()) {
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
                    } else {
                        state.prestaLongTermLoansRequestsList?.content?.map { loanlistingData ->
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                ) {
                                    LongTermLoanRequestsListContainer(
                                        onClickContainer = {
                                            loanRequestRefId = loanlistingData.refId
                                            scope.launch { modalBottomSheetState.show() }
                                        },
                                        loanProductName = loanlistingData.loanProductName,
                                        applicationComplete = false,
                                        loanAmount = "Ksh. ${formatMoney(loanlistingData.loanAmount)}",
                                        loanApplicationProgress = loanlistingData.loanRequestProgress.toFloat() / 100,
                                        applicantSigned = if (loanlistingData.applicantSigned) "APPLICANT SIGNED" else "APPLICANT SIGN PENDING",
                                        applicantHasSigned = loanlistingData.applicantSigned
                                    )
                                }

                            }
                        }
                    }
                }
            })
    }
}

@Composable
fun GuarantorListingContainer(
    guarantorName: String,
    index: Int,
    onClick: (Int) -> Unit,
    expandContent: Boolean,
    committedAmount: String,
    eligibilityStatus: String,
    guarantorShipStatus: String,
    signatureStatus: String,
    acceptanceStatus: String,
    onClickReplaceGuarantor: () -> Unit,

) {
    var showExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ) {
        //Expand the content  of this container
        //Guarantor Listing
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                guarantorName,
                fontSize = 13.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
            )
            //progress indicator
            LinearProgressWithPercentage(progress = 0.5f,
                onClick = {
                    onClick.invoke(index)
                    showExpanded = !showExpanded
                })
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(start = 10.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .size(20.dp),
                onClick = {
                    onClick.invoke(index)
                    showExpanded = !showExpanded
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        modifier = if (expandContent) Modifier.size(
                            25.dp
                        )
                            .rotate(90F) else Modifier.size(25.dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
        AnimatedVisibility(expandContent) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxHeight()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Eligibility Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            eligibilityStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Committed Amount",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = committedAmount,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Guarantorship Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = guarantorShipStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Signature Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = signatureStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Approval Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = "",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Acceptance  Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = acceptanceStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Date Accepted",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = "",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                //Replace guarantor
                item {
                    OutlinedButton(
                        onClick = onClickReplaceGuarantor,
                        modifier = Modifier.padding(top = 16.dp),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.onBackground)
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Replace  Guarantor",
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanRequestsListContainer(
    onClickContainer: () -> Unit,
    loanProductName: String,
    applicationComplete: Boolean,
    loanAmount: String,
    loanApplicationProgress: Float,
    applicantSigned: String,
    applicantHasSigned: Boolean
) {
    val progress by remember { mutableStateOf(loanApplicationProgress) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(size = 25.dp),
        onClick = onClickContainer
    ) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.outline.copy(0.3f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 20.dp
                        )
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 10.dp, end = 10.dp,
                            top = 25.dp
                        )
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //show different icon based on the loan status
                    if (applicationComplete) {
                        Column() {
                            Icon(
                                Icons.Filled.DoneAll,
                                contentDescription = "completed",
                                modifier = Modifier.clip(shape = CircleShape)
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .padding(5.dp),
                                tint = MaterialTheme.colorScheme.background
                            )
                            Text(
                                "Completed",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(
                                    top = 10.dp,
                                    bottom = 25.dp
                                )
                            )
                        }
                    } else {
                        Column() {
                            LoanApplicationProgress(
                                progress = progress,
                                text = "${(progress * 100).toInt()}%",
                                modifier = Modifier.padding(start = 1.dp)
                            )
                            Text(
                                "In Progress",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(
                                    top = 10.dp,
                                    bottom = 25.dp
                                )
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .wrapContentHeight()
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = applicantSigned,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(
                                end = 15.dp,
                                top = 5.dp
                            ),
                            color = if (applicantHasSigned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column() {
                            Text(
                                text = loanProductName,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column() {
                            Text(
                                text = loanAmount,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 15.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "27/04/2023 08:32",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(end = 15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LinearProgressWithPercentage(
    progress: Float,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.8f)
            .clickable {
                onClick()
            }
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .height(10.dp)
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Accepted",
                fontSize = 7.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)

            )
            Text(
                "Signed",
                fontSize = 7.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
            )
            Text(
                "Approved",
                fontSize = 7.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
            )
        }
    }
}
@Composable
fun LoanApplicationProgress(
    progress: Float,
    text: String,
    strokeWidth: Dp = 2.dp,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(modifier = modifier.size(50.dp)) {
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = strokeWidth,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            modifier = Modifier.align(Alignment.Center)
        )
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = strokeWidth,
            color = color,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        )
    }
}