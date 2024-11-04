package xyz.alaniz.aaron.lightsaber.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import app.cash.burst.Burst
import app.cash.burst.burstValues
import xyz.alaniz.aaron.lightsaber.fixtures.LightsaberColor
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.BladeState
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.Lightsaber
import xyz.alaniz.aaron.lightsaber.ui.lightsaber.LightsaberState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
@Burst
class LightsaberUiTest {

    @Test
    fun bladeShouldNotBeVisible_whenNotActivated(
        bladeState: BladeState = burstValues(BladeState.Initializing, BladeState.Deactivated),
        bladeColor: LightsaberColor,
    ) =
        runLightsaberUiTest(
            initialState = lightSaberState(
                bladeState = bladeState,
                bladeColor = bladeColor.value,
            )
        ) {
            lightsaber {
                seeHandle()
                doNotSeeBlade()
            }
        }

    @Test
    fun givenActivatedLightsaber_whenLightsaberShown_thenBladeShouldBeVisible(
        bladeColor: LightsaberColor
    ) =
        runLightsaberUiTest(
            initialState = lightSaberState(
                bladeState = BladeState.Activated,
                bladeColor = bladeColor.value
            )
        ) {
            lightsaber {
                seeHandle()
                seeBlade()
            }
        }

    private fun runLightsaberUiTest(
        initialState: LightsaberState,
        testBody: ComposeUiTest.() -> Unit
    ) = runComposeUiTest {
        setContent { Lightsaber(lightsaberState = initialState) }
        testBody()
    }

    private fun lightSaberState(
        bladeState: BladeState,
        bladeColor: Color
    ) = LightsaberState(
        bladeState = bladeState,
        bladeColor = bladeColor,
        onEvent = {}
    )
}