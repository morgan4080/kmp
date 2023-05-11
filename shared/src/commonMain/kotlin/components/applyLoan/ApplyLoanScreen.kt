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
import composables.navigateBackTopBar
import composables.productSelectionCard2
import theme.labelTextColor

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
            Row(modifier = Modifier.fillMaxWidth()){


                navigateBackTopBar("Apply Loan")

            }
        }

        item {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .blur(12.dp)
                .fillMaxHeight()
            ){


                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Select Loan Type",
                    color = MaterialTheme.colorScheme.onBackground
                )

                productSelectionCard2("Short Term Loan", onClickContainer = {



                })
                productSelectionCard2("Long Term Loan", onClickContainer = {


                })
            }
        }
    }
}
