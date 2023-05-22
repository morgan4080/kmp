package components.processingTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.NavigateBackTopBar

@Composable
fun ProcessingTransactionScreen(component: ProcessingTransactionComponent) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            NavigateBackTopBar("", onClickContainer ={

            } )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 26.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Phone Number")
                Text(
                    text = "+254 724 482 047",
                    modifier = Modifier.padding(start = 5.dp)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "KSH 5000")
                Text(
                    text = "FEES 30",
                    modifier = Modifier
                        .padding(start = 5.dp)
                )

            }

        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(70.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.then(Modifier.size(60.dp)),

                        )
                }

            }
            Row(modifier = Modifier.padding(start = 80.dp, end = 80.dp, top = 22.dp)) {

                Text(
                    text = "Your transaction is being processed...…….……..",
                    fontSize = 14.sp
                )

            }

        }

    }
}