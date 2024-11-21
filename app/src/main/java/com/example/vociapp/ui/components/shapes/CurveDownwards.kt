package com.example.vociapp.ui.components.shapes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class AnimatedCurvatureDownShape : Shape {
    private var animationProgress by mutableFloatStateOf(0f)

    fun updateAnimationProgress(progress: Float) {
        animationProgress = progress
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)

        // Control point for the curve
        val controlX = size.width / 2f
        val controlY = size.height / 3f * animationProgress

        // Draw the curve
        path.quadraticTo(
            controlX, controlY,
            size.width, 0f
        )

        return Outline.Generic(path)
    }
}

@Composable
fun CurvedDownwardShape() {
    val shape = remember { AnimatedCurvatureDownShape() }
    val progress by animateFloatAsState(
        targetValue = 1f, // Animate to 100% progress
        animationSpec = tween(durationMillis = 1000), label = "" // 1-second animation
    )

    LaunchedEffect(progress) {
        shape.updateAnimationProgress(progress)
    }

    // Apply the shape to a composable
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondary)
    )
}