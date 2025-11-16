package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import androidx.compose.ui.graphics.Color

data class LightsaberState(
    val bladeColor: Color,
    val bladeState: BladeState,
    val onEvent: (LightsaberEvent) -> Unit
)

sealed interface LightsaberEvent {
    object SettingsSelected : LightsaberEvent
    object LightsaberActivating : LightsaberEvent
    object LightsaberDeactivating : LightsaberEvent
    object LightsaberActivated : LightsaberEvent
    object LightsaberDeactivated : LightsaberEvent
}

enum class BladeState {
    Initializing,
    Activating,
    Activated,
    Deactivating,
    Deactivated
}
