package com.presta.customer.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashLMS(text: String = "LMS", callback: () -> Unit) {
    val scale2 = remember {
        Animatable(0f)
    }
    val scaleOut = remember {
        Animatable(1f)
    }
    val op = remember {
        Animatable(0f)
    }

    // AnimationEffect
    LaunchedEffect(key1 = true) {
        scale2.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutLinearInEasing
            )
        )
        op.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutLinearInEasing
            )
        )
        scaleOut.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = 1000,
                easing = FastOutLinearInEasing
            )
        )
        callback()
    }

    // Image
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize().scale(scaleOut.value),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.scale(scale2.value).size(75.dp).absoluteOffset(y = (-75).dp).graphicsLayer {
                    alpha = op.value
                },
                painter = painterResource("lms_logo.xml"),
                contentDescription = "Pattern"
            )
            Text(
                modifier = Modifier.scale(scale2.value).graphicsLayer {
                    alpha = op.value
                },
                text = text,
                color= MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBoldItalic),
                textAlign = TextAlign.Center
            )
        }
    }
}