package composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.actionButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ActionButton(label:String,onClickContainer: () -> Unit){
    ElevatedCard(onClick = onClickContainer,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = actionButtonColor)){
        Text(text = label,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 11.dp, bottom = 11.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}