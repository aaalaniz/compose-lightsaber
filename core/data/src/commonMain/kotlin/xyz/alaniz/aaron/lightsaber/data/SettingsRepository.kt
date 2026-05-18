package xyz.alaniz.aaron.lightsaber.data

import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberGreen

private val defaultBladeColor = LightsaberGreen.value

interface SettingsRepository {
    val lightsaberSettings: StateFlow<LightsaberSettings>

    suspend fun saveSettings(lightsaberSettings: LightsaberSettings)
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealSettingsRepository(
    scope: CoroutineScope,
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private val bladeColorKey = longPreferencesKey("lightsaber_blade_color")

    private val defaultSettings = LightsaberSettings(
        bladeColor = Color(defaultBladeColor)
    )

    override val lightsaberSettings: StateFlow<LightsaberSettings> = dataStore.data.map {
        val bladeColor: ULong = it[bladeColorKey]?.toULong() ?: defaultBladeColor
        LightsaberSettings(bladeColor = Color(bladeColor))
    }.stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = defaultSettings)

    override suspend fun saveSettings(lightsaberSettings: LightsaberSettings) {
        dataStore.edit {
            it[bladeColorKey] = lightsaberSettings.bladeColor.value.toLong()
        }
    }
}