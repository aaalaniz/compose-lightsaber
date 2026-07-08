package xyz.alaniz.aaron.lightsaber.feature.lightsaber

import android.os.Parcelable
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding
import kotlinx.parcelize.Parcelize


@Parcelize
@ContributesBinding(scope = AppScope::class, binding = binding<LightsaberScreen>())
data object AndroidLightsaberScreen : LightsaberScreen, Parcelable
