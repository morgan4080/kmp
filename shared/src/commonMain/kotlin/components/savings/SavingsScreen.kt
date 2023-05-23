package components.savings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.ActionButton
import composables.CurrentSavingsContainer
import composables.NavigateBackTopBar
import composables.TransactionHistoryContainer
import helpers.LocalSafeArea
import theme.backArrowColor
import theme.labelTextColor

@Composable
fun SavingsScreen(component: SavingsComponent) {
    val model by component.model.subscribeAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(LocalSafeArea.current),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NavigateBackTopBar("Savings", onClickContainer = {

                    })

                }
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .background(color = Color.Transparent)
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Enter Savings Amount",
                        color = labelTextColor
                    )
                    CurrentSavingsContainer()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Transactions ")
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                component.onSeeALlSelected()

                            }

                        ) {
                            Box(contentAlignment = Alignment.Center){
                                Row(){
                                    Text(text = "See all",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier)

                                    Icon(
                                        Icons.Filled.ArrowForward,
                                        contentDescription = "Forward Arrow",
                                        tint = backArrowColor,
                                    )

                                }

                            }

                        }
                    }
                    //Transaction History From the Api
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 1.dp)
                            .weight(1f)
                    ) {
                        items(20) {
                            TransactionHistoryContainer()
                        }
                    }

                    //Action Button
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                        .padding(bottom = 100.dp)) {
                        ActionButton("+ Add Savings", onClickContainer = {
                           //Navigate to Add savings Screen
                            component.onAddSavingsSelected()
                            println("Button clicked")
                        })
                    }
                }
            }
        }
    }
}