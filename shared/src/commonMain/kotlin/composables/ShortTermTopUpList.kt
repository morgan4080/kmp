package composables

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

@Composable
fun  ShortTermTopUpList(){
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
                    TopUpSelectionContainer()
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