package de.showcase.tezos_viewer.domains.shared.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp

@Composable
fun BlockComposable(dimension: Dp) {
    Canvas(
        modifier = Modifier
            .size(dimension)
    ) {
        drawBlock(dimension)
    }
}

fun DrawScope.drawBlock(dimension: Dp) {
        val blockSize = dimension.toPx()
        val x = (size.width - blockSize) / 2
        val y = (size.height - blockSize) / 2

        drawRect(
            color = Color.White,
            // move block to center of canvas
            topLeft = Offset(x, y),
            size = Size(blockSize, blockSize)
        )
}