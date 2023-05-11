package components.welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.ActionButton
import composables.Paginator
import dev.icerock.moko.resources.compose.painterResource
import helpers.LocalSafeArea
import prestaCustomer.shared.MR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(component: WelcomeComponent) {
    val model by component.model.subscribeAsState()
    val state = rememberLazyListState()

    LazyColumn (
        modifier = Modifier.fillMaxHeight(1f).padding(LocalSafeArea.current),
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
                            .padding(horizontal = 16.dp, vertical = 30.dp)
                    ) {
                        Row {
                            Text(
                                text = item.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        Row {
                            Text(
                                text = item.title,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 1.em
                            )
                        }

                        Row (modifier = Modifier.padding(top = 50.dp)) {
                            val painter: Painter = painterResource(MR.images.user_education_1)

                            Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f))
                        }

                    }
                }
            }
        }

        item {
            Row (modifier = Modifier.padding(top = 0.dp, bottom = 20.dp)) {
                Paginator(3, state.firstVisibleItemIndex)
            }

            Row (modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 30.dp)) {
                ActionButton("Get Started", onClickContainer = {
                    component.onGetStarted()
                })
            }
        }
    }
}