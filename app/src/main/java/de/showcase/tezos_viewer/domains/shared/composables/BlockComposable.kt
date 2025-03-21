package de.showcase.tezos_viewer.domains.shared.composables

import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp

@Composable
fun TransactionComposable(
    dimension: Dp,
    lineColor: Color,
    textColor: Color
) {
    Canvas(
        modifier = Modifier
            .fillMaxHeight()
            .size(dimension)
    ) {
        drawBlock(
            dimension = dimension,
            lineColor = lineColor,
            textColor = textColor
        )
    }
}

fun DrawScope.drawBlock(
    dimension: Dp,
    lineColor: Color,
    textColor: Color
) {
    val blockSize = dimension.toPx() / 2
    val x = (size.width - blockSize) / 2
    val y = (size.height - blockSize) / 2

    drawLine(
        color = lineColor,
        start = Offset(size.width / 2, 0f),
        end = Offset(size.width / 2, blockSize),
        strokeWidth = 2f,
    )

    drawLine(
        color = lineColor,
        start = Offset(size.width / 2, blockSize * 2),
        end = Offset(size.width / 2, size.height),
        strokeWidth = 2f,
    )

    drawCircle(
        color = lineColor,
        radius = blockSize,
        center = Offset(size.width / 2, size.height / 2),
        style = Stroke(width = 2f)
    )

    drawRect(
        color = lineColor,
        // move block to center of canvas
        topLeft = Offset(x, y),
        style = Stroke(width = 2f),
        size = Size(blockSize, blockSize),
    )

    val sizeOfText = blockSize * 0.75f
    val xOfText = size.width / 2 - blockSize / 4f
    val yOfText = size.height / 2 + blockSize / 8f

    drawContext
        .canvas
        .nativeCanvas
        .drawText("ꜩ", xOfText, yOfText, Paint().apply {
            color = textColor.hashCode()
            textSize = sizeOfText
        })
}