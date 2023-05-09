package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import theme.backArrowColor
import theme.containerColor
import theme.labelTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navigateBackTopBar(label:String){
    CenterAlignedTopAppBar(
        title = {

            Row(modifier = Modifier.background(color = containerColor),
            verticalAlignment = Alignment.CenterVertically){

                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Navigates back",
                        tint = backArrowColor
                    )
                }

                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 1.dp),
                    color = labelTextColor
                )
                Spacer(modifier = Modifier.weight(1f))

            }

        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = containerColor)

    )

}


