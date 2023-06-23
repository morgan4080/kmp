package com.presta.customer.ui.components.transactionHistory.ui

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.transactionHistory.store.TransactionHistoryStore
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.composables.singleTransaction
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class
)
@Composable
fun TransactionHistoryContent(
    authState: AuthStore.State,
    state: TransactionHistoryStore.State,
    onEvent: (TransactionHistoryStore.Intent) -> Unit,
    onBack: () -> Unit,
    onMappingChange: (mapping: List<String>) -> Unit
) {
    var transactionReference by remember {
        mutableStateOf(TextFieldValue())
    }

    val scope = rememberCoroutineScope()

    val tabs = remember { mutableMapOf("0" to "All") }

    LaunchedEffect(state.transactionMapping) {
        if (state.transactionMapping !== null) {
            state.transactionMapping.forEach {
                tabs[it.key] = it.value
            }
        }
    }

    var refreshing by remember { mutableStateOf(false) }

    val refreshScope = rememberCoroutineScope()

    val pagerState = rememberPagerState()

    fun refresh() = refreshScope.launch {
        refreshing = true
        if (state.transactionMapping !== null && authState.cachedMemberData !== null) {
            pagerState.animateScrollToPage(0)
            onEvent(TransactionHistoryStore.Intent.GetTransactionHistory(
                token = authState.cachedMemberData.accessToken,
                refId = authState.cachedMemberData.refId,
                purposeIds = state.transactionMapping.keys.toList(),
                searchTerm = null
            ))
        }
        delay(1500)
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            println("current page")
            println(page)
            if (state.transactionMapping !== null) {
                if (page == 0) {
                    onMappingChange(state.transactionMapping.keys.toList())
                } else {
                    onMappingChange(listOf(page.toString()))
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(LocalSafeArea.current),
        topBar = {
            NavigateBackTopBar("Transaction History", onClickContainer = {
                onBack()
            })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                        .padding(horizontal = 16.dp)
                ) {

                    TextInputContainer(
                        label = "Transaction Reference",
                        inputValue = "",
                        icon = Icons.Outlined.Search,
                        inputType = InputTypes.STRING,
                        callback = {
                            transactionReference = TextFieldValue(it)
                            if (authState.cachedMemberData !== null && state.transactionMapping !== null) {
                                if (pagerState.currentPage == 0) {
                                    onEvent(TransactionHistoryStore.Intent.GetTransactionHistory(
                                        token = authState.cachedMemberData.accessToken,
                                        refId = authState.cachedMemberData.refId,
                                        purposeIds = state.transactionMapping.keys.toList(),
                                        searchTerm = it
                                    ))
                                } else {
                                    onEvent(TransactionHistoryStore.Intent.GetTransactionHistory(
                                        token = authState.cachedMemberData.accessToken,
                                        refId = authState.cachedMemberData.refId,
                                        purposeIds = listOf(pagerState.currentPage.toString()),
                                        searchTerm = it
                                    ))
                                }
                            }
                        }
                    )

                }

                ScrollableTabRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .consumeWindowInsets(LocalSafeArea.current)
                        .padding(top = 25.dp),
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = Color.Transparent,
                    edgePadding = 16.dp,
                    indicator = {}
                ) {
                    tabs.forEach {
                        Tab (
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .defaultMinSize(70.dp)
                                .padding(end = 12.dp)
                                .clip(RoundedCornerShape(70.dp))
                                .background(if (pagerState.currentPage == it.key.toInt()) actionButtonColor else actionButtonColor.copy(
                                    alpha = 0.2f
                                )),
                            text = {
                                Text(
                                    text = it.value,
                                    fontSize = 12.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                    color = if (pagerState.currentPage == it.key.toInt()) Color.White else MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            },
                            selected = pagerState.currentPage == it.key.toInt(),
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(it.key.toInt())
                                }
                            },
                            selectedContentColor = Color.Black,
                            unselectedContentColor = Color.DarkGray
                        )
                    }
                }

                HorizontalPager(
                    pageCount = tabs.size,
                    state = pagerState,
                    flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                ) {
                    Box (modifier = Modifier
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                        .pullRefresh(refreshState).defaultMinSize(minHeight = 500.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            item {
                                singleTransaction(state.transactionHistory)
                            }
                        }

                        PullRefreshIndicator(refreshing, refreshState,
                            Modifier
                                .align(Alignment.TopCenter).zIndex(1f),
                            contentColor = actionButtonColor
                        )
                    }
                }
            }
        }
    }
}