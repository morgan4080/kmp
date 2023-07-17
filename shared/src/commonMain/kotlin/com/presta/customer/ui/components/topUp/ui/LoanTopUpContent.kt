package com.presta.customer.ui.components.topUp.ui

import ShimmerBrush
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.components.topUp.LoanTopUpComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.OptionsSelectionContainer
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanTopUpContent(
    component: LoanTopUpComponent,
    state: ShortTermLoansStore.State,
    profileState: ProfileStore.State,
) {
    var launchPopUp by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf(TextFieldValue()) }
    val allowedMinAmount = component.minAmount
    var currentTerm by remember { mutableStateOf(false) }
    var labelText = ""
    val currentTermText = "Current Term"
    val newTermText = "New Term"
    val radioOptions = listOf(currentTermText, newTermText)
    var isIconPresent by remember { mutableStateOf(false) }
    val loanOperation="topUp"

    val (selectedOption: Int, onOptionSelected: (Int) -> Unit) = remember {
        mutableStateOf(
            -1
        )
    }
    Scaffold(
        modifier = Modifier
            .padding(LocalSafeArea.current)
            .fillMaxHeight(),
        topBar = {
            NavigateBackTopBar("Topup", onClickContainer = {
                component.onBackNavSelected()
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(size = 12.dp))
                        .absolutePadding(
                            left = 0.dp,
                            right = 2.dp,
                            top = 10.dp,
                            bottom = 5.dp
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                top = 23.dp,
                                start = 24.dp,
                                end = 19.5.dp,
                                bottom = 23.dp,
                            ).fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .defaultMinSize(
                                        minHeight = 20.dp,
                                        minWidth = 70.dp
                                    )
                                    .background(
                                        brush = ShimmerBrush(
                                            state.prestaLoanProductById?.offer?.maturityDate == null,
                                            800f
                                        )
                                    )) {
                                    Text(
                                        modifier = Modifier,
                                        text =if (state.prestaLoanProductById?.offer?.maturityDate != null) "Current  loan" else "",
                                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light)
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = if (state.prestaLoanProductById?.offer?.totalAmount != null) "Kes" + formatMoney(
                                            state.prestaLoanProductById.offer.totalAmount
                                        ) else "",
                                        color = MaterialTheme.colorScheme.onBackground, // #002C56
                                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.bold),
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .defaultMinSize(
                                                minHeight = 20.dp,
                                                minWidth = 70.dp
                                            )
                                            .background(
                                                brush = ShimmerBrush(
                                                    state.prestaLoanProductById?.offer?.totalAmount == null,
                                                    800f
                                                )
                                            )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 11.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = if (state.prestaLoanProductById?.offer?.maturityDate != null)component.loanName  else "",
                                        fontSize = 14.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light),
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .defaultMinSize(
                                                minHeight = 20.dp,
                                                minWidth = 100.dp
                                            )
                                            .wrapContentHeight()
                                            .fillMaxWidth(0.5f)
                                            .background(
                                                brush = ShimmerBrush(
                                                    state.prestaLoanProductById?.offer?.maturityDate == null,
                                                    800f
                                                )
                                            ),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )

                                    Text(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .defaultMinSize(
                                                minHeight = 20.dp,
                                                minWidth = 100.dp
                                            )
                                            .defaultMinSize(
                                                minHeight = 20.dp,
                                                minWidth = 80.dp
                                            )
                                            .background(
                                                brush = ShimmerBrush(
                                                    state.prestaLoanProductById?.offer?.maturityDate == null,
                                                    800f
                                                )
                                            )
                                        ,
                                        text = if (state.prestaLoanProductById != null) "Due: " +  state.prestaLoanProductById.offer?.maturityDate.toString().take(10) else " ",
                                        fontSize = 12.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.medium)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Select Loan Term",
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular),
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 14.sp
                    )
                }
            }

            item {
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEachIndexed { index, text ->
                        isIconPresent = text.contains(newTermText)
                        SelectOptionsCheckBox(
                            index = index,
                            text = text,
                            isSelectedOption = selectedOption == index,
                            onSelectOption = {
                                if (it == selectedOption) {
                                    onOptionSelected(-1)
                                } else {
                                    onOptionSelected(it)
                                }

                            },
                            isIconPresent = isIconPresent,
                            showPopUp = launchPopUp,
                            onShowPopUpOption = {
                                if (text.contains(newTermText)) {
                                    launchPopUp = true
                                    onOptionSelected(-1)
                                }
                            }
                        )
                    }
                    if (selectedOption == 0) {
                        currentTerm = true
                    }
                    if (selectedOption == 1) {
                        currentTerm = false
                    }
                }
            }

            item {
                Text(
                    modifier = Modifier,
                    text = "Top up amount",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = fontFamilyResource(MR.fonts.Metropolis.regular),
                    fontSize = 14.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    //Throw Error if amount exceed desired min or max
                    TextInputContainer(
                        "Desired Amount",
                        "",
                        inputType = InputTypes.NUMBER
                    ) {
                        val inputValue: Double? = TextFieldValue(it).text.toDoubleOrNull()
                        if (inputValue != null && state.prestaLoanEligibilityStatus != null) {
                            if (
                                (inputValue >= allowedMinAmount.toDouble() && inputValue <= state.prestaLoanEligibilityStatus.amountAvailable)
                                && (TextFieldValue(
                                    it
                                ).text !== "")
                            ) {
                                amount = TextFieldValue(it)
                                isError = false

                            } else {
                                isError = true
                            }
                        }
                    }
                }
            }

            item {
                if (isError) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                        text = if (state.prestaLoanEligibilityStatus !== null) "min value Ksh $allowedMinAmount max value Ksh ${state.prestaLoanEligibilityStatus.amountAvailable}" else "",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light),
                        color = Color.Red
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 300.dp)
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    ActionButton("Proceed", onClickContainer = {
                        component.onProceedSelected(
                            component.referencedLoanRefId,
                            component.loanRefId,
                            amount.text.toDouble(),
                            component.maxLoanPeriod,
                            LoanType._TOP_UP,
                            component.loanName,
                            component.interestRate,
                            component.loanPeriodUnit,
                            currentTerm,
                            component.minLoanPeriod,
                            loanOperation = loanOperation
                        )
                    }, enabled = amount.text != "" && !isError && (selectedOption > -1))
                }
            }
        }

        if (launchPopUp) {
            Popup {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color.Black.copy(alpha = 0.7f)),
                    verticalArrangement = Arrangement.Center
                ) {

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 25.dp,
                                end = 25.dp,
                                top = 26.dp
                            ),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Set Loan Term(Months)",
                                    modifier = Modifier
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                OptionsSelectionContainer(
                                    label = component.maxLoanPeriod.toString() + " " + component.loanPeriodUnit,
                                    onClickContainer = {
                                        //current term to false
                                        onOptionSelected(1)
                                        labelText =
                                            component.loanName + " at " + component.interestRate + "%/month"

                                    })
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            ElevatedCard(
                                onClick = {
                                    launchPopUp = false
                                    labelText = ""
                                    onOptionSelected(-1)
                                }, modifier = Modifier
                                    .padding(start = 16.dp)
                            ) {

                                Text(
                                    text = "Dismiss",
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .padding(
                                            top = 5.dp,
                                            bottom = 5.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                )
                            }
                            ElevatedCard(
                                onClick = {
                                    launchPopUp = false

                                }, modifier = Modifier
                                    .padding(end = 16.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = actionButtonColor
                                )
                            ) {
                                Text(
                                    text = "Proceed",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(
                                        top = 5.dp,
                                        bottom = 5.dp,
                                        start = 20.dp,
                                        end = 20.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOptionsCheckBox(
    index: Int,
    text: String,
    isSelectedOption: Boolean,
    onSelectOption: (Int) -> Unit,
    isIconPresent: Boolean,
    showPopUp: Boolean,
    onShowPopUpOption: (Boolean) -> Unit,
) {
    ElevatedCard(
        onClick = {
            onSelectOption(index)
            onShowPopUpOption(showPopUp)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Row(
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelectedOption,
                    onCheckedChange = {
                        onSelectOption(index)
                        onShowPopUpOption(showPopUp)
                    }
                )
                Column {
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Metropolis.light)
                    )
                }

                if (isIconPresent) {
                    Row() {

                        Spacer(modifier = Modifier.weight(1f))
                        Icon(

                            Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Forward Arrow",
                            modifier = Modifier.clip(shape = CircleShape)
                                .background(Color(0xFFE5F1F5)),
                            tint = backArrowColor
                        )
                        Spacer(modifier = Modifier.padding(end = 15.dp))
                    }
                }
            }
        }
    }
}


