package xyz.alaniz.aaron.lightsaber.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColors(
    primary = Color.White,
    secondary = Color.Black,
)

private val LightColorScheme = lightColors(
    primary = Color.Black,
    secondary = Color.White,
    background = GalaxyCream
)

@Composable
fun LightsaberTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colors = colorScheme,
        content = content
    )
}