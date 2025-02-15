package com.voci.app.ui.components.core

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun StatusLED(
    color: Color,                   // Color of the LED
    isPulsating: Boolean = true,    // Whether the LED should pulse or not
    modifier: Modifier = Modifier   // Modifier for the LED
) {
    // Pulsating shader
    if (isPulsating) {
        // Animation time in milliseconds
        val animationTime = remember { Random.nextInt(1000, 1500) }
        // Infinite transition for the pulsation
        val infiniteTransition = rememberInfiniteTransition(label = "led_pulsation")
        // Alpha value for the shader
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.5f,                // Initial alpha value
            targetValue = 1f,                   // Target alpha value
            animationSpec = infiniteRepeatable( // Infinitely repeating animation
                animation = tween(durationMillis = animationTime, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "led_alpha"
        )
        val glowColor = color.copy(alpha = alpha)   // Glow shader
        val glowRadius = 100.dp                     // Glow radius
        // Shader brush for the glow
        val glowShaderBrush = remember(glowColor, glowRadius) {
            object : ShaderBrush() {
                override fun createShader(size: Size): Shader {
                    // Create a radial gradient shader with the glow color and radius
                    val biggerDimension = kotlin.comparisons.maxOf(size.height, size.width)
                    return RadialGradientShader(
                        colors = listOf(glowColor, Color.Transparent),
                        center = size.center,
                        radius = biggerDimension / 2f,
                        tileMode = TileMode.Clamp
                    )
                }
            }
        }
        // Box with the glow shader
        Box(
            modifier = modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(glowShaderBrush)
        )
    } else {
        // Just shader with no pulsating
        val staticShaderBrush = remember(color) {
            object : ShaderBrush() {
                override fun createShader(size: Size): Shader {
                    // Create a radial gradient shader with the color
                    val biggerDimension = maxOf(size.height, size.width)
                    return RadialGradientShader(
                        colors = listOf(color, Color.Transparent),
                        center = size.center,
                        radius = biggerDimension / 2f,
                        tileMode = TileMode.Clamp
                    )
                }
            }
        }
        // Box with the shader
        Box(
            modifier = modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(staticShaderBrush)
        )
    }
}