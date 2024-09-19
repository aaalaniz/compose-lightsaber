package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
data object IosLightsaberScreen : LightsaberScreen