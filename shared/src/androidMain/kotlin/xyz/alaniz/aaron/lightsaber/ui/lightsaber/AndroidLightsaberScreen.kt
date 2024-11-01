package xyz.alaniz.aaron.lightsaber.ui.lightsaber

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding


@Inject
@Parcelize
@ContributesBinding(scope = AppScope::class, boundType = LightsaberScreen::class)
data object AndroidLightsaberScreen : LightsaberScreen, Parcelable