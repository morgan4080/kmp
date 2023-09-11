package com.presta.customer.ui.components.selectLoanPurpose.ui

import ShimmerBrush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.selectLoanPurpose.SelectLoanPurposeComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.LoanPurposeProductSelectionCard
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLoanPurposeContent(
    component: SelectLoanPurposeComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
) {
    var selectedCategoryIndex by remember { mutableStateOf(-1) }
    var selectedSubCategoryIndex by remember { mutableStateOf(-1) }
    var selectedSubCategoryChildrenIndex by remember { mutableStateOf(-1) }
    var parentId by remember { mutableStateOf("") }
    var childId by remember { mutableStateOf("") }
    val (selectedOption: Int, onOptionSelected: (Int) -> Unit) = remember {
        mutableStateOf(-1)
    }
    var loanCategory by remember { mutableStateOf("") }
    var loanPurpose by remember { mutableStateOf("") }
    var loanPurposeCategory by remember { mutableStateOf("") }
    var loanPurposeCategoryCode by remember { mutableStateOf("") }
    if (parentId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            parentId
        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetLongTermLoansProductsSubCategories(
                    token = it.accessToken,
                    parent = parentId
                )

            }?.let {
                onEvent(
                    it
                )
            }
        }
    }

    if (parentId != "" && childId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            parentId,
            childId
        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetLongTermLoansProductsSubCategoriesChildren(
                    token = it.accessToken,
                    parent = parentId,
                    child = childId
                )

            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Select Loan Purpose", onClickContainer = {
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
                    .fillMaxHeight(0.9f)
                    .padding(innerPadding)
            ) {
                if (state.prestaLongTermLoanProductsCategories.isEmpty()) {
                    items(8) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp)
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
                    state.prestaLongTermLoanProductsCategories.mapIndexed() { topIndex, categories ->
                        item {
                            Column(
                                modifier = Modifier
                            ) {
                                LoanPurposeProductSelectionCard(
                                    categories.name,
                                    myLazyColumn = {
                                        Column() {
                                            if (state.isLoading) {
                                                ElevatedCard(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 10.dp, bottom = 10.dp)
                                                        .background(color = MaterialTheme.colorScheme.inverseOnSurface),
                                                    colors = CardDefaults.elevatedCardColors(
                                                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                                    )
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
                                            } else {
                                                state.prestaLongTermLoanProductsSubCategories.mapIndexed { subIndex, loanSubCategory ->
                                                    SelectLoanSubCategoryCard(
                                                        label = loanSubCategory.name,
                                                        index = subIndex,
                                                        onClick = { subCatsIndex: Int ->
                                                            selectedSubCategoryIndex =
                                                                if (selectedSubCategoryIndex == subIndex) -1 else subCatsIndex
                                                            selectedSubCategoryChildrenIndex = -1
                                                            if (selectedSubCategoryIndex == subIndex) {
                                                                childId =
                                                                    state.prestaLongTermLoanProductsSubCategories[selectedSubCategoryIndex].code
                                                                loanPurpose = state.prestaLongTermLoanProductsSubCategories[selectedSubCategoryIndex].name
                                                            }
                                                        },
                                                        expandContent = selectedSubCategoryIndex == subIndex,
                                                        myLazyColumn = {
                                                            Column() {
                                                                state.prestaLongTermLoanProductsSubCategoriesChildren.mapIndexed { subCatChildIndex, loanSubcategoryChildren ->
                                                                    SelectSubProductCheckBox(
                                                                        index = subCatChildIndex,
                                                                        label = loanSubcategoryChildren.name,
                                                                        onSelectOption = { subCatsChildIndex: Int ->
                                                                            selectedSubCategoryChildrenIndex =
                                                                                if (selectedSubCategoryChildrenIndex == subCatsChildIndex) -1 else subCatChildIndex
                                                                            if (selectedSubCategoryChildrenIndex > -1) {
                                                                                if (selectedSubCategoryChildrenIndex == subCatChildIndex) {
                                                                                    loanPurposeCategory =
                                                                                        state.prestaLongTermLoanProductsSubCategoriesChildren[selectedSubCategoryChildrenIndex].name
                                                                                    loanPurposeCategoryCode =
                                                                                        state.prestaLongTermLoanProductsSubCategoriesChildren[selectedSubCategoryChildrenIndex].code
                                                                                }
                                                                            }

                                                                        },
                                                                        isSelectedOption = selectedSubCategoryChildrenIndex == subCatChildIndex,
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    index = topIndex,
                                    onClick = { index: Int ->
                                        selectedCategoryIndex =
                                            if (selectedCategoryIndex == topIndex) -1 else index
                                        selectedSubCategoryIndex = -1
                                        if (selectedCategoryIndex == topIndex) {
                                            parentId =
                                                state.prestaLongTermLoanProductsCategories[selectedCategoryIndex].code
                                            loanCategory =
                                                state.prestaLongTermLoanProductsCategories[selectedCategoryIndex].name
                                        }
                                    },
                                    expandContent = selectedCategoryIndex == topIndex,
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                ActionButton(label = "Continue", onClickContainer = {
                    component.onContinueSelected(
                        loanRefId = component.loanRefId,
                        loanType = component.loanType,
                        desiredAmount = component.desiredAmount,
                        loanPeriod = component.loanPeriod,
                        requiredGuarantors = component.requiredGuarantors,
                        loanCategory = loanCategory,
                        loanPurpose = loanPurpose,
                        loanPurposeCategory = loanPurposeCategory,
                        loanPurposeCategoryCode = loanPurposeCategoryCode
                    )
                }, enabled = loanCategory != "" && loanPurpose != "" && loanPurposeCategory != "")
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLoanSubCategoryCard(
    label: String,
    index: Int,
    onClick: (Int) -> Unit,
    expandContent: Boolean,
    myLazyColumn: @Composable () -> Unit
) {
    var showExpanded by remember { mutableStateOf(false) }
    Column() {
        ElevatedCard(
            modifier = Modifier
                .absolutePadding(
                    left = 2.dp,
                    right = 2.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
            onClick = {
                onClick.invoke(index)
                showExpanded = !showExpanded

            },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 30.dp
            ),
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 5.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 5.dp,
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = label,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(end = 5.dp)
                        )

                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colorScheme.background)
                                .size(20.dp),
                            onClick = {
                                showExpanded = !showExpanded
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    modifier = if (expandContent) Modifier.size(
                                        25.dp
                                    )
                                        .rotate(90F) else Modifier.size(
                                        25.dp
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }
                }
            }
        }
        //Expanded Content
        AnimatedVisibility(expandContent) {
            myLazyColumn()
        }

    }
}

@Composable
fun SelectSubProductCheckBox(
    index: Int,
    label: String,
    onSelectOption: (Int) -> Unit,
    isSelectedOption: Boolean,
) {
    Box(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        .clickable {
            onSelectOption(index)
        }) {
        Row(
            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelectedOption,
                onCheckedChange = {
                    onSelectOption(index)
                }
            )
            Column {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
        }
    }
}
