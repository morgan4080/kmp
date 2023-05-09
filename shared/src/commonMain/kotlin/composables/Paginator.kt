package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Paginator(count: Int, currentIndex: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            items(count) {
                when (it) {
                    currentIndex -> {
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(20.dp)
                                .height(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF489AAB.toInt()))
                        )
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5E5E5.toInt()))
                        )
                    }
                }
            }
        }
    }
}