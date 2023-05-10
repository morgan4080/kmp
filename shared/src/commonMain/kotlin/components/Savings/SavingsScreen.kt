package components.Savings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composables.ActionButton
import composables.currentSavingsContainer
import composables.navigateBackTopBar
import composables.transactionHistoryContainer
import theme.backArrowColor
import theme.containerColor
import theme.labelTextColor

@Composable
fun  savingsScreen(){


    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)){

            Row(modifier = Modifier.fillMaxWidth()){


                navigateBackTopBar("Savings")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)){

                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Enter Savings Amount",
                    color = labelTextColor
                )

                currentSavingsContainer()

                Row(modifier = Modifier.fillMaxWidth().padding(top = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){

                    Text(text = "Transactions ")


                    Row(){
                        Text(text = "See All")

                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = "Forward Arrow",
                            tint = backArrowColor

                        )

                    }
                }

                //Transaction History From the Api
                LazyColumn(modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp, bottom = 50.dp).weight(1f)
                ){

                    item {

                        transactionHistoryContainer()

                    }

                    item {

                        transactionHistoryContainer()

                    }

                    item {

                        transactionHistoryContainer()

                    }

                    item {

                        transactionHistoryContainer()

                    }

                    item {

                        transactionHistoryContainer()

                    }
                    item {

                        transactionHistoryContainer()

                    }

                }

                //Action Button
                Row(modifier = Modifier.padding(bottom = 100.dp)){

                    ActionButton("+ Add Savings", onClickContainer = {


                    })

                }

            }

        }

    }


}