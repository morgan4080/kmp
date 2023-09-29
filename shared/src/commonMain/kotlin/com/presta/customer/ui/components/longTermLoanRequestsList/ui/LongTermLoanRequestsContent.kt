package com.presta.customer.ui.components.longTermLoanRequestsList.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.ui.SnackbarVisualsWithError
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.longTermLoanRequestsList.LongTermLoanRequestsComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class
)
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
    var loanAmount by remember { mutableStateOf("") }
    var showVoidError by remember { mutableStateOf(false) }
    var loanNumber by remember { mutableStateOf("") }
    var loanRequestNumber by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableStateOf(-1) }
    var guarantorFirstName by remember { mutableStateOf("") }
    var guarantorLastName by remember { mutableStateOf("") }
    var guarantorRefId by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memBerRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
    var deleteInitiated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(showVoidError) {
        if (showVoidError) {
            delay(4000)
            showVoidError = false
        }
    }

    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        LaunchedEffect(
            authState.cachedMemberData,
            memBerRefId,
            state.deleteLoanRequestResponse,
            refreshing
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
    if (deleteInitiated) {
        LaunchedEffect(state.deleteLoanRequestResponse) {
            if (state.deleteLoanRequestResponse.toString() == "SUCCESS") {
                snackBarScope.launch {
                    snackbarHostState.showSnackbar(
                        SnackbarVisualsWithError(
                            "Loan Deleted successfully",
                            isError = true
                        )
                    )
                }
                deleteInitiated = false
                scope.launch { modalBottomSheetState.hide() }
            }
        }
    }
    //Todo ---add pull to refresh
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        refreshing = false
    }

    if (loanRequestRefId == "") {
        scope.launch { modalBottomSheetState.hide() }
    }
    val refreshState = rememberPullRefreshState(refreshing, ::refresh)


    LaunchedEffect(state.loanRefId, state.prestaLongTermLoansRequestsList) {
        if (state.loanRefId !== "" && state.prestaLongTermLoansRequestsList !== null) {
            if (state.prestaLongTermLoansRequestsList.content.isNotEmpty()) {
                loanRequestRefId = state.loanRefId

                val lr = state.prestaLongTermLoansRequestsList.content.find { request ->
                    request.refId == state.loanRefId
                }

                if (lr !== null) {
                    loanAmount =
                        lr.loanAmount.toString()
                    loanNumber = lr.loanRequestNumber
                    loanRequestNumber =
                        lr.loanRequestNumber

                    modalBottomSheetState.show()
                }
            }
        }
    }


    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContentColor = MaterialTheme.colorScheme.background,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Request Details ".uppercase() + if (state.prestaLoanByLoanRequestRefId !== null) "(${state.prestaLoanByLoanRequestRefId.loanRequestNumber})" else "",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                            fontSize = 14.sp
                        )

                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = "Cancel  Arrow",
                            tint = MaterialTheme.colorScheme.primary.copy(0.7f),
                            modifier = Modifier.clickable {
                                scope.launch { modalBottomSheetState.hide() }
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (state.isLoading || state.prestaLoanByLoanRequestRefId?.loanProductName == null) {
                            shimmerTextContainer(showLoadingShimmer = state.isLoading)
                            shimmerTextContainer(showLoadingShimmer = state.isLoading)
                        } else {
                            Text(
                                text = state.prestaLoanByLoanRequestRefId.loanProductName,
                                fontSize = 14.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
                            )

                            Text(
                                text = state.prestaLoanByLoanRequestRefId.applicationStatus.toString()
                                    .lowercase(),
                                fontSize = 14.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (state.prestaLoanByLoanRequestRefId?.loanAmount != null) " KES " + formatMoney(
                                state.prestaLoanByLoanRequestRefId.loanAmount
                            ).uppercase() else "",
                            fontSize = 16.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                        )

                        Text(
                            text = if (state.prestaLoanByLoanRequestRefId?.loanRequestProgress != null) state.prestaLoanByLoanRequestRefId.loanRequestProgress.toString() + "%" else "",
                            fontSize = 16.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, top = 10.dp)
                    ) {
                        if (state.prestaLoanByLoanRequestRefId?.applicantSigned == false || !state.prestaLoanByLoanRequestRefId?.pendingReason.isNullOrEmpty()) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch { modalBottomSheetState.hide() }
                                    if (loanAmount != "") {
                                        signHomeState.prestaTenantByPhoneNumber?.let {
                                            component.navigateToSignLoanForm(
                                                loanNumber = loanNumber,
                                                amount = loanAmount.toDouble(),
                                                loanRequestRefId = loanRequestRefId,
                                                memberRefId = it.refId,
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.height(30.dp),
                                shape = CircleShape,
                                border = BorderStroke(
                                    0.1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.8f
                                    )
                                )
                            ) {
                                Text(
                                    text = "Sign Form",
                                    color = MaterialTheme.colorScheme.background,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )
                            }

                            Spacer(modifier = Modifier.width(5.dp))
                        }

                        println(":::::state.prestaLoanByLoanRequestRefId?.applicationStatus:::::::")
                        println(state.prestaLoanByLoanRequestRefId?.applicationStatus)

                        if (state.prestaLoanByLoanRequestRefId !== null && state.prestaLoanByLoanRequestRefId.loanRequestProgress < 100) {
                            OutlinedButton(
                                onClick = {
                                    val dateStr = state.prestaLoanByLoanRequestRefId.loanDate

                                    // Split the input string into date and time parts
                                    val dateAndTimeParts = dateStr.split(" ")

                                    // Split the date part into day, month, and year
                                    val dateParts = dateAndTimeParts[0].split("/")

                                    // Split the time part into hours and minutes
                                    val timeParts = dateAndTimeParts[1].split(":")

                                    // Extract components
                                    val year = dateParts[2].toInt()
                                    val month = dateParts[1].toInt()
                                    val day = dateParts[0].toInt()
                                    val hours = timeParts[0].toInt()
                                    val minutes = timeParts[1].toInt()

                                    // Create a LocalDateTime object
                                    val localDateTime =
                                        LocalDateTime(year, month, day, hours, minutes)

                                    // Convert it to ISO string format
                                    val isoString = localDateTime.toInstant(TimeZone.UTC)
                                        .toLocalDateTime(TimeZone.UTC).toString()
                                    val todayInstant = Clock.System.now().toLocalDateTime(
                                        TimeZone.currentSystemDefault()
                                    ).toInstant(TimeZone.currentSystemDefault())

                                    val loanDateInstant =
                                        LocalDateTime
                                            .parse(
                                                isoString
                                            )
                                            .toInstant(
                                                TimeZone.currentSystemDefault()
                                            )

                                    val olderThan1month =
                                        todayInstant.minus(loanDateInstant) > 30.days

                                    if (state.prestaLoanByLoanRequestRefId.applicantSigned !== null
                                        &&
                                        state.prestaLoanByLoanRequestRefId.applicantSigned && !olderThan1month
                                    ) {
                                        showVoidError = true
                                    } else {
                                        if (loanRequestNumber != "") {
                                            authState.cachedMemberData?.let {
                                                ApplyLongTermLoansStore.Intent.DeleteLoanRequest(
                                                    token = it.accessToken,
                                                    loanRequestNumber = loanRequestNumber
                                                )

                                            }?.let {
                                                onEvent(
                                                    it
                                                )
                                            }
                                        }
                                        deleteInitiated = true
                                    }
                                },
                                modifier = Modifier
                                    .height(30.dp),
                                shape = CircleShape,
                                border = BorderStroke(
                                    0.1.dp,
                                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.error.copy(
                                        alpha = 0.7f
                                    )
                                )
                            ) {
                                Text(
                                    text = "Void request",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(showVoidError) {
                        Row(
                            modifier = Modifier
                                .padding(top = 15.dp, bottom = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Voiding not allowed: Your loan request has already been signed and is in-progress.",
                                color = MaterialTheme.colorScheme.error.copy(
                                    alpha = 0.7f
                                ),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                            )
                        }
                    }
                    AnimatedVisibility(state.replacedGuarantor != "") {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                        ) {
                            Box(
                                modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.3f
                                    )
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(top = 9.dp, bottom = 9.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(shape = CircleShape)
                                            .padding(start = 10.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Changed guarantor will take effect when the added guarantor accepts request.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                        color = MaterialTheme.colorScheme.outline,
                                    )

                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Guarantor Status",
                            fontSize = 13.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )

                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        state.prestaLoanByLoanRequestRefId?.guarantorList?.mapIndexed { index, guarantorDataResponse ->
                            val guarantorProgress = when {
                                guarantorDataResponse.isAccepted == true && guarantorDataResponse.isSigned == false && guarantorDataResponse.isApproved == false -> 0.3f
                                guarantorDataResponse.isSigned == true && guarantorDataResponse.isAccepted == true && guarantorDataResponse.isApproved == false -> 0.6f
                                guarantorDataResponse.isApproved == true && guarantorDataResponse.isSigned == true && guarantorDataResponse.isAccepted == true -> 1f
                                else -> 0.0f
                            }
                            val approvalStatus = when (guarantorDataResponse.isApproved) {
                                true -> "Approved"
                                false -> "Declined"
                                else -> "Pending"
                            }
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(bottom = 10.dp)
                                ) {
                                    GuarantorDataCard(
                                        loanProductName = guarantorDataResponse.firstName + " " + guarantorDataResponse.lastName,
                                        loanAmount = "KES " + guarantorDataResponse.committedAmount.toString(),
                                        loanApplicationProgress = guarantorProgress,
                                        onClick = { indexed: Int ->
                                            selectedIndex =
                                                if (selectedIndex == index) -1 else indexed
                                            guarantorFirstName = guarantorDataResponse.firstName
                                            guarantorLastName = guarantorDataResponse.lastName
                                            guarantorRefId = guarantorDataResponse.refId
                                        },
                                        expandContent = selectedIndex == index,
                                        index = index,
                                        dataListingColumn = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Eligibility Status",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    guarantorDataResponse.eligibilityMessage?.let {
                                                        Text(
                                                            it,
                                                            fontSize = 12.sp,
                                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                        )
                                                    }
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Committed Amount",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    Text(
                                                        text = formatMoney(guarantorDataResponse.committedAmount),
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Guarantorship Status",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    Text(
                                                        text = if (guarantorDataResponse.isActive) "Active" else "Not Active",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Acceptance  Status",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    Text(
                                                        text = if (guarantorDataResponse.isAccepted == true) "Accepted " else "Pending",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Date Accepted",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    Text(
                                                        text = guarantorDataResponse.dateAccepted
                                                            ?: " ",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Signature Status",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    Text(
                                                        text = if (guarantorDataResponse.isSigned == true) "Signed" else "Pending",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Approval Status",
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                                    )
                                                    Text(
                                                        text = approvalStatus,
                                                        fontSize = 12.sp,
                                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    OutlinedButton(
                                                        onClick = {
                                                            //Todo--proceed to replace  guaarantor
                                                            scope.launch {
                                                                modalBottomSheetState.hide()
                                                            }
                                                            component.navigateToReplaceGuarantor(
                                                                loanRequestRefId = loanRequestRefId,
                                                                guarantorRefId = guarantorRefId,
                                                                guarantorFirstname = guarantorFirstName,
                                                                guarantorLastName = guarantorLastName
                                                            )

                                                        },
                                                        modifier = Modifier.padding(top = 16.dp),
                                                        shape = CircleShape,
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary
                                                        ),
                                                        colors = ButtonDefaults.outlinedButtonColors(
                                                            containerColor = MaterialTheme.colorScheme.primary
                                                        )
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
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(bottom = 100.dp))
                        }
                    }
                }
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.padding(LocalSafeArea.current),
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    val isError = (data.visuals as? SnackbarVisualsWithError)?.isError ?: false
                    Snackbar(
                        modifier = Modifier.padding(bottom = 80.dp, start = 16.dp, end = 16.dp),
                        action = {
                            androidx.compose.material.IconButton(
                                onClick = { if (isError) data.dismiss() else data.performAction() },
                            ) {
                                Icon(
                                    Icons.Filled.Cancel,
                                    contentDescription = "Cancel  Arrow",
                                    tint = backArrowColor,
                                    modifier = Modifier.clickable {
                                        if (isError) data.dismiss() else data.performAction()
                                    }
                                )
                            }
                        }
                    ) {
                        Text(data.visuals.message)
                    }
                }
            },
            topBar = {
                NavigateBackTopBar(label = "Loan Requests", onClickContainer = {
                    component.navigateToHome()
                })
            },
            content = { innerPadding ->
                Box(Modifier.consumeWindowInsets(innerPadding).pullRefresh(refreshState)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.95f)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        //Requests List
                        item {
                            Spacer(modifier = Modifier.padding(innerPadding))
                        }
                        if (state.isLoading) {
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
                            if (state.prestaLongTermLoansRequestsList?.content.isNullOrEmpty()) {
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
                                state.prestaLongTermLoansRequestsList?.content?.map { loanlistingData ->
                                    item {
                                        Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                            LoanApplicationProgressCard(
                                                loanProductName = loanlistingData.loanProductName,
                                                loanAmount = "KES " + loanlistingData.loanAmount.toString(),
                                                loanApplicationDate = loanlistingData.loanDate,
                                                loanProgress = "${loanlistingData.loanRequestProgress}% ${loanlistingData.applicationStatus}",
                                                loanApplicationProgress = loanlistingData.loanRequestProgress.toFloat() / 100,
                                                onClickContainer = {
                                                    onEvent(ApplyLongTermLoansStore.Intent.ClearExisting)
                                                    loanRequestRefId = loanlistingData.refId
                                                    loanAmount =
                                                        loanlistingData.loanAmount.toString()
                                                    loanNumber = loanlistingData.loanRequestNumber
                                                    loanRequestNumber =
                                                        loanlistingData.loanRequestNumber
                                                    scope.launch {
                                                        modalBottomSheetState.show()
                                                    }
                                                })
                                        }
                                    }

                                }
                                item {
                                    Spacer(modifier = Modifier.padding(top = 100.dp))
                                }

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

@Composable
fun LinearProgressWithPercentage(
    progress: Float,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp))
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanApplicationProgressCard(
    loanProductName: String,
    loanAmount: String,
    loanApplicationDate: String,
    loanProgress: String,
    loanApplicationProgress: Float,
    onClickContainer: () -> Unit,
) {
    ElevatedCard(
        onClick = onClickContainer,
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Column(
                modifier = Modifier.padding(top = 15.dp, bottom = 17.dp, start = 17.dp, end = 17.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = loanProductName,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                    Text(
                        text = loanAmount,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 11.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = loanApplicationDate,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
                    )
                    Text(
                        text = loanProgress,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = loanApplicationProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(16.dp))
                            .height(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorDataCard(
    loanProductName: String,
    loanAmount: String,
    loanApplicationProgress: Float,
    onClick: (Int) -> Unit,
    expandContent: Boolean,
    index: Int,
    dataListingColumn: @Composable () -> Unit
) {
    var showExpanded by remember { mutableStateOf(false) }
    ElevatedCard(
        onClick = {
            onClick.invoke(index)
            showExpanded = !showExpanded

        },
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.Transparent)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Column(
                modifier = Modifier.padding(top = 15.dp, bottom = 17.dp, start = 17.dp, end = 17.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = loanProductName,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                    Text(
                        text = loanAmount,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 11.dp)
                ) {
                    LinearProgressWithPercentage(progress = loanApplicationProgress,
                        onClick = {
                            onClick.invoke(index)
                            showExpanded = !showExpanded

                        })
                }
                //Animated Visibility
                AnimatedVisibility(expandContent) {
                    dataListingColumn()
                }


            }
        }
    }
}

@Composable
fun shimmerTextContainer(showLoadingShimmer: Boolean) {
    Text(
        modifier = Modifier.background(
            brush = ShimmerBrush(
                targetValue = 1300f,
                showShimmer = showLoadingShimmer
            ),
            shape = RoundedCornerShape(12.dp)
        ).defaultMinSize(100.dp),
        text = "",
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f),
        fontSize = 14.sp
    )

}


