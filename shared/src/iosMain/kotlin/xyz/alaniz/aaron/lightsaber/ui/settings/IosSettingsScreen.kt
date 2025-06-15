package xyz.alaniz.aaron.lightsaber.ui.settings

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.binding

@ContributesBinding(scope = AppScope::class, binding = binding<SettingsScreen>())
data object IosSettingsScreen : SettingsScreen