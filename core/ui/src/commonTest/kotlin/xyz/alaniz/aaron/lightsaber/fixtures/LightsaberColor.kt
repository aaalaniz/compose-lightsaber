package xyz.alaniz.aaron.lightsaber.fixtures

import androidx.compose.ui.graphics.Color
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberBlue
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberGreen
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberPurple
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberRed
import xyz.alaniz.aaron.lightsaber.ui.common.LightsaberYellow

enum class LightsaberColor(val value: Color) {
    Green(value = LightsaberGreen),
    Red(value = LightsaberRed),
    Yellow(value = LightsaberYellow),
    Blue(value = LightsaberBlue),
    Purple(value = LightsaberPurple)
}