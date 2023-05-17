package components.applyLoan

import ApplyLoanComponent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.NavigateBackTopBar
import composables.ProductSelectionCard2

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun  ApplyLoanScreen(component: ApplyLoanComponent, innerPadding: PaddingValues){

    val model by component.model.subscribeAsState()

    LazyColumn(modifier = Modifier
        .consumeWindowInsets(innerPadding)
        .background(color = MaterialTheme.colorScheme.background),
        contentPadding = innerPadding

    ){

        item {

            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()){

                Row(modifier = Modifier.fillMaxWidth()){

                    NavigateBackTopBar("Apply Loan")

                }


                Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp)){


                    Text(modifier = Modifier,
                        text = "Select Loan Type",
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(modifier = Modifier.fillMaxWidth().padding(top=16.dp)){
                        ProductSelectionCard2("Short Term Loan", onClickContainer = {



                        })

                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(top=10.dp)){
                        ProductSelectionCard2("Long Term Loan", onClickContainer = {


                        })


                    }

                }



            }


            }

        }


    }

