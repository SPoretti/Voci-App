package com.voci.app.ui.components.core

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SwipeableScreen(
    navController: NavHostController,
    targetRoute: String,
    direction: SwipeDirection,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 60.dp.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                when(direction){
                    SwipeDirection.LEFT -> {
                        detectHorizontalDragGestures { change, dragAmount ->
                            offsetX -= dragAmount
                            if (offsetX < -swipeThreshold) {
                                navController.navigate(targetRoute)
                            }
                        }
                    }
                    SwipeDirection.RIGHT -> {
                        detectHorizontalDragGestures { change, dragAmount ->
                            offsetX += dragAmount
                            if (offsetX > swipeThreshold) {
                                navController.navigate(targetRoute)
                            }
                        }
                    }
                }
            }
    ) {
        content()
    }
}