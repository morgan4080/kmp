package com.presta.customer.ui.components.welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.Paginator
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    component: WelcomeComponent
) {
    val model by component.model.subscribeAsState()
    val state = rememberLazyListState()

    Scaffold (
        modifier = Modifier
            .fillMaxHeight(1f)
            .padding(LocalSafeArea.current)
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxHeight(),
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
                                    text = item.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                                )
                            }

                            Row {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                )
                            }

                            Row (modifier = Modifier.padding(top = 50.dp)) {
                                val painter: Painter = painterResource(
                                    when(isSystemInDarkTheme()) {
                                        true -> item.imageDark
                                        false -> item.imageLight
                                    }
                                )

                                Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f))
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
                        component.onGetStarted (
                            onBoardingContext = model.onBoardingContext
                        )
                    })
                }
            }
        }
    }
}