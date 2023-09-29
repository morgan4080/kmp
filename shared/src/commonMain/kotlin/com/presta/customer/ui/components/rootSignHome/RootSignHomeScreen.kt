package com.presta.customer.ui.components.rootSignHome

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.presta.customer.ui.components.signAppHome.ui.SignHomeScreen

@Composable
fun RootSignHomeScreen(component: RootSignHomeComponent) {
    Children(
        stack = component.childSignHomeStack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootSignHomeComponent.ChildHomeSign.SignHomeChild -> SignHomeScreen(child.component)
        }
    }
}

