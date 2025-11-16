package xyz.alaniz.aaron.lightsaber

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.BladeState
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.Lightsaber
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberState

@Composable
fun MainView() {
    Lightsaber(
        lightsaberState = LightsaberState(
            bladeColor = Color.Red,
            bladeState = BladeState.Deactivated,
            onEvent = {}
        )
    )
}
