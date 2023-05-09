package components.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.ActionButton
import composables.Paginator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(component: WelcomeComponent) {
    val model by component.model.subscribeAsState()
    val state = rememberLazyListState()

    LazyColumn (
        modifier = Modifier.fillMaxHeight(1f),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            LazyRow (
                modifier = Modifier
                    .fillParentMaxHeight(0.8f),
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
            ) {
                items(items = model.items) { item ->
                    Column(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillParentMaxHeight()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row {
                            Text(
                                text = "Welcome to Presta Customer",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        Row {
                            Text(
                                text = "Loan applications made easy with Presta Sign.",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Row {
                            Image(imageVector = Icons.Outlined.Settings, contentDescription = item)
                        }
                    }
                }
            }
        }

        item {
            Row (modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                Paginator(3, 0)
            }
            Row (modifier = Modifier.padding(16.dp)) {
                ActionButton("Get Started", onClickContainer = {
                    component.onGetStarted()
                })
            }
        }
    }
}