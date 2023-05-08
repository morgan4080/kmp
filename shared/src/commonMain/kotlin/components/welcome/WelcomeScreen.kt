package components.welcome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun WelcomeScreen(component: WelcomeComponent) {
    val model by component.model.subscribeAsState()

    LazyColumn {
        items(items = model.items) { item ->
            Text(
                text = item,
                modifier = Modifier.clickable {
                    component.onGetStarted()
                },
            )
        }
    }
}