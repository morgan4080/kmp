package composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.shortTermLoans.ShortTermLoansComponent

@Composable
fun ShortTermProductList(component: ShortTermLoansComponent) {
    //update
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Select Loan Product",
            modifier = Modifier.padding(top = 25.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    ProductSelectionCard("Emergency Loan ", "Interest 12%", onClickContainer = {
                        //Navigate  to  EmergencyLoan Screen
                        //component.onEmergencyLoanSelected()
                        //Todo -Configure components
                        println("Navigate to Emergency Clicked")
                        component.onSelected("em")

                    })
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Mkopo Halisi Loan ", "Interest 12%", onClickContainer = {

                    })
                }
            }

            item {

                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Shule Loan ", "Interest 12%", onClickContainer = {

                    })
                }
            }

            item {

                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Mfanisi Loan ", "Interest 12%", onClickContainer = {
                    })
                }

            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    ProductSelectionCard("Normal Loan ", "Interest 12%", onClickContainer = {

                    })
                }
            }
        }
    }
}


