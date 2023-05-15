package composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.actionButtonColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun  ActionButton(label: String, onClickContainer: () -> Unit, loading: Boolean = false) {
    val loadingState by remember { mutableStateOf(loading) }

    val angle: Float by animateFloatAsState(
        targetValue = (-360).toFloat(),
        animationSpec = tween(
            durationMillis = 1,
            easing = LinearEasing
        )
    )
    ElevatedCard (onClick = onClickContainer,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = actionButtonColor)
    ) {
        Row (modifier = Modifier
            .padding(top = 11.dp, bottom = 11.dp)
            .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (loadingState) {
                Icon(
                    modifier = Modifier.size(22.dp).rotate(angle),
                    imageVector = Icons.Filled.DonutLarge,
                    contentDescription = "Loader",
                    tint = Color.White
                )
            }
            Text(text = label,
                modifier = Modifier.padding(start = 5.dp),
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}