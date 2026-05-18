package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.binding

@ContributesBinding(scope = AppScope::class, binding = binding<LightsaberScreen>())
data object IosLightsaberScreen : LightsaberScreen