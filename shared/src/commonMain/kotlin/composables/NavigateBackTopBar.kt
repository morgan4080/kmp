package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
import theme.backArrowColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateBackTopBar(label: String,onClickContainer: () -> Unit) {
    CenterAlignedTopAppBar(modifier = Modifier.padding(start = 5.dp),
        title = {
                    Row(modifier = Modifier
                        .fillMaxWidth()){
                        Box(contentAlignment = Alignment.Center){

                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Forward Arrow",
                                    tint = backArrowColor,
                                    modifier = Modifier.clickable {

                                        onClickContainer()

                                    }
                                )

                                Text(
                                    text = label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )

                                Spacer(modifier = Modifier.weight(1f))


                            }


                        }


                    }

        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)


    )

}


