package components.savingsTransactionHistoryScreen

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
import composables.TransactionHistoryContainer

@Composable
fun SavingsTransactionHistoryScreen() {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(top = 20.dp)) {

            Text(text = "Savings")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            items(30) {
                TransactionHistoryContainer()
            }
            //creates a space to view all the scrollable items Below the Bottom App bar
            item {

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                )
            }

        }

    }
}