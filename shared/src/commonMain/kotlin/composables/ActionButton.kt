package composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import theme.actionButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ActionButton(label: String, onClickContainer: () -> Unit, loading: Boolean = false) {

    ElevatedCard (onClick = { if (!loading) onClickContainer() },
        modifier = Modifier.fillMaxWidth().alpha(if (!loading) 1f else 0.5f),
        colors = CardDefaults.cardColors(containerColor = actionButtonColor)
    ) {
        Row (modifier = Modifier
            .padding(top = 11.dp, bottom = 11.dp)
            .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp).padding(end = 2.dp),
                    color = Color.White
                )
            }
            Text(text = label,
                modifier = Modifier.padding(start = 5.dp),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                    letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                )
            )
        }
    }
}