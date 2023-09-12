package com.presta.customer.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashLogo(callback: () -> Unit) {
    val scale = remember {
        Animatable(0f)
    }
    val rotation = remember {
        Animatable(0f)
    }

    // AnimationEffect
    LaunchedEffect(key1 = true) {
        rotation.animateTo(
            targetValue = 180f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutLinearInEasing
            )
        )
        delay(1000)
        scale.animateTo(
            targetValue = 3.5f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutLinearInEasing
            )
        )
        delay(3000)
        callback()
    }

    // Image
    Column(modifier = Modifier.fillMaxSize().graphicsLayer {
        rotationY = rotation.value
    }) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                modifier = Modifier.scale(scale.value).rotate(rotation.value),
                painter = painterResource("26.png"),
                contentDescription = "Pattern"
            )
        }
    }
}