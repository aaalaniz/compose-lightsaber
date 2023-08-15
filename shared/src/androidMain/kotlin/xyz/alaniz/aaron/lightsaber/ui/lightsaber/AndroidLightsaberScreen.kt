package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Inject
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberScreen


@Inject
@Parcelize
data object AndroidLightsaberScreen : LightsaberScreen, Parcelable