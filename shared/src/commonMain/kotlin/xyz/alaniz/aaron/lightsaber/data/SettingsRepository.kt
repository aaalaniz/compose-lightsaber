package xyz.alaniz.aaron.lightsaber.data

import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject
import xyz.alaniz.aaron.lightsaber.di.DataScope
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberGreen

private val defaultBladeColor = LightsaberGreen.value

@Inject
@DataScope
class SettingsRepository(
    scope: CoroutineScope,
    private val dataStore: DataStore<Preferences>
) {
    private val bladeColorKey = longPreferencesKey("lightsaber_blade_color")

    private val defaultSettings = LightsaberSettings(
        bladeColor = Color(defaultBladeColor)
    )

    val lightsaberSettings: StateFlow<LightsaberSettings> = dataStore.data.map {
        val bladeColor: ULong = it[bladeColorKey]?.toULong() ?: defaultBladeColor
        LightsaberSettings(bladeColor = Color(bladeColor))
    }.stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = defaultSettings)

    suspend fun saveSettings(lightsaberSettings: LightsaberSettings) {
        dataStore.edit {
            it[bladeColorKey] = lightsaberSettings.bladeColor.value.toLong()
        }
    }
}