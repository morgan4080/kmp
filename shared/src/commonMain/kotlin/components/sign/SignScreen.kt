package components.sign

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import helpers.LocalSafeArea

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignScreen(component: SignComponent) {
    val model by component.model.subscribeAsState()

    // Test the scaling  of the  Modal BottomSheet

    LazyColumn (
        modifier = Modifier
            .fillMaxHeight(1f)
            .padding(LocalSafeArea.current),
        verticalArrangement = Arrangement.Center,
    ) {
        item {

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                    Image(
                        Icons.Outlined.Timelapse,
                        contentDescription = "Coming Soon",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.size(100.dp)
                    )

                }


            }

        }

    }

}