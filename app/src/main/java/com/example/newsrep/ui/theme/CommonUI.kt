package com.example.newsrep.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

// Common colors for the app
object AppColors {
    val DeepOrange = Color(0xFFFF5722)
    val BrightOrange = Color(0xFFFF8C00)
    val Golden = Color(0xFFFFD700)
    val LightYellow = Color(0xFFFFEB3B)
    val TextFieldBackground = Color(0xFFE65100)
}

@Composable
fun GradientBackground(
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Animated Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AppColors.DeepOrange,
                            AppColors.BrightOrange,
                            AppColors.Golden,
                            AppColors.BrightOrange,
                            AppColors.DeepOrange
                        ),
                        start = Offset(
                            x = cos(angle * (PI / 180).toFloat()) * 1000f + 500f,
                            y = sin(angle * (PI / 180).toFloat()) * 1000f + 500f
                        ),
                        end = Offset(
                            x = cos((angle + 180) * (PI / 180).toFloat()) * 1000f + 500f,
                            y = sin((angle + 180) * (PI / 180).toFloat()) * 1000f + 500f
                        )
                    )
                )
        )
        content()
    }
} 