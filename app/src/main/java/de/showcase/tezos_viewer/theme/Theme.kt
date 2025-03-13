@file:Suppress("ObjectPropertyName")

package de.showcase.tezos_viewer.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = TezosDarkPurple,
    inversePrimary = TezosWithe,
    secondary = TezosDarkBlue,
    tertiary = TezosLightGrey,
    background = TezosDarkGrey,
    onBackground = TezosDarkBlue,
    surface = TezosLightBlue,
    surfaceVariant = TezosDarkPurple,
    surfaceBright = TezosLightPurple,
    outline = TezosGreen,
    outlineVariant = TezosCyan,
)

@Composable
fun TezosViewerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val shapes = Shapes(
        small = MaterialTheme.shapes.small,
        medium = RoundedCornerShape(8.dp),
        large = MaterialTheme.shapes.large
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        content = content,
    )

}