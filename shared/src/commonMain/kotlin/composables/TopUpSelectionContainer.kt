package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopUpSelectionContainer() {

    var checkedState by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checkedState,
                onCheckedChange = { checkedState = it },
                modifier = Modifier.clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .height(20.dp)
                    .width(20.dp),
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.secondaryContainer)
            )

            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = "Emergency Loan")
                Text(
                    text = "min 10,000-max 50,000",
                    fontSize = 10.sp
                )

            }
        }

        Column() {

            Text(text = "bal Kes 5000.00")
            Text(
                text = "Due- 12 May 2020",
                fontSize = 10.sp
            )

        }

    }

}