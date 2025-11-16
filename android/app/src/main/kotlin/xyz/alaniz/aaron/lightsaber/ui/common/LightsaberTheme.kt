package xyz.alaniz.aaron.lightsaber.ui.common

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

@Composable
fun LightsaberTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = darkColors(),
        content = content
    )
}
