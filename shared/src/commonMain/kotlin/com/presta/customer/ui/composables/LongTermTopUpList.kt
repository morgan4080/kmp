import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LongTermTopUpList.kt
import components.longTermLoans.LongTermLoansComponent
import composables.ActionButton
import composables.TopUpSelectionContainer
=======
import com.presta.customer.ui.components.longTermLoans.LongTermLoansComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.TopUpSelectionContainer
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LongTermTopUpList.kt

@Composable
fun LongTermTopUpList(component: LongTermLoansComponent) {

    Column(modifier = Modifier
        .fillMaxWidth()){

        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(0.2f)){
            Text(text = "Select Loan to Top Up",
                modifier = Modifier
                    .padding(top = 25.dp))

            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(top=10.dp)){

                items(10) {
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/LongTermTopUpList.kt
                    TopUpSelectionContainer()
=======
                   // TopUpSelectionContainer()
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/LongTermTopUpList.kt
                }

            }

        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 26.dp)){

            ActionButton("Proceed", onClickContainer = {

            })

        }

        //Action Button appears above the action Button
        Spacer(modifier = Modifier
            .padding(bottom = 80.dp))

    }


}