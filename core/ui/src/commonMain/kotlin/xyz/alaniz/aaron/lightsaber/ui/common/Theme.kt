package xyz.alaniz.aaron.lightsaber.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColors(
    primary = Color.Black,
    primaryVariant = Color.Black,
    secondary = Color.White,
    secondaryVariant = GalaxyCream,
    background = Color.Black,
    surface = Color.Black,
    error = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White,
)

private val LightColorScheme = lightColors(
    primary = GalaxyCream,
    primaryVariant = Color.White,
    secondary = Color.Black,
    secondaryVariant = Color.Black,
    background = GalaxyCream,
    surface = GalaxyCream,
    error = GalaxyCream,
    onPrimary = Color.Black,
    onSecondary = GalaxyCream,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.Black,
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