package de.showcase.tezos_viewer.domains.shared.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedBackground(
    label: String = "Background Animation [Gradient]",
    enabled: Boolean = true,
    duration: Int = 3500,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.surfaceBright
    ),
    content: @Composable () -> Unit,
) {
    if(!enabled) return Column{ content() }

    val transition = rememberInfiniteTransition(label)

    val animatedValue by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = label
    )

    val gradientColors = listOf(
        colors[0].copy(alpha = 1f - animatedValue),
        colors[1].copy(alpha = animatedValue),
        colors[2].copy(alpha = animatedValue)
    )

    val brush = Brush.linearGradient(gradientColors)

    Column(
        modifier = Modifier
            .background(brush)
    ) {
        content()
    }
}