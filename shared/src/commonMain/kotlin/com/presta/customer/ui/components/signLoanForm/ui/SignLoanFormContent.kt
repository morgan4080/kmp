package com.presta.customer.ui.components.signLoanForm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.rememberWebViewState
import com.presta.customer.ImageConverter
import com.presta.customer.MR
import com.presta.customer.getPlatformName
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signLoanForm.SignLoanFormComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun SignLoanFormContent(
    component: SignLoanFormComponent,
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
    if (component.loanRequestRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            component.loanRequestRefId
        ) {
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

    LaunchedEffect(state.prestaLoanByLoanRequestRefId, state.prestaLoanByLoanRequestRefId?.applicantSigned, webViewDisposed) {
        if (state.prestaLoanByLoanRequestRefId != null) {
            if (state.prestaLoanByLoanRequestRefId.applicantSigned != null) {
                if (state.prestaLoanByLoanRequestRefId.applicantSigned) {
                    println("SHOW DOCUMENT SIGNED")
                    onDocumentSigned(loanNumber,amount)
                }
                if (webViewDisposed && !state.prestaLoanByLoanRequestRefId.applicantSigned) {
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
            NavigateBackTopBar("Sign Loan Form", onClickContainer = {
                component.onBackNavClicked()
            })
        }, content = { innerPadding ->
            if (state.prestaLoanByLoanRequestRefId?.pdfThumbNail == null) {
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(1) {
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top= 25.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
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
                                        .defaultMinSize(100.dp, 100.dp)
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

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 25.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            state.prestaLoanByLoanRequestRefId?.pendingReason?.let {
                                Text(
                                    modifier = Modifier.width(200.dp),
                                    text = "$it. Please contact admin for assistance.",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                                    textAlign = TextAlign.Center,
                                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                                )
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
                                "Proceed to Sign your Loan " + component.loanNumber + " of KES " + component.amount,
                                fontSize = 16.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            "Proceed Below to sign your Loan form",
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
                                        actorType = ActorType.APPLICANT
                                    )
                                }?.let {
                                    onEvent(
                                        it
                                    )
                                }
                            },
                            loading =  state.isLoading
                        )
                    }
                }
            }
        })
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Base64ToImage(base64String: String) {
    val imageConverter = ImageConverter()
    val bitmap by imageConverter.decodeBase64ToBitmap(base64String).collectAsState()
    if (bitmap != null) {
        Image(
            bitmap = bitmap as ImageBitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(top = 20.dp)
                .fillMaxHeight(0.9f)
                .fillMaxWidth()

        )
    } else {
        Image(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(top = 20.dp)
                .fillMaxHeight(0.9f)
                .fillMaxWidth(),
            painter = painterResource("imgplaceholder.xml"),
            contentDescription = "image placeholder"
        )
    }
}

@Composable
fun ViewWebView(signURL: String, onEvent: (ApplyLongTermLoansStore.Intent) -> Unit, disposed: () -> Unit, created: () -> Unit) {
    val stateWebView = rememberWebViewState(
        url = signURL
    )

    val settings = stateWebView.settings

    settings.javaScriptEnabled = true
    if (getPlatformName() == "Android") {
        settings.androidSettings.supportZoom = true
        settings.androidSettings.domStorageEnabled = true
    } else {
        println("IOS")
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        WebView(
            state = stateWebView,
            modifier = Modifier
                .fillMaxSize(),
            captureBackPresses = false,
            onCreated = {
                created()
            },
            onDispose = {
                disposed()
            }
        )

        if (stateWebView.isLoading)
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

        IconButton(
            onClick = {
                onEvent(
                    ApplyLongTermLoansStore.Intent.CloseWebView
                )
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Filled.Cancel,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}




