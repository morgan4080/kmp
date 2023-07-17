package com.presta.customer.ui.components.selectLoanPurpose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.LoanPurposeProductSelectionCard
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLoanPurposeScreen(component: SelectLoanPurposeComponent) {
    var showExpanded by remember { mutableStateOf(false) }
    val height = 0.2f
    val (selectedOption: Int, onOptionSelected: (Int) -> Unit) = remember {
        mutableStateOf(-1)
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
            LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        LoanPurposeProductSelectionCard("Agriculture", myLazyColumn = {
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
                                        showExpanded = !showExpanded

                                    },
                                    shape = RoundedCornerShape(20.dp)
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
                                                    text = "Crop Farming",
                                                    color = MaterialTheme.colorScheme.outline,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                    style = MaterialTheme.typography.bodyMedium
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
                                                            modifier = if (showExpanded) Modifier.size(
                                                                25.dp
                                                            )
                                                                .rotate(90F) else Modifier.size(25.dp),
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                                //expand
                                AnimatedVisibility(showExpanded) {
                                    Column() {

                                        SelectSubProductCheckBox(
                                            index = 0,
                                            text = "Tea",
                                            isSelectedOption = selectedOption == 0,
                                            onSelectOption = {
                                                if (it == selectedOption) {
                                                    onOptionSelected(-1)
                                                } else {
                                                    onOptionSelected(it)
                                                }
                                            },
                                        )
                                        SelectSubProductCheckBox(
                                            index = 0,
                                            text = "Tea",
                                            isSelectedOption = selectedOption == 0,
                                            onSelectOption = {
                                                if (it == selectedOption) {
                                                    onOptionSelected(-1)
                                                } else {
                                                    onOptionSelected(it)
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                        })
                    }
                }
            }
            //Spacer(modifier = Modifier.padding(bottom = 100.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ActionButton(label = "Continue", onClickContainer = {
                    component.onContinueSelected()
                }, enabled = true)
            }
        }
    })
}

@Composable
fun SelectSubProductCheckBox(
    index: Int,
    text: String,
    isSelectedOption: Boolean,
    onSelectOption: (Int) -> Unit,
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
                    text = text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                )
            }
        }
    }
}













