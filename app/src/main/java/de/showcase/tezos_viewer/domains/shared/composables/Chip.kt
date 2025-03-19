package de.showcase.tezos_viewer.domains.shared.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun Chip(label: String, value: String? = null, borderColor: Color = MaterialTheme.colorScheme.outlineVariant) {

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = MaterialTheme.colorScheme.onBackground,
    ) {

       val text = if(value == null) label else "$label $value"
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
